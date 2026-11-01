// Copyright (C) 2026 Mazdash Project
// SPDX-License-Identifier: GPL-3.0-only

package com.dashline.launcher

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import android.view.WindowManager
import android.widget.SeekBar
import com.dashline.launcher.databinding.DialogMazdaBrightnessBinding

class MazdaBrightnessDialog(
    private val activity: Activity,
    private val onDismiss: (() -> Unit)? = null
) {

    private val dialog = Dialog(activity)
    private val binding: DialogMazdaBrightnessBinding

    init {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        binding = DialogMazdaBrightnessBinding.inflate(activity.layoutInflater)
        dialog.setContentView(binding.root)

        dialog.window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            // Ensure proper width for automotive head units (landscape / portrait)
            val metrics = activity.resources.displayMetrics
            val width = (metrics.widthPixels * 0.70).toInt().coerceIn(320, 600)
            setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT)
        }

        dialog.setOnDismissListener {
            onDismiss?.invoke()
        }

        setupControls()
    }

    private fun setupControls() {
        val initialBrightness = MazdaBrightnessController.getBrightness(activity)
        binding.sliderBrightness.progress = initialBrightness
        binding.textBrightnessPercent.text = "${initialBrightness}%"

        binding.sliderBrightness.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val clamped = progress.coerceIn(MazdaBrightnessController.MIN_BRIGHTNESS, MazdaBrightnessController.MAX_BRIGHTNESS)
                binding.textBrightnessPercent.text = "${clamped}%"
                MazdaBrightnessController.setBrightness(activity, clamped)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        binding.btnPresetNight.setOnClickListener {
            applyPreset(25)
        }

        binding.btnPreset50.setOnClickListener {
            applyPreset(50)
        }

        binding.btnPreset75.setOnClickListener {
            applyPreset(75)
        }

        binding.btnPresetDay.setOnClickListener {
            applyPreset(100)
        }

        binding.btnScreenOff.setOnClickListener {
            dialog.dismiss()
            MazdaBrightnessController.showScreenOffBlackout(activity)
        }

        binding.btnClose.setOnClickListener {
            dialog.dismiss()
        }
    }

    private fun applyPreset(percent: Int) {
        binding.sliderBrightness.progress = percent
        binding.textBrightnessPercent.text = "${percent}%"
        MazdaBrightnessController.setBrightness(activity, percent)
    }

    fun show() {
        if (!activity.isFinishing) {
            dialog.show()
        }
    }
}
