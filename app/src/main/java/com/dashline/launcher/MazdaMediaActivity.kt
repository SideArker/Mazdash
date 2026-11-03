// Copyright (C) 2026 Mazdash Project
// SPDX-License-Identifier: GPL-3.0-only

package com.dashline.launcher

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import com.dashline.launcher.databinding.ActivityMazdaMediaBinding

class MazdaMediaActivity : BaseActivity() {

    private lateinit var binding: ActivityMazdaMediaBinding
    private lateinit var btController: MazdaBluetoothController
    private lateinit var prefs: Prefs
    private var mediaMonitor: MediaMonitor? = null
    private var currentMediaInfo: MediaInfo? = null

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
        bindPageNav(binding.navBack)

        // 20-bar visualizer for rich audio display
        binding.largeVisualizer.barCount = 20

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mediaMonitor = MediaMonitor(this) { info ->
                runOnUiThread {
                    currentMediaInfo = info
                    updateMediaUI()
                }
            }
        }

        // Top shortcuts
        binding.btnOpenDsp.setOnClickListener {
            startActivity(Intent(this, MazdaAudioDspActivity::class.java))
        }
        binding.btnOpenBluetooth.setOnClickListener {
            startActivity(Intent(this, MazdaBluetoothActivity::class.java))
        }

        // Playback controls
        binding.btnPlayPause.setOnClickListener {
            val local = currentMediaInfo
            if (local != null && local.isPlaying) {
                mediaMonitor?.playPause()
            } else if (btController.isConnected) {
                btController.playPause()
            } else if (local != null) {
                mediaMonitor?.playPause()
            } else {
                btController.playPause()
            }
        }
        binding.btnPrev.setOnClickListener {
            if (currentMediaInfo != null) {
                mediaMonitor?.previous()
            } else {
                btController.previousTrack()
            }
        }
        binding.btnNext.setOnClickListener {
            if (currentMediaInfo != null) {
                mediaMonitor?.next()
            } else {
                btController.nextTrack()
            }
        }

        // Pair button in disconnected prompt
        binding.btnPairPhone.setOnClickListener {
            startActivity(Intent(this, MazdaBluetoothActivity::class.java))
        }
    }

    override fun onStart() {
        super.onStart()
        btController.addListener(btListener)
        btController.queryMusicId3()
        mediaMonitor?.start()
        updateMediaUI()
    }

    override fun onStop() {
        super.onStop()
        mediaMonitor?.stop()
        btController.removeListener(btListener)
    }

    private fun updateMediaUI() {
        val local = currentMediaInfo
        val isBtConnected = btController.isConnected
        val isBtPlaying = btController.isMusicPlaying

        val hasContent = (local != null) || isBtConnected || isBtPlaying

        if (hasContent) {
            binding.layoutPlaying.visibility = View.VISIBLE
            binding.layoutNotConnected.visibility = View.GONE

            val title: String
            val artist: String
            val album: String
            val sourceText: String
            val isPlaying: Boolean

            if (local != null && (local.isPlaying || !isBtConnected)) {
                title = local.title.ifEmpty { "Utwór audio" }
                artist = local.artist.ifEmpty { "Nieznany wykonawca" }
                album = local.album.ifEmpty { "Album domyślny" }
                val appLabel = AppRepository.labelFor(this, local.packageName) ?: "Odtwarzacz"
                sourceText = "$appLabel • System Audio"
                isPlaying = local.isPlaying

                if (local.art != null) {
                    binding.imageAlbumArt.setImageBitmap(local.art)
                    binding.imageAlbumArt.imageTintList = null
                    binding.imageAlbumArt.setPadding(0, 0, 0, 0)

                    binding.imageMediaBackground.setImageBitmap(local.art)
                    binding.imageMediaBackground.visibility = View.VISIBLE
                    binding.imageMediaOverlay.visibility = View.VISIBLE
                } else {
                    val accentColor = androidx.core.content.ContextCompat.getColor(this, R.color.mz_accent)
                    binding.imageAlbumArt.setImageResource(R.drawable.ic_audio)
                    binding.imageAlbumArt.imageTintList = ColorStateList.valueOf(accentColor)
                    val p = (16 * resources.displayMetrics.density).toInt()
                    binding.imageAlbumArt.setPadding(p, p, p, p)

                    binding.imageMediaBackground.visibility = View.GONE
                    binding.imageMediaOverlay.visibility = View.GONE
                }
            } else {
                title = btController.trackTitle.ifEmpty { "Bluetooth Audio" }
                artist = btController.trackArtist.ifEmpty { btController.connectedDeviceName.orEmpty() }
                album = "Transmisja Bluetooth A2DP"
                val devName = btController.connectedDeviceName ?: "Telefon"
                sourceText = "Bluetooth Audio • $devName"
                isPlaying = isBtPlaying

                val accentColor = androidx.core.content.ContextCompat.getColor(this, R.color.mz_accent)
                binding.imageAlbumArt.setImageResource(R.drawable.ic_audio)
                binding.imageAlbumArt.imageTintList = ColorStateList.valueOf(accentColor)
                val p = (16 * resources.displayMetrics.density).toInt()
                binding.imageAlbumArt.setPadding(p, p, p, p)

                binding.imageMediaBackground.visibility = View.GONE
                binding.imageMediaOverlay.visibility = View.GONE
            }

            binding.textTrackTitle.text = title
            binding.textTrackArtist.text = artist
            binding.textAlbumName.text = album
            binding.textMediaSource.text = sourceText

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
            binding.btnPlayPause.setColorFilter(Color.WHITE, android.graphics.PorterDuff.Mode.SRC_IN)
        } else {
            binding.layoutPlaying.visibility = View.GONE
            binding.layoutNotConnected.visibility = View.VISIBLE
            binding.imageMediaBackground.visibility = View.GONE
            binding.imageMediaOverlay.visibility = View.GONE
        }
    }
}
