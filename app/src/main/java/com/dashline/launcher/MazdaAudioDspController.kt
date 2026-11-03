// Copyright (C) 2026 Mazdash Project
// SPDX-License-Identifier: GPL-3.0-only

package com.dashline.launcher

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.os.RemoteException
import android.provider.Settings
import android.util.Log
import com.nwd.setting.service.AudioSettingCallback
import com.nwd.setting.service.SettingConstant
import com.nwd.setting.service.SettingFeature

/**
 * Controller for the native T3 K2001M MCU Audio DSP and Equalizer hardware.
 *
 * Communicates with `com.nwd.setting.service.SettingService` via AIDL [SettingFeature].
 * Provides:
 * - 2D Sound Stage / Cockpit positioning (Fader & Balance)
 * - 4-Band Equalizer (Treble, Tenor/Mid, Bass, Heavy Bass / Subwoofer)
 * - EQ Preset Profiles (Standard, Pop, Rock, Classical, Jazz, Custom)
 * - Loudness / Equalization contour switch
 */
class MazdaAudioDspController private constructor(private val appContext: Context) {

    companion object {
        private const val TAG = "MazdaDspController"

        const val MAX_STAGE = 14
        const val CENTER_STAGE = 7
        const val MAX_EQ = 14
        const val CENTER_EQ = 7
        const val MAX_SYSTEM_VOLUME = 40

        // EQ Modes matching SettingConstant.AudioType
        const val EQ_CUSTOM = 0
        const val EQ_STANDARD = 1
        const val EQ_POP = 2
        const val EQ_ROCK = 3
        const val EQ_CLASSIC = 4
        const val EQ_JAZZ = 5

        @Volatile
        private var instance: MazdaAudioDspController? = null

        fun getInstance(context: Context): MazdaAudioDspController {
            return instance ?: synchronized(this) {
                instance ?: MazdaAudioDspController(context.applicationContext).also {
                    instance = it
                }
            }
        }
    }

    private var settingFeature: SettingFeature? = null
    private var isBound = false

    // Live DSP values
    var fader: Int = CENTER_STAGE
        private set
    var balance: Int = CENTER_STAGE
        private set
    var bass: Int = CENTER_EQ
        private set
    var tenor: Int = CENTER_EQ
        private set
    var treble: Int = CENTER_EQ
        private set
    var heavyBass: Int = 0
        private set
    var eqMode: Int = EQ_STANDARD
        private set
    var isLoudness: Boolean = false
        private set
    var systemVolume: Int = 20
        private set

    interface Listener {
        fun onStageChanged(fader: Int, balance: Int) {}
        fun onEqualizerChanged(bass: Int, tenor: Int, treble: Int, heavyBass: Int, eqMode: Int) {}
        fun onLoudnessChanged(enabled: Boolean) {}
        fun onSystemVolumeChanged(volume: Int) {}
    }

    private val listeners = mutableSetOf<Listener>()

    fun addListener(listener: Listener) {
        listeners.add(listener)
        listener.onStageChanged(fader, balance)
        listener.onEqualizerChanged(bass, tenor, treble, heavyBass, eqMode)
        listener.onLoudnessChanged(isLoudness)
        listener.onSystemVolumeChanged(systemVolume)
    }

    fun removeListener(listener: Listener) {
        listeners.remove(listener)
    }

