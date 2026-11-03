// Copyright (C) 2026 Mazdash Project
// SPDX-License-Identifier: GPL-3.0-only

package com.dashline.launcher

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

/**
 * Interactive 2D Sound Stage / Cockpit View for Mazda Headunit.
 *
 * Renders the car cabin with seats (R.drawable.che) and lets the driver
 * drag or tap a Soul Red target crosshair to position Fader & Balance.
 */
class MazdaSoundStageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val bgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = androidx.core.content.ContextCompat.getColor(context, R.color.mz_bg_screen)
        style = Paint.Style.FILL
    }

    private val gridPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = androidx.core.content.ContextCompat.getColor(context, R.color.mz_border)
        strokeWidth = 2f * resources.displayMetrics.density
        style = Paint.Style.STROKE
    }

    private val cabinBitmap: Bitmap? = try {
        BitmapFactory.decodeResource(resources, R.drawable.che)
    } catch (e: Exception) {
        null
    }

    private val bitmapPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        isFilterBitmap = true
    }

    private val speakerPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = androidx.core.content.ContextCompat.getColor(context, R.color.mz_surface_interactive_hover)
        style = Paint.Style.FILL
    }

    private val targetPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = androidx.core.content.ContextCompat.getColor(context, R.color.mz_accent)
        style = Paint.Style.FILL
    }

    private val targetGlowPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = androidx.core.content.ContextCompat.getColor(context, R.color.mz_accent_glow)
        style = Paint.Style.FILL
    }

    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = androidx.core.content.ContextCompat.getColor(context, R.color.mz_text_secondary)
        textSize = 12f * resources.displayMetrics.scaledDensity
        textAlign = Paint.Align.CENTER
    }

    var fader: Int = MazdaAudioDspController.CENTER_STAGE
        set(value) {
            field = value.coerceIn(0, MazdaAudioDspController.MAX_STAGE)
            invalidate()
        }

    var balance: Int = MazdaAudioDspController.CENTER_STAGE
        set(value) {
            field = value.coerceIn(0, MazdaAudioDspController.MAX_STAGE)
            invalidate()
        }

    var onStagePositionChanged: ((fader: Int, balance: Int) -> Unit)? = null

    private val boundsRect = RectF()
    private val srcRect = Rect()
    private val dstRect = RectF()

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val w = width.toFloat()
        val h = height.toFloat()
        if (w <= 0 || h <= 0) return

        val pad = 12f * resources.displayMetrics.density
        boundsRect.set(pad, pad, w - pad, h - pad)

        val corner = 14f * resources.displayMetrics.density
        canvas.drawRoundRect(boundsRect, corner, corner, bgPaint)
        canvas.drawRoundRect(boundsRect, corner, corner, gridPaint)

        // Center crosshair lines
        val cx = boundsRect.centerX()
        val cy = boundsRect.centerY()
        canvas.drawLine(cx, boundsRect.top + 16, cx, boundsRect.bottom - 16, gridPaint)
        canvas.drawLine(boundsRect.left + 16, cy, boundsRect.right - 16, cy, gridPaint)

        // Draw car cabin bitmap (seats, dashboard)
        cabinBitmap?.let { bmp ->
            val scale = (boundsRect.height() * 0.85f) / bmp.height.toFloat()
            val drawW = bmp.width * scale
            val drawH = bmp.height * scale
            val left = cx - (drawW / 2f)
            val top = cy - (drawH / 2f)
            srcRect.set(0, 0, bmp.width, bmp.height)
            dstRect.set(left, top, left + drawW, top + drawH)
            canvas.drawBitmap(bmp, srcRect, dstRect, bitmapPaint)
        }

        // Labels
        canvas.drawText("PRZÓD (FRONT)", cx, boundsRect.top + 22f, textPaint)
        canvas.drawText("TYŁ (REAR)", cx, boundsRect.bottom - 10f, textPaint)

        // 4 Speaker positions
        val spkRadius = 7f * resources.displayMetrics.density
        val spkOffset = 30f * resources.displayMetrics.density
        canvas.drawCircle(boundsRect.left + spkOffset, boundsRect.top + spkOffset, spkRadius, speakerPaint)
        canvas.drawCircle(boundsRect.right - spkOffset, boundsRect.top + spkOffset, spkRadius, speakerPaint)
        canvas.drawCircle(boundsRect.left + spkOffset, boundsRect.bottom - spkOffset, spkRadius, speakerPaint)
        canvas.drawCircle(boundsRect.right - spkOffset, boundsRect.bottom - spkOffset, spkRadius, speakerPaint)

        // Target Disc (Soul Red)
        val fracX = balance.toFloat() / MazdaAudioDspController.MAX_STAGE.toFloat()
        val fracY = fader.toFloat() / MazdaAudioDspController.MAX_STAGE.toFloat()

        val activeWidth = boundsRect.width() - (spkOffset * 1.6f)
        val activeHeight = boundsRect.height() - (spkOffset * 1.6f)
        val startX = boundsRect.left + (spkOffset * 0.8f)
        val startY = boundsRect.top + (spkOffset * 0.8f)

        val targetX = startX + (fracX * activeWidth)
        val targetY = startY + (fracY * activeHeight)

        val glowRadius = 22f * resources.displayMetrics.density
        val targetRadius = 9f * resources.displayMetrics.density

        canvas.drawCircle(targetX, targetY, glowRadius, targetGlowPaint)
        canvas.drawCircle(targetX, targetY, targetRadius, targetPaint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                parent?.requestDisallowInterceptTouchEvent(true)
                updateStageTouch(event.x, event.y)
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                parent?.requestDisallowInterceptTouchEvent(true)
                updateStageTouch(event.x, event.y)
                return true
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                parent?.requestDisallowInterceptTouchEvent(false)
                return true
            }
        }
        return super.onTouchEvent(event)
    }

    private fun updateStageTouch(touchX: Float, touchY: Float) {
        val pad = 12f * resources.displayMetrics.density
        val spkOffset = 30f * resources.displayMetrics.density
        val startX = pad + (spkOffset * 0.8f)
        val startY = pad + (spkOffset * 0.8f)
        val activeWidth = (width - 2 * pad) - (spkOffset * 1.6f)
        val activeHeight = (height - 2 * pad) - (spkOffset * 1.6f)

        if (activeWidth > 0 && activeHeight > 0) {
            val rawFracX = ((touchX - startX) / activeWidth).coerceIn(0f, 1f)
            val rawFracY = ((touchY - startY) / activeHeight).coerceIn(0f, 1f)

            val newBalance = Math.round(rawFracX * MazdaAudioDspController.MAX_STAGE).toInt()
            val newFader = Math.round(rawFracY * MazdaAudioDspController.MAX_STAGE).toInt()

            if (newBalance != balance || newFader != fader) {
                balance = newBalance
                fader = newFader
                onStagePositionChanged?.invoke(fader, balance)
                invalidate()
            }
        }
    }
}
