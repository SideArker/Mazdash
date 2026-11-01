// Copyright (C) 2026 Mazdash Project
// SPDX-License-Identifier: GPL-3.0-only

package com.dashline.launcher

import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.ServiceConnection
import android.os.IBinder
import android.os.RemoteException
import android.util.Log
import com.bt.BTConstant
import com.bt.BTDefine
import com.bt.BTDevice
import com.bt.BTFeature

/**
 * Controller for the native T3 K2001M Bluetooth stack (com.bt.bc03.BTService).
 *
 * Provides:
 * - Device naming & PIN management (e.g. "Mazda", PIN "0000")
 * - One-touch discoverable pairing mode
 * - Paired device enumeration, connection, and deletion
 * - Real-time A2DP/AVRCP ID3 music metadata and playback controls
 */
class MazdaBluetoothController private constructor(private val appContext: Context) {

    companion object {
        private const val TAG = "MazdaBTController"

        @Volatile
        private var instance: MazdaBluetoothController? = null

        fun getInstance(context: Context): MazdaBluetoothController {
            return instance ?: synchronized(this) {
                instance ?: MazdaBluetoothController(context.applicationContext).also {
                    instance = it
                }
            }
        }
    }

    private var btFeature: BTFeature? = null
    private var isBound = false
    private val prefs = Prefs(appContext)

    // Current state
    var isConnected: Boolean = false
        private set
    var connectedDeviceName: String? = null
        private set
    var connectedDeviceAddress: String? = null
        private set

    var isMusicPlaying: Boolean = false
        private set
    var trackTitle: String = ""
        private set
    var trackArtist: String = ""
        private set

    var localDeviceName: String = prefs.bluetoothDeviceName
        private set
    var pinCode: String = prefs.bluetoothPin
        private set

    val pairedDevices = mutableListOf<BTDevice>()
    val discoveredDevices = mutableListOf<BTDevice>()
    var isDiscovering: Boolean = false
        private set

    // Listeners
    interface Listener {
        fun onConnectionChanged(connected: Boolean, deviceName: String?, deviceAddress: String?) {}
        fun onPlaybackChanged(isPlaying: Boolean, title: String, artist: String) {}
        fun onDeviceInfoChanged(name: String, pin: String) {}
        fun onPairedDevicesChanged(devices: List<BTDevice>) {}
        fun onDiscoveryChanged(isDiscovering: Boolean, discovered: List<BTDevice>) {}
    }

    private val listeners = mutableSetOf<Listener>()

    fun addListener(listener: Listener) {
        listeners.add(listener)
        // Emit current state immediately
        listener.onConnectionChanged(isConnected, connectedDeviceName, connectedDeviceAddress)
        listener.onPlaybackChanged(isMusicPlaying, trackTitle, trackArtist)
        listener.onDeviceInfoChanged(localDeviceName, pinCode)
        listener.onPairedDevicesChanged(pairedDevices.toList())
    }

