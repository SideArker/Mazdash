// Copyright (C) 2026 Mazdash Project
// SPDX-License-Identifier: GPL-3.0-only

package com.dashline.launcher

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.database.ContentObserver
import android.graphics.PixelFormat
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.core.content.ContextCompat

/**
 * Native Mazda Floating Volume HUD.
 *
 * Pinned cleanly at the top center of the screen, styled in Mazda Soul Red & dark stealth glass.
 * Directly replaces the clunky stock Carkit_VolumeView (com.nwd.volumeview) and responds
 * instantly to hardware rotary dials, steering wheel controls, and touch adjustments.
 */
class MazdaVolumeHud(private val context: Context) : MazdaAudioDspController.Listener {

    companion object {
        private const val TAG = "MazdaVolumeHud"
        private const val HIDE_DELAY_MS = 2500L
        private const val ACTION_MUTE_STATE_CHANGE = "com.nwd.action.ACTION_MUTE_STATE_CHANGE"
        private const val EXTRA_MUTE_STATE = "extra_mute_state"
        private const val ACTION_SET_MUTE = "com.nwd.action.ACTION_SET_MUTE"
        private const val EXTRA_MUTE = "extra_mute"
        private const val ACTION_KEY_VALUE = "com.nwd.action.ACTION_KEY_VALUE"
        private const val EXTRA_KEY_TYPE = "extra_key_type"
        private const val EXTRA_KEY_VALUE = "extra_key_value"
    }

    private val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    private val mainHandler = Handler(Looper.getMainLooper())
    private val dspController = MazdaAudioDspController.getInstance(context)
    private val prefs = Prefs(context)

    private var hudView: View? = null
    private var windowParams: WindowManager.LayoutParams? = null
    private var isShowing = false
    private var isUserDragging = false
    private var isMuted = false

    private val hideRunnable = Runnable {
        hide()
    }

