// Copyright (C) 2026 Mazdash Project
// SPDX-License-Identifier: GPL-3.0-only

package com.dashline.launcher

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Handler
import android.os.Looper
import android.os.RemoteException
import android.util.Log
import com.bt.BTDefine
import com.bt.BTFeature
import com.bt.BTPIMCallback
import java.util.concurrent.CopyOnWriteArrayList

/**
 * Controller for the native T3 K2001M Bluetooth Telephony stack (HFP/PBAP).
 *
 * Provides:
 * - Direct dialing and DTMF signaling via [BTFeature.dial_BC]
 * - Answering & hanging up calls ([BTFeature.answerCall], [BTFeature.cancelCall])
 * - Audio routing switch (Car hands-free speakers vs Phone handset)
 * - Phonebook and Call Log synchronization via [BTPIMCallback]
 * - Full simulated preview mode for offline phone testing
 */
class MazdaPhoneController private constructor(private val appContext: Context) {

    companion object {
        private const val TAG = "MazdaPhoneController"

        @Volatile
        private var instance: MazdaPhoneController? = null

        fun getInstance(context: Context): MazdaPhoneController {
            return instance ?: synchronized(this) {
                instance ?: MazdaPhoneController(context.applicationContext).also {
                    instance = it
                }
            }
        }
    }

    enum class CallState {
        IDLE,
        DIALING,
        INCOMING,
        ACTIVE
    }

    data class ContactItem(val name: String, val number: String)
    data class CallLogItem(val type: Int, val name: String, val number: String, val date: String)

    interface CallListener {
        fun onCallStateChanged(state: CallState, number: String, name: String?)
        fun onCallDurationTick(seconds: Int)
        fun onContactsUpdated(contacts: List<ContactItem>)
        fun onCallLogsUpdated(logs: List<CallLogItem>)
        fun onAudioChannelChanged(isCarSpeaker: Boolean)
    }

    private val mainHandler = Handler(Looper.getMainLooper())
    private val listeners = CopyOnWriteArrayList<CallListener>()
    private val btController = MazdaBluetoothController.getInstance(appContext)

    var callState = CallState.IDLE
        private set
    var currentCallNumber: String = ""
        private set
    var currentCallName: String? = null
        private set
    var isVoiceAtCar: Boolean = true
        private set
    var isMicMuted: Boolean = false
        private set
    var callDurationSeconds: Int = 0
        private set

    val contacts = ArrayList<ContactItem>().apply {
        add(ContactItem("Mechanik Mazda", "+48 600 100 200"))
        add(ContactItem("Dom", "+48 500 200 300"))
        add(ContactItem("Jan Kowalski", "+48 601 234 567"))
        add(ContactItem("Biuro", "+48 22 123 45 67"))
        add(ContactItem("Pomoc Drogowa", "+48 800 123 456"))
    }

    val callLogs = ArrayList<CallLogItem>().apply {
        add(CallLogItem(1, "Dom", "+48 500 200 300", "Dzisiaj 14:20"))
        add(CallLogItem(2, "Mechanik Mazda", "+48 600 100 200", "Dzisiaj 11:05"))
        add(CallLogItem(3, "Jan Kowalski", "+48 601 234 567", "Wczoraj 18:45"))
    }

    private var durationRunnable: Runnable? = null

    private val pimCallback = object : BTPIMCallback.Stub() {
        override fun onGetContact(name: String?, number: String?) {
            if (!name.isNullOrEmpty() && !number.isNullOrEmpty()) {
                mainHandler.post {
                    contacts.add(ContactItem(name, number))
                    listeners.forEach { it.onContactsUpdated(contacts) }
                }
            }
        }

        override fun onGetCallLog(type: Int, name: String?, number: String?, date: String?) {
            if (!number.isNullOrEmpty()) {
                mainHandler.post {
                    callLogs.add(CallLogItem(type, name ?: number, number, date ?: ""))
                    listeners.forEach { it.onCallLogsUpdated(callLogs) }
                }
            }
        }

        override fun onGetSMS(str: String?, str2: String?, i: Int) {}
    }

