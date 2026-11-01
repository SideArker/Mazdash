// Copyright (C) 2026 Mazdash Project
// SPDX-License-Identifier: GPL-3.0-only

package com.dashline.launcher

import android.util.Log

/**
 * Manages the unauthenticated root ADB port (5555) on the headunit.
 *
 * Allows the user to shut down port 5555 while driving to prevent rogue access,
 * and easily re-enable it when pairing with a laptop for development or logs.
 */
object AdbController {
    private const val TAG = "AdbController"

    fun setWifiAdb(enabled: Boolean) {
        val cmd = if (enabled) {
            "setprop service.adb.tcp.port 5555; stop adbd; start adbd"
        } else {
            "setprop service.adb.tcp.port -1; stop adbd"
        }

        try {
            val process = Runtime.getRuntime().exec(arrayOf("sh", "-c", cmd))
            process.waitFor()
            Log.d(TAG, "Wi-Fi ADB set to enabled=$enabled via sh (exit=${process.exitValue()})")
        } catch (e: Exception) {
            try {
                val p = Runtime.getRuntime().exec(arrayOf("su", "-c", cmd))
                p.waitFor()
                Log.d(TAG, "Wi-Fi ADB set to enabled=$enabled via su (exit=${p.exitValue()})")
            } catch (e2: Exception) {
                Log.e(TAG, "Failed to toggle Wi-Fi ADB", e2)
            }
        }
    }
}
