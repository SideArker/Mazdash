// Copyright (C) 2026 Mazdash Project
// SPDX-License-Identifier: GPL-3.0-only

package com.dashline.launcher

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import com.dashline.launcher.databinding.ActivityMazdaAudioDspBinding

class MazdaAudioDspActivity : BaseActivity() {

    private lateinit var binding: ActivityMazdaAudioDspBinding
    private lateinit var dspController: MazdaAudioDspController

    private lateinit var eqSliders: Array<MazdaVerticalSlider>
    private lateinit var eqLabels: Array<TextView>
    private lateinit var profileButtons: Array<Button>

    private var isUpdatingFromHardware = false
    private var isUserInteractingWithSliders = false
    private var isCustomUserCurve = false

    private val dspListener = object : MazdaAudioDspController.Listener {
        override fun onStageChanged(fader: Int, balance: Int) {
            runOnUiThread {
                binding.soundStageView.fader = fader
                binding.soundStageView.balance = balance
                updateStageLabels(fader, balance)
            }
        }

        override fun onEqualizerChanged(bass: Int, tenor: Int, treble: Int, heavyBass: Int, eqMode: Int) {
            runOnUiThread {
                binding.seekHeavyBass.progress = heavyBass
                binding.textHeavyBassVal.text = "+$heavyBass"

                if (isUserInteractingWithSliders) {
                    return@runOnUiThread
                }

                isUpdatingFromHardware = true
                if (eqMode == MazdaAudioDspController.EQ_STANDARD) {
                    isCustomUserCurve = false
                    applyEqCurve(intArrayOf(7, 7, 7, 7, 7, 7, 7, 7), false)
                } else if (eqMode == MazdaAudioDspController.EQ_POP) {
                    isCustomUserCurve = false
                    applyEqCurve(intArrayOf(8, 9, 8, 7, 8, 9, 8, 7), false)
                } else if (eqMode == MazdaAudioDspController.EQ_ROCK) {
                    isCustomUserCurve = false
                    applyEqCurve(intArrayOf(11, 10, 6, 5, 7, 9, 11, 12), false)
                } else if (eqMode == MazdaAudioDspController.EQ_CLASSIC) {
                    isCustomUserCurve = false
                    applyEqCurve(intArrayOf(10, 9, 7, 7, 8, 9, 10, 10), false)
                } else if (eqMode == MazdaAudioDspController.EQ_JAZZ) {
                    isCustomUserCurve = false
                    applyEqCurve(intArrayOf(9, 9, 8, 9, 7, 8, 9, 10), false)
                } else if (!isCustomUserCurve) {
                    // Custom from hardware / external change: sync bands proportionally
                    eqSliders[0].progress = bass
                    eqSliders[1].progress = bass
                    eqSliders[2].progress = (bass + tenor) / 2
                    eqSliders[3].progress = tenor
                    eqSliders[4].progress = tenor
                    eqSliders[5].progress = (tenor + treble) / 2
                    eqSliders[6].progress = treble
                    eqSliders[7].progress = treble
                    for (i in 0 until 8) {
                        eqLabels[i].text = formatDb(eqSliders[i].progress)
                    }
                }
                updateProfileHighlight(eqMode)
                isUpdatingFromHardware = false
            }
        }

        override fun onLoudnessChanged(enabled: Boolean) {
            runOnUiThread {
                if (binding.switchLoudness.isChecked != enabled) {
                    binding.switchLoudness.isChecked = enabled
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMazdaAudioDspBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bindPageNav(binding.navBack)

        dspController = MazdaAudioDspController.getInstance(this)

        initViews()
        setupTabs()
        setupStageControls()
        setupEqualizerControls()
    }

    override fun onStart() {
        super.onStart()
        dspController.addListener(dspListener)
    }

    override fun onStop() {
        super.onStop()
        dspController.removeListener(dspListener)
    }

    private fun initViews() {
        eqSliders = arrayOf(
            binding.sliderEq0,
            binding.sliderEq1,
            binding.sliderEq2,
            binding.sliderEq3,
            binding.sliderEq4,
            binding.sliderEq5,
            binding.sliderEq6,
            binding.sliderEq7
        )

        eqLabels = arrayOf(
            binding.textEqVal0,
            binding.textEqVal1,
            binding.textEqVal2,
            binding.textEqVal3,
            binding.textEqVal4,
            binding.textEqVal5,
            binding.textEqVal6,
            binding.textEqVal7
        )

        profileButtons = arrayOf(
            binding.btnEqStandard,
            binding.btnEqPop,
            binding.btnEqRock,
            binding.btnEqClassic,
            binding.btnEqJazz
        )
    }

    private fun setupTabs() {
        binding.tabCabin.setOnClickListener { selectTab(isCabin = true) }
        binding.tabEqualizer.setOnClickListener { selectTab(isCabin = false) }
    }

    private fun selectTab(isCabin: Boolean) {
        val activeColor = androidx.core.content.ContextCompat.getColor(this, R.color.mz_accent)
        val inactiveColor = Color.TRANSPARENT
        val activeTextColor = androidx.core.content.ContextCompat.getColor(this, R.color.mz_text_on_accent)
        val inactiveTextColor = androidx.core.content.ContextCompat.getColor(this, R.color.mz_text_secondary)

        binding.pageCabin.visibility = if (isCabin) View.VISIBLE else View.GONE
        binding.pageEqualizer.visibility = if (isCabin) View.GONE else View.VISIBLE

        if (isCabin) {
            binding.tabCabin.backgroundTintList = ColorStateList.valueOf(activeColor)
            binding.tabCabin.setTextColor(activeTextColor)
            binding.tabEqualizer.backgroundTintList = ColorStateList.valueOf(inactiveColor)
            binding.tabEqualizer.setTextColor(inactiveTextColor)
        } else {
            binding.tabEqualizer.backgroundTintList = ColorStateList.valueOf(activeColor)
            binding.tabEqualizer.setTextColor(activeTextColor)
            binding.tabCabin.backgroundTintList = ColorStateList.valueOf(inactiveColor)
            binding.tabCabin.setTextColor(inactiveTextColor)
        }
    }

    private fun setupStageControls() {
        binding.btnStageCenter.setOnClickListener { dspController.setStageCenter() }
        binding.btnStageDriver.setOnClickListener { dspController.setStageDriver() }
        binding.btnStageCopilot.setOnClickListener { dspController.setStageCopilot() }
        binding.btnStageRear.setOnClickListener { dspController.setStageRear() }
        binding.btnStageReset.setOnClickListener { dspController.setStageCenter() }

        binding.soundStageView.onStagePositionChanged = { fader, balance ->
            dspController.setFader(fader)
            dspController.setBalance(balance)
            updateStageLabels(fader, balance)
        }
    }

    private fun updateStageLabels(fader: Int, balance: Int) {
        val fDiff = fader - MazdaAudioDspController.CENTER_STAGE
        val bDiff = balance - MazdaAudioDspController.CENTER_STAGE

        val fText = when {
            fDiff < 0 -> "Fader: Przód (${-fDiff})"
            fDiff > 0 -> "Fader: Tył (+$fDiff)"
            else -> "Fader: Środek (0)"
        }

        val bText = when {
            bDiff < 0 -> "Balance: Lewo (${-bDiff})"
            bDiff > 0 -> "Balance: Prawo (+$bDiff)"
            else -> "Balance: Środek (0)"
        }

        binding.textFaderStatus.text = fText
        binding.textBalanceStatus.text = bText
    }

    private fun setupEqualizerControls() {
        // Setup 8 sliders & steppers
        val minusButtons = arrayOf(
            binding.btnEq0Minus, binding.btnEq1Minus, binding.btnEq2Minus, binding.btnEq3Minus,
            binding.btnEq4Minus, binding.btnEq5Minus, binding.btnEq6Minus, binding.btnEq7Minus
        )
        val plusButtons = arrayOf(
            binding.btnEq0Plus, binding.btnEq1Plus, binding.btnEq2Plus, binding.btnEq3Plus,
            binding.btnEq4Plus, binding.btnEq5Plus, binding.btnEq6Plus, binding.btnEq7Plus
        )

        for (i in 0 until 8) {
            val slider = eqSliders[i]
            val label = eqLabels[i]

            slider.onTrackingTouchChanged = { _ ->
                val anyTracking = eqSliders.any { it.isTrackingTouch }
                isUserInteractingWithSliders = anyTracking
                if (!anyTracking && !isUpdatingFromHardware) {
                    isCustomUserCurve = true
                    dspController.setEqMode(MazdaAudioDspController.EQ_CUSTOM)
                    updateProfileHighlight(MazdaAudioDspController.EQ_CUSTOM)
                    pushEqToHardware()
                }
            }

            slider.onProgressChanged = { progress ->
                label.text = formatDb(progress)
                if (!isUpdatingFromHardware) {
                    isCustomUserCurve = true
                    dspController.setEqMode(MazdaAudioDspController.EQ_CUSTOM)
                    updateProfileHighlight(MazdaAudioDspController.EQ_CUSTOM)
                    if (!slider.isTrackingTouch) {
                        pushEqToHardware()
                    }
                }
            }

            minusButtons[i].setOnClickListener {
                isCustomUserCurve = true
                dspController.setEqMode(MazdaAudioDspController.EQ_CUSTOM)
                updateProfileHighlight(MazdaAudioDspController.EQ_CUSTOM)
                slider.progress = (slider.progress - 1).coerceAtLeast(0)
                pushEqToHardware()
            }
            plusButtons[i].setOnClickListener {
                isCustomUserCurve = true
                dspController.setEqMode(MazdaAudioDspController.EQ_CUSTOM)
                updateProfileHighlight(MazdaAudioDspController.EQ_CUSTOM)
                slider.progress = (slider.progress + 1).coerceAtMost(slider.max)
                pushEqToHardware()
            }
        }

        // Subwoofer slider & buttons
        binding.seekHeavyBass.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                binding.textHeavyBassVal.text = "+$progress"
                if (fromUser) {
                    dspController.setHeavyBass(progress)
                }
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        binding.btnSubMinus.setOnClickListener {
            val next = (dspController.heavyBass - 1).coerceAtLeast(0)
            dspController.setHeavyBass(next)
        }
        binding.btnSubPlus.setOnClickListener {
            val next = (dspController.heavyBass + 1).coerceAtMost(14)
            dspController.setHeavyBass(next)
        }

        // Loudness switch - use OnClickListener to prevent programmatic loop!
        binding.switchLoudness.setOnClickListener {
            dspController.setLoudness(binding.switchLoudness.isChecked)
        }

        // Reset EQ
        binding.btnResetEq.setOnClickListener {
            applyEqCurve(intArrayOf(7, 7, 7, 7, 7, 7, 7, 7), true)
            dspController.setHeavyBass(0)
            dspController.setEqMode(MazdaAudioDspController.EQ_STANDARD)
        }

        // Profile Presets
        binding.btnEqStandard.setOnClickListener {
            applyEqCurve(intArrayOf(7, 7, 7, 7, 7, 7, 7, 7), true)
            dspController.setEqMode(MazdaAudioDspController.EQ_STANDARD)
        }
        binding.btnEqPop.setOnClickListener {
            applyEqCurve(intArrayOf(8, 9, 8, 7, 8, 9, 8, 7), true)
            dspController.setEqMode(MazdaAudioDspController.EQ_POP)
        }
        binding.btnEqRock.setOnClickListener {
            applyEqCurve(intArrayOf(11, 10, 6, 5, 7, 9, 11, 12), true)
            dspController.setEqMode(MazdaAudioDspController.EQ_ROCK)
        }
        binding.btnEqClassic.setOnClickListener {
            applyEqCurve(intArrayOf(10, 9, 7, 7, 8, 9, 10, 10), true)
            dspController.setEqMode(MazdaAudioDspController.EQ_CLASSIC)
        }
        binding.btnEqJazz.setOnClickListener {
            applyEqCurve(intArrayOf(9, 9, 8, 9, 7, 8, 9, 10), true)
            dspController.setEqMode(MazdaAudioDspController.EQ_JAZZ)
        }
    }

    private fun applyEqCurve(curve: IntArray, pushToHardware: Boolean) {
        for (i in 0 until 8) {
            eqSliders[i].progress = curve[i]
            eqLabels[i].text = formatDb(curve[i])
        }
        if (pushToHardware) {
            pushEqToHardware()
        }
    }

    private fun pushEqToHardware() {
        val bass = ((eqSliders[0].progress + eqSliders[1].progress + eqSliders[2].progress) / 3)
        val tenor = ((eqSliders[3].progress + eqSliders[4].progress) / 2)
        val treble = ((eqSliders[5].progress + eqSliders[6].progress + eqSliders[7].progress) / 3)

        dspController.setBass(bass)
        dspController.setTenor(tenor)
        dspController.setTreble(treble)
    }

    private fun updateProfileHighlight(mode: Int) {
        val activeBg = androidx.core.content.ContextCompat.getColor(this, R.color.mz_accent)
        val inactiveBg = androidx.core.content.ContextCompat.getColor(this, R.color.mz_surface_interactive)
        val activeText = androidx.core.content.ContextCompat.getColor(this, R.color.mz_text_on_accent)
        val inactiveText = androidx.core.content.ContextCompat.getColor(this, R.color.mz_text_secondary)

        val modes = intArrayOf(
            MazdaAudioDspController.EQ_STANDARD,
            MazdaAudioDspController.EQ_POP,
            MazdaAudioDspController.EQ_ROCK,
            MazdaAudioDspController.EQ_CLASSIC,
            MazdaAudioDspController.EQ_JAZZ
        )

        for (i in profileButtons.indices) {
            val btn = profileButtons[i]
            val isCurrent = (modes[i] == mode)
            btn.backgroundTintList = ColorStateList.valueOf(if (isCurrent) activeBg else inactiveBg)
            btn.setTextColor(if (isCurrent) activeText else inactiveText)
        }
    }

    private fun formatDb(raw: Int): String {
        val db = raw - MazdaAudioDspController.CENTER_EQ
        return if (db > 0) "+$db dB" else "$db dB"
    }
}
