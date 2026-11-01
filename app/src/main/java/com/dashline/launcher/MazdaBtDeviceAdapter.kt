// Copyright (C) 2026 Mazdash Project
// SPDX-License-Identifier: GPL-3.0-only

package com.dashline.launcher

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bt.BTDevice
import com.dashline.launcher.databinding.ItemMazdaBtDeviceBinding

class MazdaBtDeviceAdapter(
    private val onConnectToggle: (device: BTDevice, isConnected: Boolean) -> Unit,
    private val onDelete: (position: Int, device: BTDevice) -> Unit
) : RecyclerView.Adapter<MazdaBtDeviceAdapter.ViewHolder>() {

    private val devices = mutableListOf<BTDevice>()
    private var connectedAddress: String? = null
    private var isConnected: Boolean = false

    fun submitList(list: List<BTDevice>, activeAddress: String?, activeState: Boolean) {
        devices.clear()
        devices.addAll(list)
        connectedAddress = activeAddress
        isConnected = activeState
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMazdaBtDeviceBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(devices[position], position)
    }

    override fun getItemCount(): Int = devices.size

    inner class ViewHolder(private val binding: ItemMazdaBtDeviceBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(device: BTDevice, position: Int) {
            binding.textDeviceName.text = device.name ?: "Urządzenie Bluetooth"
            binding.textDeviceAddress.text = device.address ?: ""

            val isThisConnected = isConnected &&
                !connectedAddress.isNullOrEmpty() &&
                device.address.equals(connectedAddress, ignoreCase = true)

            val context = binding.root.context
            if (isThisConnected) {
                binding.textDeviceStatus.text = context.getString(R.string.bt_connected)
                binding.textDeviceStatus.setTextColor(androidx.core.content.ContextCompat.getColor(context, R.color.mz_status_connected))
                binding.btnConnectToggle.text = context.getString(R.string.bt_disconnect)
            } else {
                binding.textDeviceStatus.text = context.getString(R.string.bt_disconnected)
                binding.textDeviceStatus.setTextColor(androidx.core.content.ContextCompat.getColor(context, R.color.mz_text_secondary))
                binding.btnConnectToggle.text = context.getString(R.string.bt_connect)
            }

            binding.btnConnectToggle.setOnClickListener {
                onConnectToggle(device, isThisConnected)
            }

            binding.btnDelete.setOnClickListener {
                onDelete(position, device)
            }
        }
    }
}
