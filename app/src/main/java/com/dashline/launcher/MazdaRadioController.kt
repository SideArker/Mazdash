// Copyright (C) 2026 Mazdash Project
// SPDX-License-Identifier: GPL-3.0-only

package com.dashline.launcher

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import com.nwd.radio.service.RadioCallback
import com.nwd.radio.service.RadioConstant
import com.nwd.radio.service.RadioFeature
import com.nwd.radio.service.data.Frequency
import java.util.concurrent.CopyOnWriteArrayList

/**
 * Controller for the native Allwinner T3 radio tuner hardware (com.nwd.radio.service).
 *
 * Communicates with the hardware MCU via AIDL [RadioFeature] and [RadioCallback].
 * When running in testing mode (e.g. on a phone), provides a seamless interactive mock
 * allowing full UI testing of tuning, band switching, and preset storage.
 */
class MazdaRadioController private constructor(private val appContext: Context) {

    companion object {
        private const val TAG = "MazdaRadioController"

        @Volatile
        private var instance: MazdaRadioController? = null

        fun getInstance(context: Context): MazdaRadioController {
            return instance ?: synchronized(this) {
                instance ?: MazdaRadioController(context.applicationContext).also {
                    instance = it
                }
            }
        }

        fun formatFrequency(band: Byte, frequency: Int): String {
            return if (isAmBand(band)) {
                "$frequency kHz"
            } else {
                val mhz = if (frequency > 50000) frequency / 1000f else frequency / 100f
                String.format(java.util.Locale.US, "%.2f MHz", mhz)
            }
        }

        fun isAmBand(band: Byte): Boolean {
            return band == RadioConstant.BandType.AM1 ||
                    band == RadioConstant.BandType.AM2 ||
                    band == RadioConstant.BandType.AM3
        }

        fun bandName(band: Byte): String = when (band) {
            RadioConstant.BandType.FM1 -> "FM1"
            RadioConstant.BandType.FM2 -> "FM2"
            RadioConstant.BandType.FM3 -> "FM3"
            RadioConstant.BandType.AM1 -> "AM1"
            RadioConstant.BandType.AM2 -> "AM2"
            RadioConstant.BandType.AM3 -> "AM3"
            else -> "FM"
        }
    }

    interface Listener {
        fun onFrequencyChanged(band: Byte, frequency: Int, psName: String?)
        fun onPresetsUpdated(presets: List<Frequency>)
        fun onStereoChanged(isStereo: Boolean)
        fun onRtMessage(message: String)
        fun onRadioState(state: Byte)
    }

    private val mainHandler = Handler(Looper.getMainLooper())
    private val listeners = CopyOnWriteArrayList<Listener>()

    private var radioFeature: RadioFeature? = null
    private var isBound = false

    // State
    var currentBand: Byte = RadioConstant.BandType.FM1
        private set
    var currentFrequency: Int = 10560 // 105.60 MHz default
        private set
    var currentPsName: String = "RMF FM"
        private set
    var currentRtMessage: String = "Najlepsza Muzyka - RMF FM"
        private set
    var isStereo: Boolean = true
        private set

    val presets = ArrayList<Frequency>().apply {
        add(Frequency(RadioConstant.BandType.FM1, 10560, "RMF FM"))
        add(Frequency(RadioConstant.BandType.FM1, 10750, "Radio ZET"))
        add(Frequency(RadioConstant.BandType.FM1, 9880, "ESKA"))
        add(Frequency(RadioConstant.BandType.FM1, 9030, "Trójka"))
        add(Frequency(RadioConstant.BandType.FM1, 10100, "Jedynka"))
        add(Frequency(RadioConstant.BandType.FM1, 10300, "Antyradio"))
    }

    private val radioCallback = object : RadioCallback.Stub() {
        override fun notifyCurrentFrequency(band: Byte, freq: Int, psName: String?, param: Int) {
            mainHandler.post {
                currentBand = band
                currentFrequency = freq
                currentPsName = psName ?: ""
                listeners.forEach { it.onFrequencyChanged(band, freq, psName) }
            }
        }

        override fun notifyPrefabFrequency(frequencyArr: Array<out Frequency>?) {
            if (frequencyArr != null) {
                mainHandler.post {
                    presets.clear()
                    presets.addAll(frequencyArr)
                    listeners.forEach { it.onPresetsUpdated(presets) }
                }
            }
        }

        override fun notifyStereo(stereo: Boolean) {
            mainHandler.post {
                isStereo = stereo
                listeners.forEach { it.onStereoChanged(stereo) }
            }
        }

        override fun notifyRtMessage(str: String?) {
            mainHandler.post {
                currentRtMessage = str ?: ""
                listeners.forEach { it.onRtMessage(currentRtMessage) }
            }
        }

        override fun notifyState(b: Byte) {
            mainHandler.post {
                listeners.forEach { it.onRadioState(b) }
            }
        }

        override fun notifyNearOn(z: Boolean) {}
        override fun notifyStereoOn(z: Boolean) {}
        override fun notifyRDSStateChange() {}
        override fun notifyCurrentPTYType(b: Byte) {}
        override fun notifyPrefabPTYType(b: Byte) {}
        override fun notifyRadioPoint(radioPointArr: Array<out com.nwd.radio.service.data.RadioPoint>?) {}
        override fun notifyCurrentIsTA(z: Boolean) {}
        override fun notifyRdsShowState(z: Boolean) {}
    }

