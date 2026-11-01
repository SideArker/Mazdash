// Copyright (C) 2026 Mazdash Project
// SPDX-License-Identifier: GPL-3.0-only

package com.dashline.launcher

import android.os.Bundle
import android.widget.SeekBar
import com.dashline.launcher.databinding.ActivityMazdaAudioDspBinding

class MazdaAudioDspActivity : BaseActivity() {

    private lateinit var binding: ActivityMazdaAudioDspBinding
    private lateinit var dspController: MazdaAudioDspController

    private val dspListener = object : MazdaAudioDspController.Listener {
        override fun onStageChanged(fader: Int, balance: Int) {
            runOnUiThread {
                binding.soundStageView.fader = fader
                binding.soundStageView.balance = balance
            }
        }

        override fun onEqualizerChanged(bass: Int, tenor: Int, treble: Int, heavyBass: Int, eqMode: Int) {
            runOnUiThread {
                binding.seekBass.progress = bass
                binding.seekTenor.progress = tenor
                binding.seekTreble.progress = treble
                binding.seekHeavyBass.progress = heavyBass

                binding.textBassVal.text = formatDb(bass)
                binding.textTenorVal.text = formatDb(tenor)
                binding.textTrebleVal.text = formatDb(treble)
                binding.textHeavyBassVal.text = heavyBass.toString()
            }
        }

        override fun onLoudnessChanged(enabled: Boolean) {
            runOnUiThread {
                binding.switchLoudness.isChecked = enabled
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMazdaAudioDspBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.header.pageTitle.setText(R.string.mazda_dsp_title)
        bindPageNav(binding.header.navBack, binding.header.navHome)

        dspController = MazdaAudioDspController.getInstance(this)

        // Stage Preset Buttons
        binding.btnStageCenter.setOnClickListener { dspController.setStageCenter() }
        binding.btnStageDriver.setOnClickListener { dspController.setStageDriver() }
        binding.btnStageCopilot.setOnClickListener { dspController.setStageCopilot() }
        binding.btnStageRear.setOnClickListener { dspController.setStageRear() }

        // Interactive Stage Touch
        binding.soundStageView.onStagePositionChanged = { fader, balance ->
            dspController.setFader(fader)
            dspController.setBalance(balance)
        }

        // SeekBars
        binding.seekBass.setOnSeekBarChangeListener(createSeekListener { dspController.setBass(it) })
        binding.seekTenor.setOnSeekBarChangeListener(createSeekListener { dspController.setTenor(it) })
        binding.seekTreble.setOnSeekBarChangeListener(createSeekListener { dspController.setTreble(it) })
        binding.seekHeavyBass.setOnSeekBarChangeListener(createSeekListener { dspController.setHeavyBass(it) })

        // Loudness switch
        binding.switchLoudness.setOnCheckedChangeListener { _, isChecked ->
            dspController.setLoudness(isChecked)
        }

        // EQ Presets
        binding.btnEqStandard.setOnClickListener { dspController.setEqMode(MazdaAudioDspController.EQ_STANDARD) }
        binding.btnEqPop.setOnClickListener { dspController.setEqMode(MazdaAudioDspController.EQ_POP) }
        binding.btnEqRock.setOnClickListener { dspController.setEqMode(MazdaAudioDspController.EQ_ROCK) }
        binding.btnEqClassic.setOnClickListener { dspController.setEqMode(MazdaAudioDspController.EQ_CLASSIC) }
        binding.btnEqJazz.setOnClickListener { dspController.setEqMode(MazdaAudioDspController.EQ_JAZZ) }
    }

    override fun onStart() {
        super.onStart()
        dspController.addListener(dspListener)
    }

    override fun onStop() {
        super.onStop()
        dspController.removeListener(dspListener)
    }

    private fun formatDb(raw: Int): String {
        val db = raw - MazdaAudioDspController.CENTER_EQ
        return if (db > 0) "+$db" else db.toString()
    }

    private fun createSeekListener(onChanged: (Int) -> Unit): SeekBar.OnSeekBarChangeListener {
        return object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    onChanged(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        }
    }
}
