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
 * Draws animated Soul Red equalizer bars (configurable count, e.g. 10 or 20).
 * When [isPlaying] is true, animates smoothly using hardware-accelerated Canvas.
 * When paused, settles into sleek baseline resting bars and sleeps to consume 0% CPU.
 */
class AudioVisualizerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val barPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = androidx.core.content.ContextCompat.getColor(context, R.color.mz_accent)
        style = Paint.Style.FILL
    }

    private val barRect = RectF()

    var barCount: Int = 10
        set(value) {
            val clamped = value.coerceIn(3, 32)
            if (field != clamped) {
                field = clamped
                initBars()
                invalidate()
            }
        }

    private var speeds = FloatArray(10)
    private var phases = FloatArray(10)
    private var currentHeights = FloatArray(10)

    init {
        initBars()
    }

    private fun initBars() {
        speeds = FloatArray(barCount) { idx ->
            1.6f + ((idx * 7) % 11) * 0.22f
        }
        phases = FloatArray(barCount) { idx ->
            (idx * 0.73f) % (Math.PI * 2).toFloat()
        }
        currentHeights = FloatArray(barCount) { 0.12f }
    }

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

        val spacing = 2.0f * resources.displayMetrics.density
        val totalSpacing = (barCount - 1) * spacing
        val barWidth = ((w - totalSpacing) / barCount).coerceAtLeast(2f)
        val cornerRadius = (barWidth / 2f).coerceAtMost(4f)
        val now = SystemClock.uptimeMillis() / 1000f

        var needsAnotherFrame = false

        for (i in 0 until barCount) {
            val targetHeight = if (isPlaying) {
                val wave = (sin(now * speeds[i] * 3.2f + phases[i]) + 1f) / 2f
                (0.18f + wave * 0.78f)
            } else {
                0.12f
            }

            // Smooth interpolation
            currentHeights[i] += (targetHeight - currentHeights[i]) * 0.28f
            if (Math.abs(targetHeight - currentHeights[i]) > 0.01f || isPlaying) {
                needsAnotherFrame = true
            }

            val barH = (h * currentHeights[i]).coerceAtLeast(barWidth)
            val left = i * (barWidth + spacing)
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
