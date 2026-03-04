package com.example.jamanalog

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import java.util.*
import kotlin.math.cos
import kotlin.math.sin

class AnalogClockView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var padding = 0
    private var fontSize = 0
    private var handTruncation = 0
    private var hourHandTruncation = 0
    private var radius = 0
    private lateinit var paint: Paint
    private var isInit = false
    private val numbers = intArrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12)
    private val rect = Rect()

    private fun initClock() {
        padding = 50
        fontSize = 40
        val minHeightWidth = Math.min(height, width)
        radius = minHeightWidth / 2 - padding
        handTruncation = minHeightWidth / 20
        hourHandTruncation = minHeightWidth / 7
        paint = Paint()
        isInit = true
    }

    override fun onDraw(canvas: Canvas) {
        if (!isInit) {
            initClock()
        }

        canvas.drawColor(Color.TRANSPARENT)
        drawCircle(canvas)
        drawTicks(canvas)
        drawCenter(canvas)
        drawNumerals(canvas)
        drawHands(canvas)

        postInvalidateDelayed(1000)
    }

    private fun drawTicks(canvas: Canvas) {
        paint.style = Paint.Style.FILL
        paint.color = Color.BLACK
        val tickRadius = radius + 20f
        for (i in 0 until 60) {
            val angle = Math.PI * i / 30 - Math.PI / 2
            val x = (width / 2 + cos(angle) * tickRadius).toFloat()
            val y = (height / 2 + sin(angle) * tickRadius).toFloat()
            
            if (i % 5 == 0) {
                canvas.drawCircle(x, y, 6f, paint)
            } else {
                canvas.drawCircle(x, y, 3f, paint)
            }
        }
    }

    private fun drawHand(canvas: Canvas, loc: Double, isHour: Boolean) {
        val angle = Math.PI * loc / 30 - Math.PI / 2
        val handRadius = if (isHour) radius - hourHandTruncation else radius - handTruncation
        canvas.drawLine(
            (width / 2).toFloat(), (height / 2).toFloat(),
            (width / 2 + cos(angle) * handRadius).toFloat(),
            (height / 2 + sin(angle) * handRadius).toFloat(),
            paint
        )
    }

    private fun drawHands(canvas: Canvas) {
        val c = Calendar.getInstance()
        var hour = c.get(Calendar.HOUR_OF_DAY).toFloat()
        hour = if (hour > 12) hour - 12 else hour
        
        paint.strokeWidth = 12f
        paint.color = Color.BLACK
        drawHand(canvas, ((hour + c.get(Calendar.MINUTE) / 60.0) * 5.0), true)
        
        paint.strokeWidth = 8f
        drawHand(canvas, c.get(Calendar.MINUTE).toDouble(), false)
        
        paint.strokeWidth = 4f
        paint.color = Color.RED
        drawHand(canvas, c.get(Calendar.SECOND).toDouble(), false)
    }

    private fun drawNumerals(canvas: Canvas) {
        paint.textSize = fontSize.toFloat()
        paint.color = Color.BLACK
        paint.style = Paint.Style.FILL
        for (number in numbers) {
            val tmp = number.toString()
            paint.getTextBounds(tmp, 0, tmp.length, rect)
            val angle = Math.PI / 6 * (number - 3)
            val x = (width / 2 + cos(angle) * radius - rect.width() / 2).toFloat()
            val y = (height / 2 + sin(angle) * radius + rect.height() / 2).toFloat()
            canvas.drawText(tmp, x, y, paint)
        }
    }

    private fun drawCenter(canvas: Canvas) {
        paint.style = Paint.Style.FILL
        paint.color = Color.BLACK
        canvas.drawCircle((width / 2).toFloat(), (height / 2).toFloat(), 12f, paint)
    }

    private fun drawCircle(canvas: Canvas) {
        paint.reset()
        paint.color = Color.BLACK
        paint.strokeWidth = 10f
        paint.style = Paint.Style.STROKE
        paint.isAntiAlias = true
        canvas.drawCircle((width / 2).toFloat(), (height / 2).toFloat(), (radius + padding - 10).toFloat(), paint)
    }
}
