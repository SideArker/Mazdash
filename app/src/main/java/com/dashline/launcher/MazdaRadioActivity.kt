// Copyright (C) 2026 Mazdash Project
// SPDX-License-Identifier: GPL-3.0-only

package com.dashline.launcher

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dashline.launcher.databinding.ActivityMazdaRadioBinding
import com.nwd.radio.service.data.Frequency

class MazdaRadioActivity : AppCompatActivity(), MazdaRadioController.Listener {

    private lateinit var binding: ActivityMazdaRadioBinding
    private lateinit var controller: MazdaRadioController
    private lateinit var prefs: Prefs

    private lateinit var presetTiles: List<View>
    private lateinit var presetFreqs: List<TextView>
    private lateinit var presetNames: List<TextView>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMazdaRadioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        controller = MazdaRadioController.getInstance(this)
        prefs = Prefs(this)

        presetTiles = listOf(
            binding.preset1, binding.preset2, binding.preset3,
            binding.preset4, binding.preset5, binding.preset6
        )
        presetFreqs = listOf(
            binding.p1Freq, binding.p2Freq, binding.p3Freq,
            binding.p4Freq, binding.p5Freq, binding.p6Freq
        )
        presetNames = listOf(
            binding.p1Name, binding.p2Name, binding.p3Name,
            binding.p4Name, binding.p5Name, binding.p6Name
        )

        setupNavigation()
        setupControls()
        setupPresets()
    }

    private fun setupNavigation() {
        binding.navBack.setOnClickListener { finish() }
        binding.navHome.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java).apply {
                action = Intent.ACTION_MAIN
                addCategory(Intent.CATEGORY_HOME)
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            }
            startActivity(intent)
        }
        binding.btnOpenDsp.setOnClickListener {
            startActivity(Intent(this, MazdaAudioDspActivity::class.java))
        }
        binding.btnOpenBluetooth.setOnClickListener {
            startActivity(Intent(this, MazdaBluetoothActivity::class.java))
        }
    }

    private fun setupControls() {
        binding.btnBandToggle.setOnClickListener {
            controller.changeBand()
        }
        binding.btnSeekDown.setOnClickListener {
            controller.seek(false)
        }
        binding.btnSeekUp.setOnClickListener {
            controller.seek(true)
        }
        binding.btnStepDown.setOnClickListener {
            controller.stepTune(false)
        }
        binding.btnStepUp.setOnClickListener {
            controller.stepTune(true)
        }
        binding.btnAmsScan.setOnClickListener {
            controller.startAms()
            Toast.makeText(this, "Skanowanie pasma (AMS)...", Toast.LENGTH_SHORT).show()
        }

        // Visualizer
        binding.radioVisualizer.visibility = if (prefs.visualizerEnabled) View.VISIBLE else View.GONE
        binding.radioVisualizer.isPlaying = true
    }

    private fun setupPresets() {
        for (i in presetTiles.indices) {
            val tile = presetTiles[i]
            tile.setOnClickListener {
                controller.selectPreset(i)
            }
            tile.setOnLongClickListener {
                controller.savePreset(i)
                Toast.makeText(this, "Zapisano stację w P${i + 1}", Toast.LENGTH_SHORT).show()
                true
            }
        }
    }

    override fun onResume() {
        super.onResume()
        controller.addListener(this)
        controller.bindService()
    }

    override fun onPause() {
        controller.removeListener(this)
        super.onPause()
    }

    override fun onFrequencyChanged(band: Byte, frequency: Int, psName: String?) {
        binding.textRadioBand.text = MazdaRadioController.bandName(band)
        val isAm = MazdaRadioController.isAmBand(band)
        binding.textFreqUnit.text = if (isAm) "kHz" else "MHz"

        if (isAm) {
            binding.textFrequency.text = frequency.toString()
        } else {
            val mhz = if (frequency > 50000) frequency / 1000f else frequency / 100f
            binding.textFrequency.text = String.format(java.util.Locale.US, "%.2f", mhz)
        }

        val name = if (!psName.isNullOrEmpty()) psName else if (isAm) "AM Radio" else "Radio FM"
        binding.textStationName.text = name

        highlightActivePreset(frequency)
    }

    override fun onPresetsUpdated(presets: List<Frequency>) {
        for (i in presetTiles.indices) {
            if (i < presets.size) {
                val p = presets[i]
                val isAm = MazdaRadioController.isAmBand(p.bandType)
                if (isAm) {
                    presetFreqs[i].text = p.frequency.toString()
                } else {
                    val mhz = if (p.frequency > 50000) p.frequency / 1000f else p.frequency / 100f
                    presetFreqs[i].text = String.format(java.util.Locale.US, "%.2f", mhz)
                }
                val name = if (!p.psName.isNullOrEmpty()) p.psName else "P${i + 1}"
                presetNames[i].text = "P${i + 1} - $name"
            }
        }
        highlightActivePreset(controller.currentFrequency)
    }

    override fun onStereoChanged(isStereo: Boolean) {
        binding.textStereoBadge.visibility = if (isStereo) View.VISIBLE else View.INVISIBLE
    }

    override fun onRtMessage(message: String) {
        binding.textRtMessage.text = if (message.isNotEmpty()) message else binding.textStationName.text
    }

    override fun onRadioState(state: Byte) {
        // State updates
    }

    private fun highlightActivePreset(freq: Int) {
        val presets = controller.presets
        for (i in presetTiles.indices) {
            val isCurrent = i < presets.size && presets[i].frequency == freq
            presetTiles[i].isActivated = isCurrent
        }
    }
}
