// Copyright (C) 2026 Mazdash Project
// SPDX-License-Identifier: GPL-3.0-only

package com.dashline.launcher

import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import com.andrerinas.openheadunit.contract.HeadUnit
import com.andrerinas.openheadunit.contract.HeadUnitCommand
import com.andrerinas.openheadunit.contract.SessionStateIntent

/**
 * Manages two-way communication between Mazdash and Open Headunit (AAP / Android Auto).
 *
 * Listens to session state broadcasts ([SessionStateIntent]) to determine if
 * Android Auto is connecting, active/projecting, or disconnected, and provides
 * one-tap projection elevation without going through system status shades.
 */
class HeadUnitManager(
    private val context: Context,
    private val onStateChanged: (SessionInfo) -> Unit
) {
    companion object {
        private const val TAG = "HeadUnitManager"
        const val PKG_OPEN_HEADUNIT = HeadUnit.packageName
        const val ACTIVITY_PROJECTION = "com.andrerinas.openheadunit.aap.AapProjectionActivity"
        const val RECEIVER_AUTOMATION = HeadUnitCommand.RECEIVER_CLASS
    }

    data class SessionInfo(
        val state: String = SessionStateIntent.STATE_DISCONNECTED,
        val transport: String = "unknown",
        val isProjecting: Boolean = false
    )

    private var currentInfo = SessionInfo()
    private var isRegistered = false

    private val sessionReceiver = object : BroadcastReceiver() {
        override fun onReceive(ctx: Context, intent: Intent) {
            if (intent.action != SessionStateIntent.action) return
            val state = intent.getStringExtra(SessionStateIntent.EXTRA_STATE)
                ?: SessionStateIntent.STATE_DISCONNECTED
            val transport = intent.getStringExtra(SessionStateIntent.EXTRA_TRANSPORT)
                ?: "unknown"
            val isProjecting = (state == SessionStateIntent.STATE_PROJECTING)

            Log.d(TAG, "Open Headunit session state: $state (transport=$transport, projecting=$isProjecting)")
            currentInfo = SessionInfo(state, transport, isProjecting)
            onStateChanged(currentInfo)
        }
    }

    fun start() {
        if (isRegistered) return
        try {
            val filter = IntentFilter(SessionStateIntent.action)
            context.registerReceiver(sessionReceiver, filter)
            isRegistered = true
            // Initial probe via AutomationReceiver query
            queryState()
        } catch (e: Exception) {
            Log.e(TAG, "Failed to register SessionState receiver", e)
        }
    }

    fun stop() {
        if (!isRegistered) return
        try {
            context.unregisterReceiver(sessionReceiver)
            isRegistered = false
        } catch (e: Exception) {
            Log.e(TAG, "Failed to unregister SessionState receiver", e)
        }
    }

    fun isInstalled(): Boolean = AppRepository.isInstalled(context, PKG_OPEN_HEADUNIT)

    fun isProjecting(): Boolean = currentInfo.isProjecting

    /**
     * Brings Android Auto projection straight to the front.
     * Uses explicit Activity intent first for immediate foregrounding,
     * supplemented by AutomationReceiver broadcast.
     */
    fun raiseProjection(): Boolean {
        return try {
            val intent = Intent().apply {
                component = ComponentName(PKG_OPEN_HEADUNIT, ACTIVITY_PROJECTION)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            }
            context.startActivity(intent)
            // Also notify automation receiver
            val cmd = Intent(HeadUnitCommand.ACTION_RAISE_PROJECTION).apply {
                component = ComponentName(PKG_OPEN_HEADUNIT, RECEIVER_AUTOMATION)
            }
            context.sendBroadcast(cmd)
            true
        } catch (e: Exception) {
            Log.w(TAG, "Direct projection launch failed, launching main app intent", e)
            AppRepository.launch(context, PKG_OPEN_HEADUNIT)
        }
    }

    fun startWireless() {
        try {
            val cmd = Intent(HeadUnitCommand.ACTION_START_WIRELESS).apply {
                component = ComponentName(PKG_OPEN_HEADUNIT, RECEIVER_AUTOMATION)
            }
            context.sendBroadcast(cmd)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to send ACTION_START_WIRELESS", e)
        }
    }

    private fun queryState() {
        try {
            val cmd = Intent(HeadUnitCommand.ACTION_QUERY_STATE).apply {
                component = ComponentName(PKG_OPEN_HEADUNIT, RECEIVER_AUTOMATION)
            }
            context.sendBroadcast(cmd)
        } catch (e: Exception) {
            Log.d(TAG, "State query broadcast failed: ${e.message}")
        }
    }
}
