// Copyright (C) 2026 Mazdash Project
// SPDX-License-Identifier: GPL-3.0-only

package com.dashline.launcher

import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat

/**
 * Native Mazda Floating Call HUD Banner.
 *
 * Appears seamlessly at the top center of the screen when a phone call is incoming or active.
 * Replaces the stock clunky BtFloatingWindow (com.nwd.android.phone) with a sleek,
 * automotive-grade Mazda Soul Red & dark stealth capsule HUD.
 */
class MazdaCallHud(private val context: Context) : MazdaPhoneController.CallListener {

    companion object {
        private const val TAG = "MazdaCallHud"
    }

    private val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    private val mainHandler = Handler(Looper.getMainLooper())
    private val phoneController = MazdaPhoneController.getInstance(context)
    private val prefs = Prefs(context)

    private var hudView: View? = null
    private var windowParams: WindowManager.LayoutParams? = null
    var isShowing: Boolean = false
        private set

    var onVisibilityChanged: ((Boolean) -> Unit)? = null

    init {
        phoneController.addListener(this)
    }

    override fun onCallStateChanged(state: MazdaPhoneController.CallState, number: String, name: String?) {
        mainHandler.post {
            if (MazdaPhoneActivity.isForeground) {
                hide()
                return@post
            }

            if (!prefs.customCallHudEnabled) {
                hide()
                return@post
            }

            when (state) {
                MazdaPhoneController.CallState.INCOMING -> {
                    ensureViewCreated()
                    showIncomingCall(number, name)
                    show()
                }
                MazdaPhoneController.CallState.DIALING -> {
                    ensureViewCreated()
                    showDialingCall(number, name)
                    show()
                }
                MazdaPhoneController.CallState.ACTIVE -> {
                    ensureViewCreated()
                    showActiveCall(number, name)
                    show()
                }
                MazdaPhoneController.CallState.IDLE -> {
                    hide()
                }
            }
        }
    }

    override fun onCallDurationTick(seconds: Int) {
        if (isShowing && phoneController.callState == MazdaPhoneController.CallState.ACTIVE) {
            mainHandler.post {
                val view = hudView ?: return@post
                val tvStatus = view.findViewById<TextView>(R.id.callHudStatus)
                val routeSuffix = if (phoneController.isVoiceAtCar) "" else " • " + context.getString(R.string.call_hud_route_phone)
                tvStatus.text = String.format("%s (%02d:%02d)%s", context.getString(R.string.call_hud_active), seconds / 60, seconds % 60, routeSuffix)
            }
        }
    }

    override fun onContactsUpdated(contacts: List<MazdaPhoneController.ContactItem>) {}
    override fun onCallLogsUpdated(logs: List<MazdaPhoneController.CallLogItem>) {}

    override fun onAudioChannelChanged(isCarSpeaker: Boolean) {
        mainHandler.post {
            val view = hudView ?: return@post
            val btnRoute = view.findViewById<TextView>(R.id.btnCallHudAudioRoute)
            btnRoute.text = if (isCarSpeaker) context.getString(R.string.call_hud_route_car) else context.getString(R.string.call_hud_route_phone)
        }
    }

    private fun ensureViewCreated() {
        if (hudView != null) return

        val density = context.resources.displayMetrics.density
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.layout_mazda_call_hud, null)

