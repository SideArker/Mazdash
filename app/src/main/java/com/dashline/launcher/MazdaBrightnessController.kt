// Copyright (C) 2026 Mazdash Project
// SPDX-License-Identifier: GPL-3.0-only

package com.dashline.launcher

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.provider.Settings
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.TextView

/**
 * Hardware and Window brightness manager for Allwinner T3 (K2001M) and Android.
 * Communicates with Allwinner NWD AbsBackLightManager via broadcasts,
 * updates Settings.System if permitted, and applies window overrides for immediate visual feedback.
 */
object MazdaBrightnessController {

    const val ACTION_SET_BRIGHTNESS = "com.nwd.action.ACTION_SET_BRIGHTNESS"
    const val EXTRA_BRIGHTNESS = "extra_brightness"

    const val ACTION_SET_OS_BACK_LIGHT_STATE = "com.nwd.ACTION_SET_OS_BACK_LIGHT_STATE"
    const val EXTRA_LIGHT_STATE = "extra_light_state"

    const val MIN_BRIGHTNESS = 5
    const val MAX_BRIGHTNESS = 100

    fun getBrightness(context: Context): Int {
        return Prefs(context).screenBrightness.coerceIn(MIN_BRIGHTNESS, MAX_BRIGHTNESS)
    }

    fun setBrightness(activity: Activity, percent: Int, save: Boolean = true) {
        val bounded = percent.coerceIn(MIN_BRIGHTNESS, MAX_BRIGHTNESS)
        if (save) {
            Prefs(activity).screenBrightness = bounded
        }

        // 1. Immediate window brightness override (works on any Android phone/device)
        applyWindowBrightness(activity, bounded)

        // 2. Hardware broadcast for Allwinner T3 LCD inverter / backlight driver
        runCatching {
            val intent = Intent(ACTION_SET_BRIGHTNESS).apply {
                putExtra(EXTRA_BRIGHTNESS, bounded)
            }
            activity.sendBroadcast(intent)
        }

        // 3. System brightness setting (persists if WRITE_SETTINGS is granted)
        runCatching {
            val sysValue = (bounded * 255) / 100
            Settings.System.putInt(
                activity.contentResolver,
                Settings.System.SCREEN_BRIGHTNESS,
                sysValue
            )
        }
    }

    fun applyWindowBrightness(activity: Activity, percent: Int? = null) {
        val p = (percent ?: getBrightness(activity)).coerceIn(MIN_BRIGHTNESS, MAX_BRIGHTNESS)
        val lp = activity.window.attributes
        lp.screenBrightness = (p / 100f).coerceIn(0.05f, 1.0f)
        activity.window.attributes = lp
    }

    fun setScreenBacklightOn(context: Context, on: Boolean) {
        runCatching {
            val intent = Intent(ACTION_SET_OS_BACK_LIGHT_STATE).apply {
                putExtra(EXTRA_LIGHT_STATE, if (on) 1 else 0)
            }
            context.sendBroadcast(intent)
        }
    }

    /**
     * Activates night "Screen Off" mode. Shows an absolute black fullscreen surface,
     * reduces backlight to minimum, and turns off hardware backlight where supported.
     * Touching anywhere immediately wakes the screen and restores previous brightness.
     */
    fun showScreenOffBlackout(activity: Activity) {
        val previousBrightness = getBrightness(activity)
        setScreenBacklightOn(activity, false)

        val blackoutDialog = Dialog(activity, android.R.style.Theme_NoTitleBar_Fullscreen)
        blackoutDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        val root = FrameLayout(activity).apply {
            setBackgroundColor(Color.BLACK)
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )

            val hint = TextView(activity).apply {
                text = activity.getString(R.string.brightness_screen_off_hint)
                setTextColor(Color.parseColor("#44FFFFFF"))
                textSize = 14f
                gravity = Gravity.CENTER
            }
            val lp = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT,
                Gravity.CENTER
            )
            addView(hint, lp)
        }

        blackoutDialog.setContentView(root)

        blackoutDialog.window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.BLACK))
            setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT)
            val attr = attributes
            attr.screenBrightness = 0.01f
            attributes = attr
        }

        root.setOnClickListener {
            setScreenBacklightOn(activity, true)
            setBrightness(activity, previousBrightness, save = false)
            blackoutDialog.dismiss()
        }

        blackoutDialog.setOnCancelListener {
            setScreenBacklightOn(activity, true)
            setBrightness(activity, previousBrightness, save = false)
        }

        blackoutDialog.show()
    }
}