    private val audioCallback = object : AudioSettingCallback.Stub() {
        override fun notifyAudioParam(param: Int, value: Int) {
            when (param) {
                SettingConstant.AudioType.AUDIO_PARA_FADER.toInt() -> {
                    fader = value
                    notifyStageChanged()
                }
                SettingConstant.AudioType.AUDIO_PARA_BALANCE.toInt() -> {
                    balance = value
                    notifyStageChanged()
                }
                SettingConstant.AudioType.AUDIO_PARA_BASS.toInt() -> {
                    bass = value
                    notifyEqualizerChanged()
                }
                SettingConstant.AudioType.AUDIO_PARA_TENOR.toInt() -> {
                    tenor = value
                    notifyEqualizerChanged()
                }
                SettingConstant.AudioType.AUDIO_PARA_TREBLE.toInt() -> {
                    treble = value
                    notifyEqualizerChanged()
                }
                SettingConstant.AudioType.AUDIO_PARA_HEAVY_BASS.toInt() -> {
                    heavyBass = value
                    notifyEqualizerChanged()
                }
                SettingConstant.AudioType.AUDIO_PARA_EQ.toInt() -> {
                    eqMode = value
                    notifyEqualizerChanged()
                }
                SettingConstant.AudioType.AUDIO_PARA_EQUAL.toInt() -> {
                    isLoudness = (value != 0)
                    notifyLoudnessChanged()
                }
                SettingConstant.AudioType.AUDIO_PARA_SYSTEM_VOLUME.toInt() -> {
                    systemVolume = value
                    notifySystemVolumeChanged()
                }
            }
        }

        override fun notifyDspAudioParam(param: Int, value: Int) {}
        override fun notifySingleGainParam(param: Int, value: Int) {}
    }

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            Log.d(TAG, "SettingService connected")
            settingFeature = SettingFeature.Stub.asInterface(service)
            isBound = true
            try {
                settingFeature?.registAudioCallback(audioCallback)
            } catch (e: RemoteException) {
                Log.e(TAG, "Failed to register AudioSettingCallback", e)
            }
            readStoredValues()
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            Log.d(TAG, "SettingService disconnected")
            settingFeature = null
            isBound = false
        }
    }

    fun init() {
        readStoredValues()
        bindService()
    }

    fun release() {
        if (isBound) {
            try {
                settingFeature?.unRegistAudioCallback(audioCallback)
                appContext.unbindService(serviceConnection)
            } catch (e: Exception) {
                // ignore
            }
            isBound = false
            settingFeature = null
        }
    }

    private fun bindService() {
        val intent = Intent(SettingConstant.ACTION_SETTING_SERVICE).apply {
            component = ComponentName("com.nwd.setting.service", "com.nwd.setting.service.SettingService")
            setPackage("com.nwd.setting.service")
        }
        try {
            appContext.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to bind SettingService", e)
        }
    }

    private fun readStoredValues() {
        val resolver = appContext.contentResolver
        fader = Settings.System.getInt(resolver, "mcu_fader", CENTER_STAGE)
        balance = Settings.System.getInt(resolver, "mcu_balance", CENTER_STAGE)
        bass = Settings.System.getInt(resolver, "mcu_bass", CENTER_EQ)
        tenor = Settings.System.getInt(resolver, "mcu_tenor", CENTER_EQ)
        treble = Settings.System.getInt(resolver, "mcu_treble", CENTER_EQ)
        heavyBass = Settings.System.getInt(resolver, "mcu_heavy_bass", 0)
        eqMode = Settings.System.getInt(resolver, "mcu_eq", EQ_STANDARD)
        isLoudness = Settings.System.getInt(resolver, "mcu_state_equal", 0) != 0
        systemVolume = Settings.System.getInt(resolver, "mcu_system_volume", 20)

        notifyStageChanged()
        notifyEqualizerChanged()
        notifyLoudnessChanged()
        notifySystemVolumeChanged()
    }

    // ---- Controls ----

    fun setSystemVolume(value: Int) {
        val clamped = value.coerceIn(0, MAX_SYSTEM_VOLUME)
        systemVolume = clamped
        notifySystemVolumeChanged()
        sendAudioParam(SettingConstant.AudioType.AUDIO_PARA_SYSTEM_VOLUME.toInt(), clamped)
        try {
            Settings.System.putInt(appContext.contentResolver, "mcu_system_volume", clamped)
        } catch (e: Exception) {
            // ignore
        }
        syncAndroidStreamVolume(clamped)
    }

    private fun syncAndroidStreamVolume(mcuVol: Int) {
        try {
            val am = appContext.getSystemService(Context.AUDIO_SERVICE) as? android.media.AudioManager ?: return
            val stream = android.media.AudioManager.STREAM_MUSIC
            val max = am.getStreamMaxVolume(stream)
            val streamTarget = Math.round((mcuVol.toFloat() / MAX_SYSTEM_VOLUME.toFloat()) * max)
            am.setStreamVolume(stream, streamTarget, 0)
        } catch (e: Exception) {
            // ignore
        }
    }

    fun setFader(value: Int) {
        val clamped = value.coerceIn(0, MAX_STAGE)
        fader = clamped
        notifyStageChanged()
        sendAudioParam(SettingConstant.AudioType.AUDIO_PARA_FADER.toInt(), clamped)
    }

    fun setBalance(value: Int) {
        val clamped = value.coerceIn(0, MAX_STAGE)
        balance = clamped
        notifyStageChanged()
        sendAudioParam(SettingConstant.AudioType.AUDIO_PARA_BALANCE.toInt(), clamped)
    }

    fun setStageCenter() {
        setFader(CENTER_STAGE)
        setBalance(CENTER_STAGE)
    }

    fun setStageDriver() {
        setFader(4)
        setBalance(4)
    }

    fun setStageCopilot() {
        setFader(4)
        setBalance(10)
    }

    fun setStageRear() {
        setFader(10)
        setBalance(CENTER_STAGE)
    }

    fun setBass(value: Int) {
        val clamped = value.coerceIn(0, MAX_EQ)
        bass = clamped
        notifyEqualizerChanged()
        sendAudioParam(SettingConstant.AudioType.AUDIO_PARA_BASS.toInt(), clamped)
    }

    fun setTenor(value: Int) {
        val clamped = value.coerceIn(0, MAX_EQ)
        tenor = clamped
        notifyEqualizerChanged()
        sendAudioParam(SettingConstant.AudioType.AUDIO_PARA_TENOR.toInt(), clamped)
    }

    fun setTreble(value: Int) {
        val clamped = value.coerceIn(0, MAX_EQ)
        treble = clamped
        notifyEqualizerChanged()
        sendAudioParam(SettingConstant.AudioType.AUDIO_PARA_TREBLE.toInt(), clamped)
    }

    fun setHeavyBass(value: Int) {
        val clamped = value.coerceIn(0, MAX_EQ)
        heavyBass = clamped
        notifyEqualizerChanged()
        sendAudioParam(SettingConstant.AudioType.AUDIO_PARA_HEAVY_BASS.toInt(), clamped)
    }

    fun setEqMode(mode: Int) {
        eqMode = mode
        notifyEqualizerChanged()
        sendAudioParam(SettingConstant.AudioType.AUDIO_PARA_EQ.toInt(), mode)
    }

    fun setLoudness(enabled: Boolean) {
        isLoudness = enabled
        notifyLoudnessChanged()
        sendAudioParam(SettingConstant.AudioType.AUDIO_PARA_EQUAL.toInt(), if (enabled) 1 else 0)
    }

    private fun sendAudioParam(param: Int, value: Int) {
        try {
            settingFeature?.setAudioParam(param, value)
        } catch (e: RemoteException) {
            Log.e(TAG, "Failed setAudioParam($param, $value)", e)
        }
    }

    private fun notifyStageChanged() {
        listeners.forEach { it.onStageChanged(fader, balance) }
    }

    private fun notifyEqualizerChanged() {
        listeners.forEach { it.onEqualizerChanged(bass, tenor, treble, heavyBass, eqMode) }
    }

    private fun notifyLoudnessChanged() {
        listeners.forEach { it.onLoudnessChanged(isLoudness) }
    }

    private fun notifySystemVolumeChanged() {
        listeners.forEach { it.onSystemVolumeChanged(systemVolume) }
    }
}
