// Copyright (C) 2026 Mazdash Project
// SPDX-License-Identifier: GPL-3.0-only

package com.dashline.launcher

import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.PixelFormat
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.IBinder
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

/**
 * Lightweight floating Mazda Home overlay button.
 *
 * Appears when the user enters an external app (like Open Headunit, Radio, or BT Music)
 * and automatically hides when the user returns to Mazdash.
 * Tapping it brings Mazdash HomeActivity to the front instantly, eliminating the need
 * for the obsolete Android status bar / swipe-down panel.
 */
class MazdaOverlayService : Service() {

    companion object {
        const val ACTION_SHOW = "com.dashline.launcher.action.OVERLAY_SHOW"
        const val ACTION_HIDE = "com.dashline.launcher.action.OVERLAY_HIDE"

        fun show(context: Context) {
            val intent = Intent(context, MazdaOverlayService::class.java).apply {
                action = ACTION_SHOW
            }
            context.startService(intent)
        }

        fun hide(context: Context) {
            val intent = Intent(context, MazdaOverlayService::class.java).apply {
                action = ACTION_HIDE
            }
            context.startService(intent)
        }
    }

    private var windowManager: WindowManager? = null
    private var overlayView: View? = null
    private var isVisible = false

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        createOverlayView()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val prefs = Prefs(this)
        if (!prefs.floatingHomeEnabled) {
            hideOverlay()
            return START_NOT_STICKY
        }

        when (intent?.action) {
            ACTION_SHOW -> showOverlay()
            ACTION_HIDE -> hideOverlay()
        }
        return START_STICKY
    }

    private fun createOverlayView() {
        if (overlayView != null) return

        val density = resources.displayMetrics.density

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER
            val padding = (10 * density).toInt()
            setPadding(padding, padding, padding, padding)

            // Sleek Mazda stealth capsule with Soul Red border
            val accentColor = androidx.core.content.ContextCompat.getColor(this, R.color.mz_accent)
            val bgDeep = androidx.core.content.ContextCompat.getColor(this, R.color.mz_bg_deep)
            val bg = GradientDrawable().apply {
                setColor(GradientThemes.withAlpha(bgDeep, 230)) // 90% dark stealth
                setStroke((1.5f * density).toInt(), accentColor) // Soul Red border
                cornerRadius = 24 * density
            }
            background = bg
            elevation = 6 * density
        }

        val accentColor = androidx.core.content.ContextCompat.getColor(this, R.color.mz_accent)
        val icon = ImageView(this).apply {
            setImageResource(R.drawable.ic_home)
            setColorFilter(accentColor) // Soul Red vector icon
            layoutParams = LinearLayout.LayoutParams((22 * density).toInt(), (22 * density).toInt())
        }

        layout.addView(icon)

        // Drag & click listener
        val windowType = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            @Suppress("DEPRECATION")
            WindowManager.LayoutParams.TYPE_PHONE
        }

        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            windowType,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.TOP or Gravity.START
            x = (16 * density).toInt()
            y = (20 * density).toInt()
        }

        var initialX = 0
        var initialY = 0
        var initialTouchX = 0f
        var initialTouchY = 0f
        var isDragging = false

        layout.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    initialX = params.x
                    initialY = params.y
                    initialTouchX = event.rawX
                    initialTouchY = event.rawY
                    isDragging = false
                    true
                }
                MotionEvent.ACTION_MOVE -> {
                    val dx = (event.rawX - initialTouchX).toInt()
                    val dy = (event.rawY - initialTouchY).toInt()
                    if (Math.abs(dx) > 10 || Math.abs(dy) > 10) {
                        isDragging = true
                        params.x = initialX + dx
                        params.y = initialY + dy
                        if (isVisible && overlayView != null) {
                            windowManager?.updateViewLayout(overlayView, params)
                        }
                    }
                    true
                }
                MotionEvent.ACTION_UP -> {
                    if (!isDragging) {
                        // Tapped: Return directly to Mazdash Home
                        returnToHome()
                    }
                    true
                }
                else -> false
            }
        }

        overlayView = layout
        layout.tag = params
    }

    private fun showOverlay() {
        val view = overlayView ?: return
        val params = view.tag as? WindowManager.LayoutParams ?: return
        if (isVisible) return

        try {
            windowManager?.addView(view, params)
            isVisible = true
        } catch (e: Exception) {
            // Permission or display error
        }
    }

    private fun hideOverlay() {
        val view = overlayView ?: return
        if (!isVisible) return

        try {
            windowManager?.removeView(view)
            isVisible = false
        } catch (e: Exception) {
            // Ignored
        }
    }

    private fun returnToHome() {
        val homeIntent = Intent(this, HomeActivity::class.java).apply {
            action = Intent.ACTION_MAIN
            addCategory(Intent.CATEGORY_HOME)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_SINGLE_TOP or
                    Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        startActivity(homeIntent)
    }

    override fun onDestroy() {
        hideOverlay()
        super.onDestroy()
    }
}
