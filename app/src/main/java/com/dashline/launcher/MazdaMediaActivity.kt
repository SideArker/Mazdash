// Copyright (C) 2026 Mazdash Project
// SPDX-License-Identifier: GPL-3.0-only

package com.dashline.launcher

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.dashline.launcher.databinding.ActivityMazdaMediaBinding

class MazdaMediaActivity : BaseActivity() {

    private lateinit var binding: ActivityMazdaMediaBinding
    private lateinit var btController: MazdaBluetoothController
    private lateinit var prefs: Prefs

    private val btListener = object : MazdaBluetoothController.Listener {
        override fun onConnectionChanged(connected: Boolean, deviceName: String?, deviceAddress: String?) {
            runOnUiThread { updateMediaUI() }
        }

        override fun onPlaybackChanged(isPlaying: Boolean, title: String, artist: String) {
            runOnUiThread { updateMediaUI() }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMazdaMediaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prefs = Prefs(this)
        btController = MazdaBluetoothController.getInstance(this)

        binding.pageTitle.setText(R.string.mazda_media_title)
        bindPageNav(binding.navBack, binding.navHome)

        // Top shortcuts
        binding.btnOpenDsp.setOnClickListener {
            startActivity(Intent(this, MazdaAudioDspActivity::class.java))
        }
        binding.btnOpenBluetooth.setOnClickListener {
            startActivity(Intent(this, MazdaBluetoothActivity::class.java))
        }

        // Playback controls
        binding.btnPlayPause.setOnClickListener { btController.playPause() }
        binding.btnPrev.setOnClickListener { btController.previousTrack() }
        binding.btnNext.setOnClickListener { btController.nextTrack() }

        // Pair button in disconnected prompt
        binding.btnPairPhone.setOnClickListener {
            startActivity(Intent(this, MazdaBluetoothActivity::class.java))
        }
    }

    override fun onStart() {
        super.onStart()
        btController.addListener(btListener)
        btController.queryMusicId3()
        updateMediaUI()
    }

    override fun onStop() {
        super.onStop()
        btController.removeListener(btListener)
    }

    private fun updateMediaUI() {
        val isConnected = btController.isConnected
        val isPlaying = btController.isMusicPlaying
        val hasContent = isConnected || isPlaying

        if (hasContent) {
            binding.layoutPlaying.visibility = View.VISIBLE
            binding.layoutNotConnected.visibility = View.GONE

            val title = btController.trackTitle.ifEmpty { "Bluetooth Audio" }
            val artist = btController.trackArtist.ifEmpty { btController.connectedDeviceName.orEmpty() }
            val devName = btController.connectedDeviceName ?: "Telefon"

            binding.textMediaTitle.text = title
            binding.textMediaArtist.text = artist
            binding.textMediaSource.text = "Bluetooth Audio • $devName"

            // Visualizer
            if (prefs.visualizerEnabled) {
                binding.largeVisualizer.visibility = View.VISIBLE
                binding.largeVisualizer.isPlaying = isPlaying
            } else {
                binding.largeVisualizer.visibility = View.GONE
            }

            // Play/Pause icon
            binding.btnPlayPause.setImageResource(
                if (isPlaying) R.drawable.ic_pause else R.drawable.ic_play
            )
        } else {
            binding.layoutPlaying.visibility = View.GONE
            binding.layoutNotConnected.visibility = View.VISIBLE
        }
    }
}
