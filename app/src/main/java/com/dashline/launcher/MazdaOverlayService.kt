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
import android.util.Log
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout

/**
 * Unified Mazda Overlay & HUD Service.
 *
 * Hosts and coordinates:
 * 1. [MazdaVolumeHud]: Floating Mazda volume capsule reacting to dials & steering wheel keys.
 * 2. [MazdaCallHud]: Floating Mazda call banner for incoming and active phone calls.
 * 3. Floating Mazda Home overlay button when third-party apps are in the foreground.
 */
class MazdaOverlayService : Service() {

    companion object {
        private const val TAG = "MazdaOverlayService"
        const val ACTION_START = "com.dashline.launcher.action.OVERLAY_START"
        const val ACTION_SHOW = "com.dashline.launcher.action.OVERLAY_SHOW"
        const val ACTION_HIDE = "com.dashline.launcher.action.OVERLAY_HIDE"

        fun start(context: Context) {
            val intent = Intent(context, MazdaOverlayService::class.java).apply {
                action = ACTION_START
            }
            context.startService(intent)
        }

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

    private var volumeHud: MazdaVolumeHud? = null
    private var callHud: MazdaCallHud? = null

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        createOverlayView()

        // Initialize HUD components
        volumeHud = MazdaVolumeHud(this)
        callHud = MazdaCallHud(this).apply {
            onVisibilityChanged = { callShowing ->
                // Shift Volume HUD slightly down if call HUD is active to prevent overlap
                volumeHud?.setVerticalOffset(if (callShowing) 84 else 18)
            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val prefs = Prefs(this)

        when (intent?.action) {
            ACTION_SHOW -> {
                if (prefs.floatingHomeEnabled) {
                    showOverlay()
                } else {
                    hideOverlay()
                }
            }
            ACTION_HIDE -> hideOverlay()
            ACTION_START -> {
                // Keep HUD listeners active
            }
        }
        return START_STICKY
    }

    private fun createOverlayView() {
        if (overlayView != null) return

        val density = resources.displayMetrics.density

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            val padH = (6 * density).toInt()
            val padV = (3 * density).toInt()
            setPadding(padH, padV, padH, padV)

            // Sleek Mazda stealth capsule with Soul Red border
            val accentColor = androidx.core.content.ContextCompat.getColor(this@MazdaOverlayService, R.color.mz_accent)
            val bgDeep = androidx.core.content.ContextCompat.getColor(this@MazdaOverlayService, R.color.mz_bg_deep)
            val bg = GradientDrawable().apply {
                setColor(GradientThemes.withAlpha(bgDeep, 235)) // 92% dark stealth
                setStroke((1.5f * density).toInt(), accentColor) // Soul Red border
                cornerRadius = 24 * density
            }
            background = bg
            elevation = 6 * density
        }

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

        fun setupNavButton(btn: ImageView, onClick: () -> Unit) {
            btn.setOnTouchListener { _, event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        initialX = params.x
                        initialY = params.y
                        initialTouchX = event.rawX
                        initialTouchY = event.rawY
                        isDragging = false
                        btn.alpha = 0.5f
                        true
                    }
                    MotionEvent.ACTION_MOVE -> {
                        val dx = (event.rawX - initialTouchX).toInt()
                        val dy = (event.rawY - initialTouchY).toInt()
                        if (Math.abs(dx) > 10 || Math.abs(dy) > 10) {
                            isDragging = true
                            btn.alpha = 1.0f
                            params.x = initialX + dx
                            params.y = initialY + dy
                            if (isVisible && overlayView != null) {
                                windowManager?.updateViewLayout(overlayView, params)
                            }
                        }
                        true
                    }
                    MotionEvent.ACTION_UP -> {
                        btn.alpha = 1.0f
                        if (!isDragging) {
                            onClick()
                        }
                        true
                    }
                    MotionEvent.ACTION_CANCEL -> {
                        btn.alpha = 1.0f
                        true
                    }
                    else -> false
                }
            }
        }

        val accentColor = androidx.core.content.ContextCompat.getColor(this, R.color.mz_accent)
        val btnW = (38 * density).toInt()
        val btnH = (36 * density).toInt()
        val btnPad = (7 * density).toInt()

        // 1. Back button
        val btnBack = ImageView(this).apply {
            setImageResource(R.drawable.ic_back)
            setColorFilter(accentColor)
            layoutParams = LinearLayout.LayoutParams(btnW, btnH)
            setPadding(btnPad, btnPad, btnPad, btnPad)
        }
        setupNavButton(btnBack) { triggerBack() }

        // Divider
        fun createDivider() = View(this).apply {
            layoutParams = LinearLayout.LayoutParams((1 * density).toInt(), (18 * density).toInt()).apply {
                setMargins((2 * density).toInt(), 0, (2 * density).toInt(), 0)
            }
            setBackgroundColor(Color.parseColor("#44FFFFFF"))
        }

        // 2. Home button
        val btnHome = ImageView(this).apply {
            setImageResource(R.drawable.ic_home)
            setColorFilter(accentColor)
            layoutParams = LinearLayout.LayoutParams(btnW, btnH)
            setPadding(btnPad, btnPad, btnPad, btnPad)
        }
        setupNavButton(btnHome) { returnToHome() }

        // 3. Apps button
        val btnApps = ImageView(this).apply {
            setImageResource(R.drawable.ic_apps)
            setColorFilter(accentColor)
            layoutParams = LinearLayout.LayoutParams(btnW, btnH)
            setPadding(btnPad, btnPad, btnPad, btnPad)
        }
        setupNavButton(btnApps) { openAppDrawer() }

        layout.addView(btnBack)
        layout.addView(createDivider())
        layout.addView(btnHome)
        layout.addView(createDivider())
        layout.addView(btnApps)

        overlayView = layout
        layout.tag = params
    }

    private fun triggerBack() {
        Thread {
            try {
                val p = Runtime.getRuntime().exec(arrayOf("sh", "-c", "input keyevent 4"))
                p.waitFor()
                if (p.exitValue() != 0) {
                    Runtime.getRuntime().exec(arrayOf("su", "-c", "input keyevent 4")).waitFor()
                }
            } catch (e: Exception) {
                try {
                    Runtime.getRuntime().exec(arrayOf("su", "-c", "input keyevent 4")).waitFor()
                } catch (e2: Exception) {
                    Log.e(TAG, "Failed back keyevent", e2)
                }
            }
        }.start()
    }

    private fun openAppDrawer() {
        val appsIntent = Intent(this, AppDrawerActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_SINGLE_TOP or
                    Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        startActivity(appsIntent)
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
        volumeHud?.destroy()
        volumeHud = null
        callHud?.destroy()
        callHud = null
        super.onDestroy()
    }
}
