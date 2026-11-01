// Copyright (C) 2026 Mazdash Project
// SPDX-License-Identifier: GPL-3.0-only

package com.dashline.launcher

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dashline.launcher.databinding.ActivityMazdaPhoneBinding
import com.dashline.launcher.databinding.ItemMazdaPhoneCallLogBinding
import com.dashline.launcher.databinding.ItemMazdaPhoneContactBinding

class MazdaPhoneActivity : AppCompatActivity(), MazdaPhoneController.CallListener {

    private lateinit var binding: ActivityMazdaPhoneBinding
    private lateinit var phoneController: MazdaPhoneController
    private lateinit var btController: MazdaBluetoothController

    private val dialedBuilder = StringBuilder()
    private var activeTab = Tab.KEYPAD

    private lateinit var contactsAdapter: ContactsAdapter
    private lateinit var historyAdapter: HistoryAdapter

    enum class Tab {
        KEYPAD,
        CONTACTS,
        HISTORY
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMazdaPhoneBinding.inflate(layoutInflater)
        setContentView(binding.root)

        phoneController = MazdaPhoneController.getInstance(this)
        btController = MazdaBluetoothController.getInstance(this)

        setupNavigation()
        setupTabs()
        setupKeypad()
        setupLists()
        setupCallActions()
        updateTabVisibility()
    }

