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
 * Sleek vertical EQ slider for Mazda graphic equalizer.
 *
 * Progress range: 0 to 14 (center 7 = 0 dB, 0 = -7 dB, 14 = +7 dB).
 * Draws vertical track with center baseline and Soul Red gain fill.
 */
class MazdaVerticalSlider @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val trackPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = androidx.core.content.ContextCompat.getColor(context, R.color.mz_surface_interactive)
        style = Paint.Style.FILL
    }

    private val centerMarkerPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = androidx.core.content.ContextCompat.getColor(context, R.color.mz_border)
        strokeWidth = 2f * resources.displayMetrics.density
        style = Paint.Style.STROKE
    }

    private val fillPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = androidx.core.content.ContextCompat.getColor(context, R.color.mz_accent)
        style = Paint.Style.FILL
    }

    private val thumbPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        style = Paint.Style.FILL
    }

    private val thumbRingPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = androidx.core.content.ContextCompat.getColor(context, R.color.mz_accent)
        strokeWidth = 2.5f * resources.displayMetrics.density
        style = Paint.Style.STROKE
    }

    var max: Int = 14
    var isTrackingTouch: Boolean = false
        private set

    var progress: Int = 7
        set(value) {
            val clamped = value.coerceIn(0, max)
            if (field != clamped) {
                field = clamped
                invalidate()
                onProgressChanged?.invoke(field)
            }
        }

    var onProgressChanged: ((Int) -> Unit)? = null
    var onTrackingTouchChanged: ((Boolean) -> Unit)? = null

    private val trackRect = RectF()
    private val fillRect = RectF()

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val w = width.toFloat()
        val h = height.toFloat()
        if (w <= 0 || h <= 0) return

        val density = resources.displayMetrics.density
        val trackWidth = 8f * density
        val thumbRadius = 13f * density
        val padY = thumbRadius + (2f * density)

        val cx = w / 2f
        val topY = padY
        val bottomY = h - padY
        val travel = bottomY - topY

        // Track
        trackRect.set(cx - trackWidth / 2f, topY, cx + trackWidth / 2f, bottomY)
        canvas.drawRoundRect(trackRect, trackWidth / 2f, trackWidth / 2f, trackPaint)

        // Center line (0 dB at progress 7)
        val centerY = topY + travel / 2f
        canvas.drawLine(cx - 10f * density, centerY, cx + 10f * density, centerY, centerMarkerPaint)

        // Thumb position (progress 14 = top, 0 = bottom)
        val frac = progress.toFloat() / max.toFloat()
        val thumbY = bottomY - (frac * travel)

        // Gain fill from center
        if (thumbY < centerY) {
            fillRect.set(cx - trackWidth / 2f, thumbY, cx + trackWidth / 2f, centerY)
            canvas.drawRect(fillRect, fillPaint)
        } else if (thumbY > centerY) {
            fillRect.set(cx - trackWidth / 2f, centerY, cx + trackWidth / 2f, thumbY)
            canvas.drawRect(fillRect, fillPaint)
        }

        // Thumb
        canvas.drawCircle(cx, thumbY, thumbRadius, thumbPaint)
        canvas.drawCircle(cx, thumbY, thumbRadius, thumbRingPaint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                parent?.requestDisallowInterceptTouchEvent(true)
                isTrackingTouch = true
                onTrackingTouchChanged?.invoke(true)
                updateFromTouch(event.y)
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                parent?.requestDisallowInterceptTouchEvent(true)
                updateFromTouch(event.y)
                return true
            }
            MotionEvent.ACTION_UP -> {
                parent?.requestDisallowInterceptTouchEvent(false)
                isTrackingTouch = false
                onTrackingTouchChanged?.invoke(false)
                updateFromTouch(event.y)
                return true
            }
            MotionEvent.ACTION_CANCEL -> {
                parent?.requestDisallowInterceptTouchEvent(false)
                isTrackingTouch = false
                onTrackingTouchChanged?.invoke(false)
                return true
            }
        }
        return super.onTouchEvent(event)
    }

    private fun updateFromTouch(y: Float) {
        val density = resources.displayMetrics.density
        val thumbRadius = 13f * density
        val padY = thumbRadius + (2f * density)
        val topY = padY
        val bottomY = height.toFloat() - padY
        val travel = bottomY - topY

        if (travel > 0) {
            val frac = ((bottomY - y) / travel).coerceIn(0f, 1f)
            progress = Math.round(frac * max)
        }
    }
}
