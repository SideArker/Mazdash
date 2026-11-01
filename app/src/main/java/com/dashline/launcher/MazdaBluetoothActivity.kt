// Copyright (C) 2026 Mazdash Project
// SPDX-License-Identifier: GPL-3.0-only

package com.dashline.launcher

import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.bt.BTDevice
import com.dashline.launcher.databinding.ActivityMazdaBluetoothBinding

class MazdaBluetoothActivity : BaseActivity() {

    private lateinit var binding: ActivityMazdaBluetoothBinding
    private lateinit var btController: MazdaBluetoothController
    private lateinit var adapter: MazdaBtDeviceAdapter

    private val btListener = object : MazdaBluetoothController.Listener {
        override fun onConnectionChanged(connected: Boolean, deviceName: String?, deviceAddress: String?) {
            runOnUiThread {
                updateDeviceList()
                updatePairingInstructions()
            }
        }

        override fun onDeviceInfoChanged(name: String, pin: String) {
            runOnUiThread {
                binding.textLocalBtName.text = name
                binding.textBtPin.text = pin
                updatePairingInstructions()
            }
        }

        override fun onPairedDevicesChanged(devices: List<BTDevice>) {
            runOnUiThread {
                updateDeviceList()
            }
        }

        override fun onDiscoveryChanged(isDiscovering: Boolean, discovered: List<BTDevice>) {
            runOnUiThread {
                binding.btnStartDiscovery.text = if (isDiscovering) {
                    "Wyszukiwanie... (Znaleziono: ${discovered.size})"
                } else {
                    "Szukaj urządzeń w pobliżu"
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMazdaBluetoothBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.header.pageTitle.setText(R.string.mazda_bt_title)
        bindPageNav(binding.header.navBack, binding.header.navHome)

        btController = MazdaBluetoothController.getInstance(this)

        adapter = MazdaBtDeviceAdapter(
            onConnectToggle = { device, isConnected ->
                if (isConnected) {
                    btController.disconnectDevice()
                } else {
                    btController.connectDevice(device)
                    Toast.makeText(this, "Łączenie z ${device.name}...", Toast.LENGTH_SHORT).show()
                }
            },
            onDelete = { position, _ ->
                btController.deletePairedDevice(position)
            }
        )

        binding.recyclerPairedDevices.layoutManager = LinearLayoutManager(this)
        binding.recyclerPairedDevices.adapter = adapter

        // Setup Name & PIN change
        binding.btnChangeName.setOnClickListener { showChangeNameDialog() }
        binding.btnChangePin.setOnClickListener { showChangePinDialog() }

        // Start Pairing Mode
        binding.btnStartPairing.setOnClickListener {
            val ok = btController.enablePairingMode()
            if (ok) {
                Toast.makeText(this, R.string.bt_pairing_mode_active, Toast.LENGTH_SHORT).show()
                updatePairingInstructions()
            }
        }

        // Discovery
        binding.btnStartDiscovery.setOnClickListener {
            if (btController.isDiscovering) {
                btController.stopDiscovery()
            } else {
                btController.startDiscovery()
                Toast.makeText(this, "Wyszukiwanie telefonów...", Toast.LENGTH_SHORT).show()
            }
        }

        // Refresh Paired Devices
        binding.btnRefreshDevices.setOnClickListener {
            btController.refreshPairedDevices()
        }
    }

    override fun onStart() {
        super.onStart()
        btController.addListener(btListener)
        btController.refreshPairedDevices()
        updatePairingInstructions()
    }

    override fun onStop() {
        super.onStop()
        btController.removeListener(btListener)
    }

    private fun updateDeviceList() {
        val list = btController.pairedDevices
        adapter.submitList(list, btController.connectedDeviceAddress, btController.isConnected)
        binding.textEmptyPaired.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
    }

    private fun updatePairingInstructions() {
        val name = btController.localDeviceName.ifEmpty { "Mazda" }
        val pin = btController.pinCode.ifEmpty { "0000" }
        binding.textPairingInstructions.text = getString(R.string.bt_pairing_instructions, name, pin)
    }

    private fun showChangeNameDialog() {
        val input = EditText(this).apply {
            setText(btController.localDeviceName)
            setSelection(text.length)
        }
        AlertDialog.Builder(this)
            .setTitle(R.string.bt_change_name)
            .setView(input)
            .setPositiveButton(android.R.string.ok) { _, _ ->
                val newName = input.text.toString().trim()
                if (newName.isNotEmpty()) {
                    btController.setLocalDeviceName(newName)
                }
            }
            .setNegativeButton(android.R.string.cancel, null)
            .show()
    }

    private fun showChangePinDialog() {
        val input = EditText(this).apply {
            inputType = InputType.TYPE_CLASS_NUMBER
            setText(btController.pinCode)
            setSelection(text.length)
        }
        AlertDialog.Builder(this)
            .setTitle(R.string.bt_change_pin)
            .setView(input)
            .setPositiveButton(android.R.string.ok) { _, _ ->
                val newPin = input.text.toString().trim()
                if (newPin.length >= 4) {
                    btController.setPinCode(newPin)
                } else {
                    Toast.makeText(this, "PIN musi mieć co najmniej 4 cyfry", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton(android.R.string.cancel, null)
            .show()
    }
}