    private fun setupNavigation() {
        binding.navBack.setOnClickListener { finish() }
        binding.navHome.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java).apply {
                action = Intent.ACTION_MAIN
                addCategory(Intent.CATEGORY_HOME)
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            }
            startActivity(intent)
        }
        binding.btnOpenBluetooth.setOnClickListener {
            startActivity(Intent(this, MazdaBluetoothActivity::class.java))
        }
    }

    private fun setupTabs() {
        binding.tabKeypad.setOnClickListener {
            activeTab = Tab.KEYPAD
            updateTabVisibility()
        }
        binding.tabContacts.setOnClickListener {
            activeTab = Tab.CONTACTS
            updateTabVisibility()
        }
        binding.tabHistory.setOnClickListener {
            activeTab = Tab.HISTORY
            updateTabVisibility()
        }
    }

    private fun updateTabVisibility() {
        binding.containerKeypad.visibility = if (activeTab == Tab.KEYPAD) View.VISIBLE else View.GONE
        binding.containerContacts.visibility = if (activeTab == Tab.CONTACTS) View.VISIBLE else View.GONE
        binding.containerHistory.visibility = if (activeTab == Tab.HISTORY) View.VISIBLE else View.GONE

        styleTabButton(binding.tabKeypad, activeTab == Tab.KEYPAD)
        styleTabButton(binding.tabContacts, activeTab == Tab.CONTACTS)
        styleTabButton(binding.tabHistory, activeTab == Tab.HISTORY)
    }

    private fun styleTabButton(button: Button, isActive: Boolean) {
        if (isActive) {
            button.setBackgroundResource(R.drawable.btn_rounded_accent)
            button.setTextColor(ContextCompat.getColor(this, R.color.mz_text_on_accent))
        } else {
            button.setBackgroundResource(android.R.color.transparent)
            button.setTextColor(ContextCompat.getColor(this, R.color.mz_text_secondary))
        }
    }

    private fun setupKeypad() {
        fun appendKey(key: String) {
            dialedBuilder.append(key)
            binding.textDialedNumber.text = dialedBuilder.toString()
        }

        binding.key1.setOnClickListener { appendKey("1") }
        binding.key2.setOnClickListener { appendKey("2") }
        binding.key3.setOnClickListener { appendKey("3") }
        binding.key4.setOnClickListener { appendKey("4") }
        binding.key5.setOnClickListener { appendKey("5") }
        binding.key6.setOnClickListener { appendKey("6") }
        binding.key7.setOnClickListener { appendKey("7") }
        binding.key8.setOnClickListener { appendKey("8") }
        binding.key9.setOnClickListener { appendKey("9") }
        binding.keyStar.setOnClickListener { appendKey("*") }
        binding.keyHash.setOnClickListener { appendKey("#") }

        binding.key0.setOnClickListener { appendKey("0") }
        binding.key0.setOnLongClickListener {
            appendKey("+")
            true
        }

        binding.btnBackspace.setOnClickListener {
            if (dialedBuilder.isNotEmpty()) {
                dialedBuilder.deleteCharAt(dialedBuilder.length - 1)
                binding.textDialedNumber.text = dialedBuilder.toString()
            }
        }
        binding.btnBackspace.setOnLongClickListener {
            dialedBuilder.clear()
            binding.textDialedNumber.text = ""
            true
        }

        binding.btnDial.setOnClickListener {
            val num = dialedBuilder.toString()
            if (num.isNotEmpty()) {
                phoneController.dial(num)
            } else {
                Toast.makeText(this, "Wpisz numer telefonu", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupLists() {
        contactsAdapter = ContactsAdapter { contact ->
            dialedBuilder.clear()
            dialedBuilder.append(contact.number)
            binding.textDialedNumber.text = dialedBuilder.toString()
            phoneController.dial(contact.number)
        }
        binding.recyclerContacts.layoutManager = LinearLayoutManager(this)
        binding.recyclerContacts.adapter = contactsAdapter
        contactsAdapter.submitList(phoneController.contacts)

        binding.btnSyncContacts.setOnClickListener {
            phoneController.syncPhonebook()
            Toast.makeText(this, "Pobieranie kontaktów...", Toast.LENGTH_SHORT).show()
        }

        historyAdapter = HistoryAdapter { log ->
            dialedBuilder.clear()
            dialedBuilder.append(log.number)
            binding.textDialedNumber.text = dialedBuilder.toString()
            phoneController.dial(log.number)
        }
        binding.recyclerHistory.layoutManager = LinearLayoutManager(this)
        binding.recyclerHistory.adapter = historyAdapter
        historyAdapter.submitList(phoneController.callLogs)
    }

    private fun setupCallActions() {
        binding.btnHangUp.setOnClickListener {
            phoneController.hangUp()
        }
        binding.btnAnswer.setOnClickListener {
            phoneController.answerCall()
        }
        binding.btnToggleAudioRoute.setOnClickListener {
            phoneController.toggleAudioRoute()
        }
    }

    override fun onResume() {
        super.onResume()
        phoneController.addListener(this)
        val devName = btController.connectedDeviceName
        if (!devName.isNullOrEmpty()) {
            binding.textPhoneBtStatus.text = "Połączono: $devName"
            binding.textPhoneBtStatus.setTextColor(ContextCompat.getColor(this, R.color.mz_status_connected))
        } else {
            binding.textPhoneBtStatus.text = "Brak połączonego telefonu"
            binding.textPhoneBtStatus.setTextColor(ContextCompat.getColor(this, R.color.mz_text_muted))
        }
    }

    override fun onPause() {
        phoneController.removeListener(this)
        super.onPause()
    }

    override fun onCallStateChanged(state: MazdaPhoneController.CallState, number: String, name: String?) {
        if (state == MazdaPhoneController.CallState.IDLE) {
            binding.layoutActiveCall.visibility = View.GONE
            return
        }

        binding.layoutActiveCall.visibility = View.VISIBLE
        binding.textActiveCallName.text = name ?: (if (number.isNotEmpty()) number else "Połączenie")
        binding.textActiveCallNumber.text = number

        when (state) {
            MazdaPhoneController.CallState.DIALING -> {
                binding.textActiveCallStatus.text = "Łączenie..."
                binding.btnAnswer.visibility = View.GONE
            }
            MazdaPhoneController.CallState.INCOMING -> {
                binding.textActiveCallStatus.text = "Połączenie przychodzące"
                binding.btnAnswer.visibility = View.VISIBLE
            }
            MazdaPhoneController.CallState.ACTIVE -> {
                binding.btnAnswer.visibility = View.GONE
                updateDurationDisplay(phoneController.callDurationSeconds)
            }
            MazdaPhoneController.CallState.IDLE -> {}
        }
    }

    override fun onCallDurationTick(seconds: Int) {
        if (phoneController.callState == MazdaPhoneController.CallState.ACTIVE) {
            updateDurationDisplay(seconds)
        }
    }

    override fun onContactsUpdated(contacts: List<MazdaPhoneController.ContactItem>) {
        contactsAdapter.submitList(contacts)
    }

    override fun onCallLogsUpdated(logs: List<MazdaPhoneController.CallLogItem>) {
        historyAdapter.submitList(logs)
    }

    override fun onAudioChannelChanged(isCarSpeaker: Boolean) {
        binding.btnToggleAudioRoute.text = if (isCarSpeaker) "Głośniki auta" else "Słuchawka telefonu"
    }

    private fun updateDurationDisplay(seconds: Int) {
        val min = seconds / 60
        val sec = seconds % 60
        binding.textActiveCallStatus.text = String.format("Rozmowa (%02d:%02d)", min, sec)
    }

    // Adapters
    class ContactsAdapter(
        private val onCallClick: (MazdaPhoneController.ContactItem) -> Unit
    ) : RecyclerView.Adapter<ContactsAdapter.ViewHolder>() {

        private val items = mutableListOf<MazdaPhoneController.ContactItem>()

        fun submitList(list: List<MazdaPhoneController.ContactItem>) {
            items.clear()
            items.addAll(list)
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val binding = ItemMazdaPhoneContactBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            return ViewHolder(binding)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bind(items[position])
        }

        override fun getItemCount(): Int = items.size

        inner class ViewHolder(private val binding: ItemMazdaPhoneContactBinding) :
            RecyclerView.ViewHolder(binding.root) {

            fun bind(item: MazdaPhoneController.ContactItem) {
                binding.textContactName.text = item.name
                binding.textContactNumber.text = item.number
                binding.btnDialContact.setOnClickListener { onCallClick(item) }
                binding.root.setOnClickListener { onCallClick(item) }
            }
        }
    }

    class HistoryAdapter(
        private val onCallClick: (MazdaPhoneController.CallLogItem) -> Unit
    ) : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

        private val items = mutableListOf<MazdaPhoneController.CallLogItem>()

        fun submitList(list: List<MazdaPhoneController.CallLogItem>) {
            items.clear()
            items.addAll(list)
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val binding = ItemMazdaPhoneCallLogBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            return ViewHolder(binding)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bind(items[position])
        }

        override fun getItemCount(): Int = items.size

        inner class ViewHolder(private val binding: ItemMazdaPhoneCallLogBinding) :
            RecyclerView.ViewHolder(binding.root) {

            fun bind(item: MazdaPhoneController.CallLogItem) {
                binding.textCallerName.text = item.name
                binding.textCallerNumber.text = item.number
                binding.textCallDate.text = item.date
                binding.btnRedial.setOnClickListener { onCallClick(item) }
                binding.root.setOnClickListener { onCallClick(item) }
            }
        }
    }
}
