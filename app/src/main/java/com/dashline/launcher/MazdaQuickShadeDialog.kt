// Copyright (C) 2026 Mazdash Project
// SPDX-License-Identifier: GPL-3.0-only

package com.dashline.launcher

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.AudioManager
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.view.Gravity
import android.view.MotionEvent
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.SeekBar
import com.dashline.launcher.databinding.ViewMazdaQuickShadeBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MazdaQuickShadeDialog(private val activity: Activity) {

    private val dialog = Dialog(activity, R.style.MazdaTopSheetDialog)
    private val binding: ViewMazdaQuickShadeBinding
    private val audioManager = activity.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    private val handler = Handler(Looper.getMainLooper())

    private var previousVolume = 8
    private var isDraggingBrightness = false
    private var isDraggingVolume = false

    private val timeTicker = object : Runnable {
        override fun run() {
            updateClockAndDate()
            handler.postDelayed(this, 1000)
        }
    }

    init {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        binding = ViewMazdaQuickShadeBinding.inflate(activity.layoutInflater)
        dialog.setContentView(binding.root)

        dialog.window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setGravity(Gravity.TOP)
            setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
            // Clear status bar flags so it hugs the very top edge
            clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE)
        }

        setupHeader()
        setupBrightness()
        setupVolume()
        setupTiles()
        setupDismissGestures()

        dialog.setOnDismissListener {
            handler.removeCallbacks(timeTicker)
            activeDialog = null
        }
    }

    private fun updateClockAndDate() {
        val now = Date()
        val timeFmt = SimpleDateFormat("HH:mm", Locale.getDefault())
        val dateFmt = SimpleDateFormat("EEEE, d MMMM", Locale.getDefault())
        binding.shadeClock.text = timeFmt.format(now)
        binding.shadeDate.text = dateFmt.format(now).replaceFirstChar { it.uppercase() }
    }

    private fun setupHeader() {
        updateClockAndDate()
        handler.postDelayed(timeTicker, 1000)

        binding.shadeBtnClose.setOnClickListener {
            dialog.dismiss()
        }

        binding.shadeBtnSettings.setOnClickListener {
            dialog.dismiss()
            activity.startActivity(Intent(activity, SettingsActivity::class.java))
        }

        binding.shadeBtnWifi.setOnClickListener {
            dialog.dismiss()
            runCatching {
                activity.startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
            }
        }

        binding.shadeBtnBt.setOnClickListener {
            dialog.dismiss()
            activity.startActivity(Intent(activity, MazdaBluetoothActivity::class.java))
        }
    }

    private fun setupBrightness() {
        val current = MazdaBrightnessController.getBrightness(activity)
        binding.shadeBrightnessSlider.progress = current
        binding.shadeBrightnessValue.text = "${current}%"

        binding.shadeBrightnessSlider.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val clamped = progress.coerceIn(
                    MazdaBrightnessController.MIN_BRIGHTNESS,
                    MazdaBrightnessController.MAX_BRIGHTNESS
                )
                binding.shadeBrightnessValue.text = "${clamped}%"
                if (fromUser) {
                    MazdaBrightnessController.setBrightness(activity, clamped)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                isDraggingBrightness = true
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                isDraggingBrightness = false
            }
        })

        binding.shadePreset25.setOnClickListener { applyBrightness(25) }
        binding.shadePreset50.setOnClickListener { applyBrightness(50) }
        binding.shadePreset75.setOnClickListener { applyBrightness(75) }
        binding.shadePreset100.setOnClickListener { applyBrightness(100) }
    }

    private fun applyBrightness(percent: Int) {
        binding.shadeBrightnessSlider.progress = percent
        binding.shadeBrightnessValue.text = "${percent}%"
        MazdaBrightnessController.setBrightness(activity, percent)
    }

    private fun setupVolume() {
        val dsp = MazdaAudioDspController.getInstance(activity)
        val max = MazdaAudioDspController.MAX_SYSTEM_VOLUME
        val current = dsp.systemVolume

        binding.shadeVolumeSlider.max = max
        binding.shadeVolumeSlider.progress = current
        binding.shadeVolumeValue.text = "$current"
        updateVolumeIcon(current)

        binding.shadeVolumeSlider.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                binding.shadeVolumeValue.text = "$progress"
                updateVolumeIcon(progress)
                if (fromUser) {
                    dsp.setSystemVolume(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                isDraggingVolume = true
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                isDraggingVolume = false
            }
        })

        binding.shadeVolumeIcon.setOnClickListener {
            val vol = dsp.systemVolume
            if (vol > 0) {
                previousVolume = vol
                dsp.setSystemVolume(0)
                binding.shadeVolumeSlider.progress = 0
            } else {
                val restore = if (previousVolume > 0) previousVolume else 20
                dsp.setSystemVolume(restore)
                binding.shadeVolumeSlider.progress = restore
            }
        }
    }

    private fun updateVolumeIcon(vol: Int) {
        binding.shadeVolumeIcon.setImageResource(
            if (vol == 0) R.drawable.ic_volume_off else R.drawable.ic_volume
        )
    }

    private fun setupTiles() {
        binding.shadeTileScreenOff.setOnClickListener {
            dialog.dismiss()
            MazdaBrightnessController.showScreenOffBlackout(activity)
        }

        binding.shadeTileRadio.setOnClickListener {
            dialog.dismiss()
            activity.startActivity(Intent(activity, MazdaRadioActivity::class.java))
        }

        binding.shadeTilePhone.setOnClickListener {
            dialog.dismiss()
            activity.startActivity(Intent(activity, MazdaPhoneActivity::class.java))
        }

        binding.shadeTileAuto.setOnClickListener {
            dialog.dismiss()
            val pm = activity.packageManager
            val intent = pm.getLaunchIntentForPackage(HeadUnitManager.PKG_OPEN_HEADUNIT)
            if (intent != null) {
                activity.startActivity(intent)
            } else {
                runCatching {
                    val fallback = pm.getLaunchIntentForPackage("com.andrerinas.headunitrevived")
                    if (fallback != null) activity.startActivity(fallback)
                }
            }
        }
    }

    private fun setupDismissGestures() {
        binding.shadeDismissHandle.setOnClickListener {
            dialog.dismiss()
        }

        // Swipe up on handle or shade to dismiss
        var startY = 0f
        binding.shadeDismissHandle.setOnTouchListener { _, event ->
            when (event.actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    startY = event.rawY
                    true
                }
                MotionEvent.ACTION_MOVE -> {
                    if (startY - event.rawY > 40 * activity.resources.displayMetrics.density) {
                        dialog.dismiss()
                        true
                    } else false
                }
                else -> false
            }
        }
    }

    fun show() {
        if (!activity.isFinishing && !dialog.isShowing) {
            dialog.show()
            activeDialog = this
        }
    }

    fun dismiss() {
        if (dialog.isShowing) {
            dialog.dismiss()
        }
    }

    companion object {
        private var activeDialog: MazdaQuickShadeDialog? = null

        fun isShowing(): Boolean = activeDialog?.dialog?.isShowing == true

        fun show(activity: Activity) {
            if (isShowing()) return
            MazdaQuickShadeDialog(activity).show()
        }

        fun dismiss() {
            activeDialog?.dismiss()
            activeDialog = null
        }
    }
}