        val windowType = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            @Suppress("DEPRECATION")
            WindowManager.LayoutParams.TYPE_PHONE
        }

        val params = WindowManager.LayoutParams(
            (520 * density).toInt(),
            WindowManager.LayoutParams.WRAP_CONTENT,
            windowType,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
            x = 0
            y = (14 * density).toInt()
        }

        val infoContainer = view.findViewById<View>(R.id.callHudInfoContainer)
        val btnAnswer = view.findViewById<View>(R.id.btnCallHudAnswer)
        val btnDecline = view.findViewById<View>(R.id.btnCallHudDecline)
        val btnHangup = view.findViewById<View>(R.id.btnCallHudHangup)
        val btnRoute = view.findViewById<TextView>(R.id.btnCallHudAudioRoute)

        infoContainer.setOnClickListener {
            openPhoneActivity()
        }

        btnAnswer.setOnClickListener {
            phoneController.answerCall()
        }

        btnDecline.setOnClickListener {
            phoneController.hangUp()
        }

        btnHangup.setOnClickListener {
            phoneController.hangUp()
        }

        btnRoute.setOnClickListener {
            phoneController.toggleAudioRoute()
        }

        hudView = view
        windowParams = params
    }

    private fun showIncomingCall(number: String, name: String?) {
        val view = hudView ?: return
        val tvName = view.findViewById<TextView>(R.id.callHudName)
        val tvStatus = view.findViewById<TextView>(R.id.callHudStatus)
        val actionsIncoming = view.findViewById<View>(R.id.callHudActionsIncoming)
        val actionsActive = view.findViewById<View>(R.id.callHudActionsActive)
        val avatar = view.findViewById<FrameLayout>(R.id.callHudAvatarContainer)
        val icon = view.findViewById<ImageView>(R.id.callHudIcon)

        tvName.text = name ?: (if (number.isNotEmpty()) number else "Nieznany")
        tvStatus.text = context.getString(R.string.call_hud_incoming)
        tvStatus.setTextColor(ContextCompat.getColor(context, R.color.mz_status_connected))

        avatar.setBackgroundResource(R.drawable.btn_pill_call_answer)
        icon.setImageResource(R.drawable.ic_call)

        actionsIncoming.visibility = View.VISIBLE
        actionsActive.visibility = View.GONE
    }

    private fun showDialingCall(number: String, name: String?) {
        val view = hudView ?: return
        val tvName = view.findViewById<TextView>(R.id.callHudName)
        val tvStatus = view.findViewById<TextView>(R.id.callHudStatus)
        val actionsIncoming = view.findViewById<View>(R.id.callHudActionsIncoming)
        val actionsActive = view.findViewById<View>(R.id.callHudActionsActive)
        val avatar = view.findViewById<FrameLayout>(R.id.callHudAvatarContainer)
        val icon = view.findViewById<ImageView>(R.id.callHudIcon)

        tvName.text = name ?: (if (number.isNotEmpty()) number else "Nieznany")
        tvStatus.text = context.getString(R.string.call_hud_dialing)
        tvStatus.setTextColor(ContextCompat.getColor(context, R.color.mz_accent))

        avatar.setBackgroundResource(R.drawable.btn_pill_call_decline)
        icon.setImageResource(R.drawable.ic_call)

        actionsIncoming.visibility = View.GONE
        actionsActive.visibility = View.VISIBLE
    }

    private fun showActiveCall(number: String, name: String?) {
        val view = hudView ?: return
        val tvName = view.findViewById<TextView>(R.id.callHudName)
        val tvStatus = view.findViewById<TextView>(R.id.callHudStatus)
        val actionsIncoming = view.findViewById<View>(R.id.callHudActionsIncoming)
        val actionsActive = view.findViewById<View>(R.id.callHudActionsActive)
        val avatar = view.findViewById<FrameLayout>(R.id.callHudAvatarContainer)
        val icon = view.findViewById<ImageView>(R.id.callHudIcon)
        val btnRoute = view.findViewById<TextView>(R.id.btnCallHudAudioRoute)

        tvName.text = name ?: (if (number.isNotEmpty()) number else "Nieznany")
        val seconds = phoneController.callDurationSeconds
        val routeSuffix = if (phoneController.isVoiceAtCar) "" else " • " + context.getString(R.string.call_hud_route_phone)
        tvStatus.text = String.format("%s (%02d:%02d)%s", context.getString(R.string.call_hud_active), seconds / 60, seconds % 60, routeSuffix)
        tvStatus.setTextColor(ContextCompat.getColor(context, R.color.mz_text_secondary))

        avatar.setBackgroundResource(R.drawable.btn_pill_call_answer)
        icon.setImageResource(R.drawable.ic_call)

        btnRoute.text = if (phoneController.isVoiceAtCar) context.getString(R.string.call_hud_route_car) else context.getString(R.string.call_hud_route_phone)

        actionsIncoming.visibility = View.GONE
        actionsActive.visibility = View.VISIBLE
    }

    private fun show() {
        val view = hudView ?: return
        val params = windowParams ?: return
        if (isShowing) return

        try {
            val density = context.resources.displayMetrics.density
            view.alpha = 0f
            view.translationY = -30f * density

            windowManager.addView(view, params)
            isShowing = true
            onVisibilityChanged?.invoke(true)

            view.animate()
                .alpha(1f)
                .translationY(0f)
                .setDuration(220)
                .start()
        } catch (e: Exception) {
            Log.e(TAG, "Failed adding call HUD to WindowManager: ${e.message}")
        }
    }

    private fun hide() {
        val view = hudView ?: return
        if (!isShowing) return

        val density = context.resources.displayMetrics.density
        view.animate()
            .alpha(0f)
            .translationY(-30f * density)
            .setDuration(200)
            .withEndAction {
                try {
                    if (isShowing) {
                        windowManager.removeView(view)
                        isShowing = false
                        onVisibilityChanged?.invoke(false)
                    }
                } catch (e: Exception) {
                    Log.w(TAG, "Failed removing call HUD: ${e.message}")
                }
            }
            .start()
    }

    private fun openPhoneActivity() {
        try {
            val intent = Intent(context, MazdaPhoneActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            Log.w(TAG, "Failed launching MazdaPhoneActivity: ${e.message}")
        }
    }

    fun destroy() {
        if (isShowing && hudView != null) {
            try {
                windowManager.removeView(hudView)
            } catch (e: Exception) {
                // ignore
            }
            isShowing = false
            onVisibilityChanged?.invoke(false)
        }
        phoneController.removeListener(this)
    }
}