    private val serviceConn = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            Log.i(TAG, "RadioService connected")
            radioFeature = RadioFeature.Stub.asInterface(service)
            try {
                radioFeature?.registCallback(radioCallback)
                val cur = radioFeature?.currentFrequency
                if (cur != null) {
                    currentBand = cur.bandType
                    currentFrequency = cur.frequency
                    currentPsName = cur.psName ?: ""
                }
                val prefabs = radioFeature?.prefabFrequency
                if (prefabs != null && prefabs.isNotEmpty()) {
                    presets.clear()
                    presets.addAll(prefabs)
                }
            } catch (e: Exception) {
                Log.w(TAG, "Error initializing RadioFeature: ${e.message}")
            }
            notifyAllState()
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            Log.i(TAG, "RadioService disconnected")
            radioFeature = null
            isBound = false
        }
    }

    init {
        bindService()
    }

    fun addListener(listener: Listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener)
            listener.onFrequencyChanged(currentBand, currentFrequency, currentPsName)
            listener.onPresetsUpdated(presets)
            listener.onStereoChanged(isStereo)
            listener.onRtMessage(currentRtMessage)
        }
    }

    fun removeListener(listener: Listener) {
        listeners.remove(listener)
    }

    fun bindService() {
        if (isBound) return
        val intent = Intent(RadioConstant.ACTION_RADIO_SERVICE).apply {
            setPackage("com.nwd.radio.service")
        }
        try {
            isBound = appContext.bindService(intent, serviceConn, Context.BIND_AUTO_CREATE)
            if (!isBound) {
                // Fallback to com.nwd.radio
                intent.setPackage("com.nwd.radio")
                isBound = appContext.bindService(intent, serviceConn, Context.BIND_AUTO_CREATE)
            }
        } catch (e: Exception) {
            Log.d(TAG, "RadioService not present in this environment, using simulation: ${e.message}")
        }
    }

    fun changeBand() {
        val rf = radioFeature
        if (rf != null) {
            try {
                rf.changeBand()
                return
            } catch (e: Exception) {
                Log.w(TAG, "changeBand remote failed: ${e.message}")
            }
        }
        // Simulation mode
        currentBand = when (currentBand) {
            RadioConstant.BandType.FM1 -> RadioConstant.BandType.FM2
            RadioConstant.BandType.FM2 -> RadioConstant.BandType.FM3
            RadioConstant.BandType.FM3 -> RadioConstant.BandType.AM1
            RadioConstant.BandType.AM1 -> RadioConstant.BandType.AM2
            else -> RadioConstant.BandType.FM1
        }
        currentFrequency = if (isAmBand(currentBand)) 531 else 10560
        currentPsName = if (isAmBand(currentBand)) "AM" else "RMF FM"
        notifyAllState()
    }

    fun seek(next: Boolean) {
        val rf = radioFeature
        if (rf != null) {
            try {
                rf.seek(next)
                return
            } catch (e: Exception) {
                Log.w(TAG, "seek remote failed: ${e.message}")
            }
        }
        // Simulation mode
        val step = if (isAmBand(currentBand)) 9 else 50
        currentFrequency = if (next) currentFrequency + step else currentFrequency - step
        clampFrequency()
        notifyAllState()
    }

    fun stepTune(next: Boolean) {
        val rf = radioFeature
        if (rf != null) {
            try {
                rf.search(next)
                return
            } catch (e: Exception) {
                Log.w(TAG, "stepTune remote failed: ${e.message}")
            }
        }
        // Simulation mode
        val step = if (isAmBand(currentBand)) 9 else 10
        currentFrequency = if (next) currentFrequency + step else currentFrequency - step
        clampFrequency()
        notifyAllState()
    }

    fun startAms() {
        val rf = radioFeature
        if (rf != null) {
            try {
                rf.AMS()
                return
            } catch (e: Exception) {
                Log.w(TAG, "AMS remote failed: ${e.message}")
            }
        }
    }

    fun selectPreset(index: Int) {
        if (index !in presets.indices) return
        val target = presets[index]
        val rf = radioFeature
        if (rf != null) {
            try {
                rf.setCurrentFrequency(target.frequency, target.bandType, 0)
                return
            } catch (e: Exception) {
                Log.w(TAG, "setCurrentFrequency remote failed: ${e.message}")
            }
        }
        // Simulation mode
        currentBand = target.bandType
        currentFrequency = target.frequency
        currentPsName = target.psName ?: ""
        notifyAllState()
    }

    fun savePreset(index: Int) {
        if (index !in 0..5) return
        val rf = radioFeature
        if (rf != null) {
            try {
                rf.saveCurrentFrequency(index.toByte())
                return
            } catch (e: Exception) {
                Log.w(TAG, "saveCurrentFrequency remote failed: ${e.message}")
            }
        }
        // Simulation mode
        val newFreq = Frequency(currentBand, currentFrequency, currentPsName)
        if (index < presets.size) {
            presets[index] = newFreq
        } else {
            presets.add(newFreq)
        }
        listeners.forEach { it.onPresetsUpdated(presets) }
    }

    private fun clampFrequency() {
        if (isAmBand(currentBand)) {
            if (currentFrequency < 531) currentFrequency = 1602
            if (currentFrequency > 1602) currentFrequency = 531
        } else {
            if (currentFrequency < 8750) currentFrequency = 10800
            if (currentFrequency > 10800) currentFrequency = 8750
        }
    }

    private fun notifyAllState() {
        listeners.forEach {
            it.onFrequencyChanged(currentBand, currentFrequency, currentPsName)
            it.onPresetsUpdated(presets)
            it.onStereoChanged(isStereo)
            it.onRtMessage(currentRtMessage)
        }
    }
}