    fun removeListener(listener: Listener) {
        listeners.remove(listener)
    }

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            Log.d(TAG, "BTService connected successfully")
            btFeature = BTFeature.Stub.asInterface(service)
            isBound = true
            queryInitialState()
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            Log.d(TAG, "BTService disconnected")
            btFeature = null
            isBound = false
        }
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val action = intent?.action ?: return
            when (action) {
                BTDefine.ACTION_BT_CONNECTION_CHANGE -> {
                    val status = intent.getIntExtra(BTDefine.EXTRA_BT_CONNECTION_EVENT, 0)
                    val name = intent.getStringExtra(BTDefine.EXTRA_BT_DEVICE_NAME)
                    val addr = intent.getStringExtra(BTDefine.EXTRA_BT_DEVICE_ADDRESS)
                    isConnected = (status == 1)
                    if (isConnected) {
                        connectedDeviceName = name ?: connectedDeviceName
                        connectedDeviceAddress = addr ?: connectedDeviceAddress
                        queryMusicId3()
                    } else {
                        connectedDeviceName = null
                        connectedDeviceAddress = null
                        isMusicPlaying = false
                        trackTitle = ""
                        trackArtist = ""
                    }
                    notifyConnectionChanged()
                    notifyPlaybackChanged()
                    refreshPairedDevices()
                }

                BTDefine.ACTION_AVRCP_MUSIC_ID3 -> {
                    val title = intent.getStringExtra(BTDefine.EXTRA_AVRCP_ID3_TITLE).orEmpty()
                    val artist = intent.getStringExtra(BTDefine.EXTRA_AVRCP_ID3_ARTIST).orEmpty()
                    if (title.isNotEmpty() || artist.isNotEmpty()) {
                        trackTitle = title
                        trackArtist = artist
                        notifyPlaybackChanged()
                    }
                }

                BTDefine.ACTION_BT_MUSIC_PLAY -> {
                    isMusicPlaying = true
                    notifyPlaybackChanged()
                }

                BTDefine.ACTION_BT_MUSIC_PAUSE -> {
                    isMusicPlaying = false
                    notifyPlaybackChanged()
                }

                BTDefine.ACTION_GET_PAIR_DEVICE_RECORD -> {
                    val device = intent.getParcelableExtra<BTDevice>(BTDefine.EXTRA_PAIR_DEVICE)
                    if (device != null && !device.name.isNullOrBlank() && device.name != "GPS") {
                        if (!pairedDevices.any { it.address.equals(device.address, ignoreCase = true) }) {
                            pairedDevices.add(device)
                            notifyPairedDevicesChanged()
                        }
                    }
                }

                BTDefine.ACTION_BT_NAME_CHANGE -> {
                    queryLocalDeviceName()
                }

                BTDefine.ACTION_BT_PINCODE_CHANGE -> {
                    queryPinCode()
                }

                BTDefine.ACTION_BT_DEVICE_FOUND -> {
                    val addr = intent.getStringExtra(BTDefine.EXTRA_BT_DEVICE_ADDRESS)
                    val name = intent.getStringExtra(BTDefine.EXTRA_BT_DEVICE_NAME)
                    if (!addr.isNullOrBlank()) {
                        val device = BTDevice(addr, name ?: "Nieznane urządzenie")
                        if (!discoveredDevices.any { it.address.equals(addr, ignoreCase = true) }) {
                            discoveredDevices.add(device)
                            notifyDiscoveryChanged()
                        }
                    }
                }

                BTDefine.ACTION_BT_DISCOVER_FINISH -> {
                    isDiscovering = false
                    notifyDiscoveryChanged()
                }
            }
        }
    }

    fun init() {
        bindService()
        registerBroadcastReceiver()
    }

    fun release() {
        try {
            appContext.unregisterReceiver(receiver)
        } catch (e: Exception) {
            // ignore
        }
        if (isBound) {
            try {
                appContext.unbindService(serviceConnection)
            } catch (e: Exception) {
                // ignore
            }
            isBound = false
        }
    }

    private fun bindService() {
        val intent = Intent().apply {
            component = ComponentName("com.bt.bc03", "com.bt.bc03.BTService")
            setPackage("com.bt.bc03")
        }
        try {
            appContext.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to bind BTService", e)
        }
    }

    private fun registerBroadcastReceiver() {
        val filter = IntentFilter().apply {
            addAction(BTDefine.ACTION_BT_CONNECTION_CHANGE)
            addAction(BTDefine.ACTION_AVRCP_MUSIC_ID3)
            addAction(BTDefine.ACTION_BT_MUSIC_PLAY)
            addAction(BTDefine.ACTION_BT_MUSIC_PAUSE)
            addAction(BTDefine.ACTION_GET_PAIR_DEVICE_RECORD)
            addAction(BTDefine.ACTION_BT_NAME_CHANGE)
            addAction(BTDefine.ACTION_BT_PINCODE_CHANGE)
            addAction(BTDefine.ACTION_BT_DEVICE_FOUND)
            addAction(BTDefine.ACTION_BT_DISCOVER_FINISH)
            addAction(BTDefine.ACTION_A2DP_ESTABLISHED)
            addAction(BTDefine.ACTION_A2DP_RELEASE)
        }
        try {
            appContext.registerReceiver(receiver, filter)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to register BT receiver", e)
        }
    }

    private fun queryInitialState() {
        try {
            val feature = btFeature ?: return
            feature.setPairable(true)
            val currentDevice = feature.connectDevice
            if (currentDevice != null && feature.isConnectDevice) {
                isConnected = true
                connectedDeviceName = currentDevice.name
                connectedDeviceAddress = currentDevice.address
            } else {
                isConnected = false
            }

            isMusicPlaying = feature.isBTMusicPlaying
            val name = feature.localDeviceName
            if (!name.isNullOrBlank()) {
                localDeviceName = name
            } else if (prefs.bluetoothDeviceName.isNotEmpty()) {
                feature.setLocalDeviceName(prefs.bluetoothDeviceName)
            }

            val pin = feature.pinCode
            if (!pin.isNullOrBlank()) {
                pinCode = pin
            }

            notifyConnectionChanged()
            notifyPlaybackChanged()
            notifyDeviceInfoChanged()
            refreshPairedDevices()
            feature.queryA2dpId3Info()
        } catch (e: Exception) {
            Log.e(TAG, "Error querying initial BT state", e)
        }
    }

    fun queryLocalDeviceName() {
        try {
            val name = btFeature?.localDeviceName
            if (!name.isNullOrBlank()) {
                localDeviceName = name
                prefs.bluetoothDeviceName = name
                notifyDeviceInfoChanged()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error getting device name", e)
        }
    }

    fun queryPinCode() {
        try {
            val pin = btFeature?.pinCode
            if (!pin.isNullOrBlank()) {
                pinCode = pin
                prefs.bluetoothPin = pin
                notifyDeviceInfoChanged()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error getting pin code", e)
        }
    }

    fun queryMusicId3() {
        try {
            btFeature?.queryA2dpId3Info()
        } catch (e: Exception) {
            Log.e(TAG, "Error querying ID3 info", e)
        }
    }

    // ---- User Actions ----

    /** Set broadcast device name (e.g. "Mazda"). */
    fun setLocalDeviceName(name: String): Boolean {
        val trimmed = name.trim()
        if (trimmed.isEmpty()) return false
        localDeviceName = trimmed
        prefs.bluetoothDeviceName = trimmed
        notifyDeviceInfoChanged()
        return try {
            btFeature?.setLocalDeviceName(trimmed) ?: false
        } catch (e: RemoteException) {
            Log.e(TAG, "Failed to set device name", e)
            false
        }
    }

    /** Set pairing PIN code (e.g. "0000"). */
    fun setPinCode(pin: String): Boolean {
        val trimmed = pin.trim()
        if (trimmed.length < 4) return false
        pinCode = trimmed
        prefs.bluetoothPin = trimmed
        notifyDeviceInfoChanged()
        return try {
            btFeature?.setPinCode(trimmed)
            true
        } catch (e: RemoteException) {
            Log.e(TAG, "Failed to set PIN code", e)
            false
        }
    }

    /** Put radio into pairing mode (visible + pairable). */
    fun enablePairingMode(): Boolean {
        return try {
            val feature = btFeature ?: return false
            feature.setPairable(true)
            feature.setDiscoverable(true)
            true
        } catch (e: RemoteException) {
            Log.e(TAG, "Failed to enable pairing mode", e)
            false
        }
    }

    /** Refresh list of paired devices from the BC03 stack. */
    fun refreshPairedDevices() {
        pairedDevices.clear()
        try {
            btFeature?.pairDeviceRecord_BC
        } catch (e: RemoteException) {
            Log.e(TAG, "Failed to get pair device records", e)
        }
    }

    /** Connect to a paired device. */
    fun connectDevice(device: BTDevice): Boolean {
        return try {
            btFeature?.connectDevice_BC(device) ?: false
        } catch (e: RemoteException) {
            Log.e(TAG, "Failed to connect device", e)
            false
        }
    }

    /** Disconnect current Bluetooth device. */
    fun disconnectDevice(): Boolean {
        return try {
            btFeature?.disConnectDevice_BC() ?: false
        } catch (e: RemoteException) {
            Log.e(TAG, "Failed to disconnect device", e)
            false
        }
    }

    /** Delete paired device (1-based index in BC03 stack). */
    fun deletePairedDevice(index: Int): Boolean {
        if (index < 0 || index >= pairedDevices.size) return false
        pairedDevices.removeAt(index)
        notifyPairedDevicesChanged()
        return try {
            btFeature?.deletePairDeviceRecord(index + 1) ?: false
        } catch (e: RemoteException) {
            Log.e(TAG, "Failed to delete pair record", e)
            false
        }
    }

    /** Start searching for nearby discoverable phones. */
    fun startDiscovery(): Boolean {
        discoveredDevices.clear()
        isDiscovering = true
        notifyDiscoveryChanged()
        return try {
            btFeature?.startSearchDevice_BC() ?: false
        } catch (e: RemoteException) {
            Log.e(TAG, "Failed to start discovery", e)
            isDiscovering = false
            notifyDiscoveryChanged()
            false
        }
    }

    /** Stop ongoing discovery. */
    fun stopDiscovery(): Boolean {
        isDiscovering = false
        notifyDiscoveryChanged()
        return try {
            btFeature?.stopSearchDevice_BC() ?: false
        } catch (e: RemoteException) {
            Log.e(TAG, "Failed to stop discovery", e)
            false
        }
    }

    /** Play/Pause toggle (cmd 0). */
    fun playPause() {
        try {
            btFeature?.playControl(0)
        } catch (e: RemoteException) {
            Log.e(TAG, "Failed playControl(0)", e)
        }
    }

    /** Next track (cmd 2). */
    fun nextTrack() {
        try {
            btFeature?.playControl(2)
        } catch (e: RemoteException) {
            Log.e(TAG, "Failed playControl(2)", e)
        }
    }

    /** Previous track (cmd 3). */
    fun previousTrack() {
        try {
            btFeature?.playControl(3)
        } catch (e: RemoteException) {
            Log.e(TAG, "Failed playControl(3)", e)
        }
    }

    private fun notifyConnectionChanged() {
        listeners.forEach { it.onConnectionChanged(isConnected, connectedDeviceName, connectedDeviceAddress) }
    }

    private fun notifyPlaybackChanged() {
        listeners.forEach { it.onPlaybackChanged(isMusicPlaying, trackTitle, trackArtist) }
    }

    private fun notifyDeviceInfoChanged() {
        listeners.forEach { it.onDeviceInfoChanged(localDeviceName, pinCode) }
    }

    private fun notifyPairedDevicesChanged() {
        listeners.forEach { it.onPairedDevicesChanged(pairedDevices.toList()) }
    }

    private fun notifyDiscoveryChanged() {
        listeners.forEach { it.onDiscoveryChanged(isDiscovering, discoveredDevices.toList()) }
    }
}
