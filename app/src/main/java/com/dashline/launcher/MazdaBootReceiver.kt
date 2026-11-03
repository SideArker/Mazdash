// Copyright (C) 2026 Mazdash Project
// SPDX-License-Identifier: GPL-3.0-only

package com.dashline.launcher

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

/**
 * Ensures Mazdash background services and ADB listeners are immediately restored
 * upon head unit cold boot or MCU ACTION_INIT_FINISH trigger.
 */
class MazdaBootReceiver : BroadcastReceiver() {

    companion object {
        private const val TAG = "MazdaBootReceiver"
        private const val ACTION_INIT_FINISH = "com.android.action.ACTION_INIT_FINISH"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        val action = intent?.action ?: return
        Log.i(TAG, "Boot broadcast received: $action")
        if (context == null) return

        when (action) {
            Intent.ACTION_BOOT_COMPLETED,
            ACTION_INIT_FINISH -> {
                val prefs = Prefs(context)
                if (prefs.adbWifiEnabled) {
                    AdbController.setWifiAdb(true)
                }
                MazdaOverlayService.start(context)
            }
        }
    }
}
