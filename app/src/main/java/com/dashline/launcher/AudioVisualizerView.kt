// Copyright (C) 2026 Mazdash Project
// SPDX-License-Identifier: GPL-3.0-only

package com.dashline.launcher

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.os.SystemClock
import android.util.AttributeSet
import android.view.View
import kotlin.math.sin

/**
 * Lightweight Mazda audio visualizer.
 *
 * Draws 5 animated Soul Red equalizer bars. When [isPlaying] is true,
 * animates smoothly using hardware-accelerated Canvas with zero memory allocations.
 * When paused, settles into sleek baseline resting bars and sleeps to consume 0% CPU.
 */
class AudioVisualizerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val barPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#C41230") // Mazda Soul Red
        style = Paint.Style.FILL
    }

    private val barRect = RectF()
    private val barCount = 5
    private val speeds = floatArrayOf(2.2f, 3.5f, 1.8f, 2.9f, 2.4f)
    private val phases = floatArrayOf(0.0f, 1.2f, 2.5f, 0.8f, 1.9f)
    private val currentHeights = FloatArray(barCount) { 0.15f }

    var isPlaying: Boolean = false
        set(value) {
            if (field != value) {
                field = value
                postInvalidateOnAnimation()
            }
        }

    fun setBarColor(color: Int) {
        barPaint.color = color
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val w = width.toFloat()
        val h = height.toFloat()
        if (w <= 0 || h <= 0) return

        val totalSpacing = (barCount - 1) * (w * 0.12f)
        val barWidth = (w - totalSpacing) / barCount
        val cornerRadius = barWidth / 2f
        val now = SystemClock.uptimeMillis() / 1000f

        var needsAnotherFrame = false

        for (i in 0 until barCount) {
            val targetHeight = if (isPlaying) {
                val wave = (sin(now * speeds[i] * 3f + phases[i]) + 1f) / 2f
                (0.25f + wave * 0.70f)
            } else {
                0.15f
            }

            // Smooth interpolation
            currentHeights[i] += (targetHeight - currentHeights[i]) * 0.25f
            if (Math.abs(targetHeight - currentHeights[i]) > 0.01f || isPlaying) {
                needsAnotherFrame = true
            }

            val barH = (h * currentHeights[i]).coerceAtLeast(barWidth)
            val left = i * (barWidth + (w * 0.12f))
            val right = left + barWidth
            val top = h - barH
            val bottom = h

            barRect.set(left, top, right, bottom)
            canvas.drawRoundRect(barRect, cornerRadius, cornerRadius, barPaint)
        }

        if (needsAnotherFrame) {
            postInvalidateOnAnimation()
        }
    }
}