    private val volumeObserver = object : ContentObserver(mainHandler) {
        override fun onChange(selfChange: Boolean, uri: Uri?) {
            super.onChange(selfChange, uri)
            syncFromSettings()
        }
    }

    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(c: Context?, intent: Intent?) {
            val action = intent?.action ?: return
            Log.d(TAG, "Volume HUD broadcast received: $action")
            when (action) {
                ACTION_MUTE_STATE_CHANGE -> {
                    val state = intent.getByteExtra(EXTRA_MUTE_STATE, 0.toByte())
                    isMuted = (state.toInt() == 1)
                    updateMuteDisplay()
                    triggerShow()
                }
                "android.media.VOLUME_CHANGED_ACTION" -> {
                    syncFromSettings()
                }
            }
        }
    }

    init {
        dspController.addListener(this)
        registerObservers()
    }

    private fun registerObservers() {
        try {
            val cr = context.contentResolver
            cr.registerContentObserver(Settings.System.getUriFor("mcu_system_volume"), false, volumeObserver)
            cr.registerContentObserver(Settings.System.getUriFor("mcu_mute_state"), false, volumeObserver)
        } catch (e: Exception) {
            Log.w(TAG, "Failed registering volume ContentObserver: ${e.message}")
        }

        try {
            val filter = IntentFilter().apply {
                addAction(ACTION_MUTE_STATE_CHANGE)
                addAction("android.media.VOLUME_CHANGED_ACTION")
            }
            context.registerReceiver(broadcastReceiver, filter)
        } catch (e: Exception) {
            Log.w(TAG, "Failed registering volume BroadcastReceiver: ${e.message}")
        }
    }

    private fun syncFromSettings() {
        try {
            val cr = context.contentResolver
            val vol = Settings.System.getInt(cr, "mcu_system_volume", dspController.systemVolume)
            val muteVal = Settings.System.getInt(cr, "mcu_mute_state", 0)
            isMuted = (muteVal == 1 || vol == 0)

            if (!isUserDragging) {
                updateViews(vol)
                triggerShow()
            }
        } catch (e: Exception) {
            Log.w(TAG, "syncFromSettings error: ${e.message}")
        }
    }

    override fun onSystemVolumeChanged(volume: Int) {
        if (!isUserDragging) {
            updateViews(volume)
            triggerShow()
        }
    }

    private fun triggerShow() {
        if (!prefs.customVolumeHudEnabled) return

        mainHandler.post {
            ensureViewCreated()
            if (!isShowing) {
                show()
            }
            resetAutoHide()
        }
    }

    private fun ensureViewCreated() {
        if (hudView != null) return

        val density = context.resources.displayMetrics.density
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.layout_mazda_volume_hud, null)

        val windowType = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            @Suppress("DEPRECATION")
            WindowManager.LayoutParams.TYPE_PHONE
        }

        val params = WindowManager.LayoutParams(
            (410 * density).toInt(),
            WindowManager.LayoutParams.WRAP_CONTENT,
            windowType,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
            x = 0
            y = (18 * density).toInt()
        }

        val seekBar = view.findViewById<SeekBar>(R.id.hudVolumeSeekBar)
        val tvValue = view.findViewById<TextView>(R.id.hudVolumeValue)
        val ivIcon = view.findViewById<ImageView>(R.id.hudVolumeIcon)
        val btnMinus = view.findViewById<TextView>(R.id.btnHudVolumeMinus)
        val btnPlus = view.findViewById<TextView>(R.id.btnHudVolumePlus)

        val maxVol = try {
            Settings.System.getInt(context.contentResolver, "mcu_max_volume", MazdaAudioDspController.MAX_SYSTEM_VOLUME)
        } catch (e: Exception) {
            MazdaAudioDspController.MAX_SYSTEM_VOLUME
        }
        seekBar.max = maxVol
        seekBar.progress = dspController.systemVolume
        tvValue.text = dspController.systemVolume.toString()

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(sb: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    isMuted = (progress == 0)
                    updateMuteDisplay()
                    tvValue.text = if (isMuted) context.getString(R.string.volume_hud_muted) else progress.toString()
                    dspController.setSystemVolume(progress)
                    resetAutoHide()
                }
            }

            override fun onStartTrackingTouch(sb: SeekBar?) {
                isUserDragging = true
                mainHandler.removeCallbacks(hideRunnable)
            }

            override fun onStopTrackingTouch(sb: SeekBar?) {
                isUserDragging = false
                resetAutoHide()
            }
        })

        btnMinus.setOnClickListener {
            val newVol = (dspController.systemVolume - 1).coerceAtLeast(0)
            dspController.setSystemVolume(newVol)
            updateViews(newVol)
            resetAutoHide()
        }

        btnPlus.setOnClickListener {
            val newVol = (dspController.systemVolume + 1).coerceAtMost(seekBar.max)
            dspController.setSystemVolume(newVol)
            updateViews(newVol)
            resetAutoHide()
        }

        ivIcon.setOnClickListener {
            toggleMute()
            resetAutoHide()
        }

        hudView = view
        windowParams = params
    }

    private fun updateViews(volume: Int) {
        val view = hudView ?: return
        val seekBar = view.findViewById<SeekBar>(R.id.hudVolumeSeekBar)
        val tvValue = view.findViewById<TextView>(R.id.hudVolumeValue)

        seekBar.progress = volume
        tvValue.text = if (isMuted || volume == 0) context.getString(R.string.volume_hud_muted) else volume.toString()
        updateMuteDisplay()
    }

    private fun updateMuteDisplay() {
        val view = hudView ?: return
        val ivIcon = view.findViewById<ImageView>(R.id.hudVolumeIcon)
        val tvValue = view.findViewById<TextView>(R.id.hudVolumeValue)

        if (isMuted || dspController.systemVolume == 0) {
            ivIcon.setImageResource(R.drawable.ic_volume_off)
            ivIcon.setColorFilter(ContextCompat.getColor(context, R.color.mz_status_danger))
            tvValue.text = context.getString(R.string.volume_hud_muted)
        } else {
            ivIcon.setImageResource(R.drawable.ic_volume)
            ivIcon.setColorFilter(ContextCompat.getColor(context, R.color.mz_accent))
            tvValue.text = dspController.systemVolume.toString()
        }
    }

    private fun toggleMute() {
        isMuted = !isMuted
        updateMuteDisplay()

        try {
            // Direct MCU mute broadcast
            val intentMute = Intent(ACTION_SET_MUTE).apply {
                putExtra(EXTRA_MUTE, isMuted)
            }
            context.sendBroadcast(intentMute)

            // Also send MCU key broadcast
            val intentKey = Intent(ACTION_KEY_VALUE).apply {
                putExtra(EXTRA_KEY_TYPE, 0)
                putExtra(EXTRA_KEY_VALUE, 2.toByte()) // KEY_MUTE
            }
            context.sendBroadcast(intentKey)
        } catch (e: Exception) {
            Log.w(TAG, "toggleMute failed: ${e.message}")
        }
    }

    private fun show() {
        val view = hudView ?: return
        val params = windowParams ?: return
        if (isShowing) return

        try {
            val density = context.resources.displayMetrics.density
            view.alpha = 0f
            view.translationY = -24f * density

            windowManager.addView(view, params)
            isShowing = true

            view.animate()
                .alpha(1f)
                .translationY(0f)
                .setDuration(180)
                .start()
        } catch (e: Exception) {
            Log.e(TAG, "Failed adding volume HUD to WindowManager: ${e.message}")
        }
    }

    private fun hide() {
        val view = hudView ?: return
        if (!isShowing) return

        val density = context.resources.displayMetrics.density
        view.animate()
            .alpha(0f)
            .translationY(-24f * density)
            .setDuration(220)
            .withEndAction {
                try {
                    if (isShowing) {
                        windowManager.removeView(view)
                        isShowing = false
                    }
                } catch (e: Exception) {
                    Log.w(TAG, "Failed removing volume HUD: ${e.message}")
                }
            }
            .start()
    }

    private fun resetAutoHide() {
        mainHandler.removeCallbacks(hideRunnable)
        mainHandler.postDelayed(hideRunnable, HIDE_DELAY_MS)
    }

    fun setVerticalOffset(yOffsetDp: Int) {
        val density = context.resources.displayMetrics.density
        windowParams?.y = (yOffsetDp * density).toInt()
        if (isShowing && hudView != null) {
            try {
                windowManager.updateViewLayout(hudView, windowParams)
            } catch (e: Exception) {
                // ignore
            }
        }
    }

    fun destroy() {
        mainHandler.removeCallbacks(hideRunnable)
        if (isShowing && hudView != null) {
            try {
                windowManager.removeView(hudView)
            } catch (e: Exception) {
                // ignore
            }
            isShowing = false
        }
        dspController.removeListener(this)
        try {
            context.contentResolver.unregisterContentObserver(volumeObserver)
        } catch (e: Exception) {
            // ignore
        }
        try {
            context.unregisterReceiver(broadcastReceiver)
        } catch (e: Exception) {
            // ignore
        }
    }
}
