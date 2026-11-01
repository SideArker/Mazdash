// Copyright (C) 2026 Mazdash Project
// SPDX-License-Identifier: GPL-3.0-only

package com.dashline.launcher

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

/**
 * Interactive 2D Sound Stage / Cockpit View for Mazda Headunit.
 *
 * Visualizes car cabin with 4 speakers and lets the driver drag or tap
 * a Soul Red crosshair to position Fader (Front/Rear) and Balance (Left/Right).
 */
class MazdaSoundStageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val bgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#18181A")
        style = Paint.Style.FILL
    }

    private val gridPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#333336")
        strokeWidth = 2f * resources.displayMetrics.density
        style = Paint.Style.STROKE
    }

    private val carOutlinePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#444448")
        strokeWidth = 3f * resources.displayMetrics.density
        style = Paint.Style.STROKE
    }

    private val speakerPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#66666A")
        style = Paint.Style.FILL
    }

    private val targetPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#C41230") // Soul Red
        style = Paint.Style.FILL
    }

    private val targetGlowPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#44C41230")
        style = Paint.Style.FILL
    }

    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#8E8E93")
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

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val w = width.toFloat()
        val h = height.toFloat()
        if (w <= 0 || h <= 0) return

        val pad = 24f * resources.displayMetrics.density
        boundsRect.set(pad, pad, w - pad, h - pad)

        val corner = 16f * resources.displayMetrics.density
        canvas.drawRoundRect(boundsRect, corner, corner, bgPaint)
        canvas.drawRoundRect(boundsRect, corner, corner, gridPaint)

        // Center crosshair lines
        val cx = boundsRect.centerX()
        val cy = boundsRect.centerY()
        canvas.drawLine(cx, boundsRect.top + 16, cx, boundsRect.bottom - 16, gridPaint)
        canvas.drawLine(boundsRect.left + 16, cy, boundsRect.right - 16, cy, gridPaint)

        // Cabin labels
        canvas.drawText("PRZÓD (FRONT)", cx, boundsRect.top + 28f, textPaint)
        canvas.drawText("TYŁ (REAR)", cx, boundsRect.bottom - 14f, textPaint)

        // 4 Speaker locations
        val spkRadius = 7f * resources.displayMetrics.density
        val spkOffset = 36f * resources.displayMetrics.density
        // Front-Left
        canvas.drawCircle(boundsRect.left + spkOffset, boundsRect.top + spkOffset, spkRadius, speakerPaint)
        // Front-Right
        canvas.drawCircle(boundsRect.right - spkOffset, boundsRect.top + spkOffset, spkRadius, speakerPaint)
        // Rear-Left
        canvas.drawCircle(boundsRect.left + spkOffset, boundsRect.bottom - spkOffset, spkRadius, speakerPaint)
        // Rear-Right
        canvas.drawCircle(boundsRect.right - spkOffset, boundsRect.bottom - spkOffset, spkRadius, speakerPaint)

        // Target Disc (Soul Red)
        val fracX = balance.toFloat() / MazdaAudioDspController.MAX_STAGE.toFloat()
        val fracY = fader.toFloat() / MazdaAudioDspController.MAX_STAGE.toFloat()

        val activeWidth = boundsRect.width() - (spkOffset * 1.5f)
        val activeHeight = boundsRect.height() - (spkOffset * 1.5f)
        val startX = boundsRect.left + (spkOffset * 0.75f)
        val startY = boundsRect.top + (spkOffset * 0.75f)

        val targetX = startX + (fracX * activeWidth)
        val targetY = startY + (fracY * activeHeight)

        val glowRadius = 24f * resources.displayMetrics.density
        val targetRadius = 10f * resources.displayMetrics.density

        canvas.drawCircle(targetX, targetY, glowRadius, targetGlowPaint)
        canvas.drawCircle(targetX, targetY, targetRadius, targetPaint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                val pad = 24f * resources.displayMetrics.density
                val spkOffset = 36f * resources.displayMetrics.density
                val startX = pad + (spkOffset * 0.75f)
                val startY = pad + (spkOffset * 0.75f)
                val activeWidth = (width - 2 * pad) - (spkOffset * 1.5f)
                val activeHeight = (height - 2 * pad) - (spkOffset * 1.5f)

                if (activeWidth > 0 && activeHeight > 0) {
                    val rawFracX = ((event.x - startX) / activeWidth).coerceIn(0f, 1f)
                    val rawFracY = ((event.y - startY) / activeHeight).coerceIn(0f, 1f)

                    val newBalance = (rawFracX * MazdaAudioDspController.MAX_STAGE).toInt()
                    val newFader = (rawFracY * MazdaAudioDspController.MAX_STAGE).toInt()

                    if (newBalance != balance || newFader != fader) {
                        balance = newBalance
                        fader = newFader
                        onStagePositionChanged?.invoke(fader, balance)
                        invalidate()
                    }
                }
                return true
            }
        }
        return super.onTouchEvent(event)
    }
}