    private val callReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val action = intent?.action ?: return
            Log.d(TAG, "onReceive call broadcast: $action")
            when (action) {
                BTDefine.ACTION_BT_INCOMING_CALL -> {
                    val num = intent.getStringExtra(BTDefine.EXTRA_PHONE_NUMBER) ?: ""
                    val name = intent.getStringExtra(BTDefine.EXTRA_PHONE_NAME)
                    setCallState(CallState.INCOMING, num, name)
                }
                BTDefine.ACTION_BT_OUTGOING_CALL -> {
                    val num = intent.getStringExtra(BTDefine.EXTRA_PHONE_NUMBER) ?: ""
                    val name = intent.getStringExtra(BTDefine.EXTRA_PHONE_NAME)
                    setCallState(CallState.DIALING, num, name)
                }
                BTDefine.ACTION_BT_BEGIN_CALL_ONLINE -> {
                    setCallState(CallState.ACTIVE, currentCallNumber, currentCallName)
                    startDurationTimer()
                }
                BTDefine.ACTION_BT_END_CALL -> {
                    setCallState(CallState.IDLE, "", null)
                    stopDurationTimer()
                }
                BTDefine.ACTION_BT_VOICE_AT_CAR -> {
                    isVoiceAtCar = true
                    listeners.forEach { it.onAudioChannelChanged(true) }
                }
                BTDefine.ACTION_BT_VOICE_AT_PHONE -> {
                    isVoiceAtCar = false
                    listeners.forEach { it.onAudioChannelChanged(false) }
                }
                BTDefine.ACTION_BT_MIC_MUTE_STATE_CHANGE -> {
                    isMicMuted = intent.getBooleanExtra(BTDefine.EXTRA_BT_MIC_MUTE_STATE, false)
                }
            }
        }
    }

    init {
        val filter = IntentFilter().apply {
            addAction(BTDefine.ACTION_BT_INCOMING_CALL)
            addAction(BTDefine.ACTION_BT_OUTGOING_CALL)
            addAction(BTDefine.ACTION_BT_BEGIN_CALL_ONLINE)
            addAction(BTDefine.ACTION_BT_END_CALL)
            addAction(BTDefine.ACTION_BT_VOICE_AT_CAR)
            addAction(BTDefine.ACTION_BT_VOICE_AT_PHONE)
            addAction(BTDefine.ACTION_BT_MIC_MUTE_STATE_CHANGE)
        }
        try {
            appContext.registerReceiver(callReceiver, filter)
        } catch (e: Exception) {
            Log.w(TAG, "Failed registering call receiver: ${e.message}")
        }
    }

    fun addListener(listener: CallListener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener)
            listener.onCallStateChanged(callState, currentCallNumber, currentCallName)
            listener.onCallDurationTick(callDurationSeconds)
            listener.onContactsUpdated(contacts)
            listener.onCallLogsUpdated(callLogs)
            listener.onAudioChannelChanged(isVoiceAtCar)
        }
    }

    fun removeListener(listener: CallListener) {
        listeners.remove(listener)
    }

    fun dial(number: String) {
        if (number.isEmpty()) return
        Log.i(TAG, "dial: $number")
        val feature = getBtFeature()
        if (feature != null) {
            try {
                feature.dial_BC(number)
                return
            } catch (e: Exception) {
                Log.w(TAG, "dial_BC remote failed: ${e.message}")
            }
        }

        // Simulation mode for testing
        val contact = contacts.find { it.number.replace(" ", "") == number.replace(" ", "") }
        setCallState(CallState.DIALING, number, contact?.name)

        // Simulate pickup after 2 seconds
        mainHandler.postDelayed({
            if (callState == CallState.DIALING) {
                setCallState(CallState.ACTIVE, currentCallNumber, currentCallName)
                startDurationTimer()
            }
        }, 2000)
    }

    fun answerCall() {
        val feature = getBtFeature()
        if (feature != null) {
            try {
                feature.answerCall()
                return
            } catch (e: Exception) {
                Log.w(TAG, "answerCall remote failed: ${e.message}")
            }
        }
        // Simulation mode
        setCallState(CallState.ACTIVE, currentCallNumber, currentCallName)
        startDurationTimer()
    }

    fun hangUp() {
        val feature = getBtFeature()
        if (feature != null) {
            try {
                feature.cancelCall()
                return
            } catch (e: Exception) {
                Log.w(TAG, "cancelCall remote failed: ${e.message}")
            }
        }
        // Simulation mode
        setCallState(CallState.IDLE, "", null)
        stopDurationTimer()
    }

    fun toggleAudioRoute() {
        val feature = getBtFeature()
        if (feature != null) {
            try {
                val newChannel = feature.changeSoundChannel()
                isVoiceAtCar = (newChannel == 1)
                listeners.forEach { it.onAudioChannelChanged(isVoiceAtCar) }
                return
            } catch (e: Exception) {
                Log.w(TAG, "changeSoundChannel remote failed: ${e.message}")
            }
        }
        isVoiceAtCar = !isVoiceAtCar
        listeners.forEach { it.onAudioChannelChanged(isVoiceAtCar) }
    }

    fun syncPhonebook() {
        val feature = getBtFeature() ?: return
        try {
            contacts.clear()
            callLogs.clear()
            feature.registPIMCallback(pimCallback)
            feature.getContact_CB()
            feature.getCallLog_CB()
        } catch (e: Exception) {
            Log.w(TAG, "syncPhonebook failed: ${e.message}")
        }
    }

    private fun setCallState(state: CallState, number: String, name: String?) {
        callState = state
        currentCallNumber = number
        currentCallName = name
        listeners.forEach { it.onCallStateChanged(state, number, name) }
    }

    private fun startDurationTimer() {
        stopDurationTimer()
        callDurationSeconds = 0
        durationRunnable = object : Runnable {
            override fun run() {
                callDurationSeconds++
                listeners.forEach { it.onCallDurationTick(callDurationSeconds) }
                mainHandler.postDelayed(this, 1000)
            }
        }
        mainHandler.postDelayed(durationRunnable!!, 1000)
    }

    private fun stopDurationTimer() {
        durationRunnable?.let { mainHandler.removeCallbacks(it) }
        durationRunnable = null
        callDurationSeconds = 0
    }

    private fun getBtFeature(): BTFeature? {
        return btController.getBtFeature()
    }
}
