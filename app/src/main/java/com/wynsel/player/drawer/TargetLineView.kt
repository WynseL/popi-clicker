package com.wynsel.player.drawer

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class TargetLineView @JvmOverloads constructor(context: Context?,
                                               attributeSet: AttributeSet? = null,
                                               defStyleAttr: Int = 0,
                                               defStyleRes: Int = 0):
        View(context, attributeSet, defStyleAttr, defStyleRes) {

    var width: Float = 1920f
    var height: Float = 1080f

    private val spaceY = 100f
    private val spaceX = 150f

    private val topOffset = 50f
    private val leftOffset = 50f

    private val paint = Paint().apply {
        color = Color.GREEN
        style = Paint.Style.STROKE
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
        strokeWidth = 3f
    }

    private val centerPaint = Paint().apply {
        color = Color.RED
        style = Paint.Style.FILL
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
        strokeWidth = 30f
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val (cWidth, cHeight) = ((width + leftOffset) / 2) to ((height + topOffset) / 2)
        val (tWidth, tHeight) = (cWidth) to (cHeight - spaceY)
        val (bWidth, bHeight) = (cWidth) to (cHeight + spaceY)
        val (lWidth, lHeight) = (cWidth - spaceX) to (cHeight)
        val (rWidth, rHeight) = (cWidth + spaceX) to (cHeight)

        // Point
        canvas?.drawPoint(cWidth, cHeight, centerPaint)

        // Center
        canvas?.drawLine(tWidth, tHeight, rWidth, rHeight, paint)
        canvas?.drawLine(rWidth, rHeight, bWidth, bHeight, paint)
        canvas?.drawLine(bWidth, bHeight, lWidth, lHeight, paint)
        canvas?.drawLine(lWidth, lHeight, tWidth, tHeight, paint)

        // Top Right
        canvas?.drawLine(tWidth, tHeight, tWidth + spaceX, tHeight - spaceY, paint)
        canvas?.drawLine(tWidth + spaceX, tHeight - spaceY, rWidth + spaceX, rHeight - spaceY, paint)
        canvas?.drawLine(rWidth, rHeight, rWidth + spaceX, rHeight - spaceY, paint)

        // Top Left
        canvas?.drawLine(tWidth, tHeight, tWidth - spaceX, tHeight - spaceY, paint)
        canvas?.drawLine(tWidth - spaceX, tHeight - spaceY, lWidth - spaceX, rHeight - spaceY, paint)
        canvas?.drawLine(lWidth, rHeight, lWidth - spaceX, rHeight - spaceY, paint)

        // Bottom Right
        canvas?.drawLine(rWidth, rHeight, rWidth + spaceX, rHeight + spaceY, paint)
        canvas?.drawLine(rWidth + spaceX, rHeight + spaceY, bWidth + spaceX, bHeight + spaceY, paint)
        canvas?.drawLine(bWidth, bHeight, bWidth + spaceX, bHeight + spaceY, paint)

        // Bottom Left
        canvas?.drawLine(lWidth, lHeight, lWidth - spaceX, lHeight + spaceY, paint)
        canvas?.drawLine(lWidth - spaceX, lHeight + spaceY, bWidth - spaceX, bHeight + spaceY, paint)
        canvas?.drawLine(bWidth, bHeight, bWidth - spaceX, bHeight + spaceY, paint)

        // 2x Points
        canvas?.drawPoint(tWidth + spaceX * 2, tHeight - spaceY, centerPaint)
        canvas?.drawPoint(tWidth - spaceX * 2, tHeight - spaceY, centerPaint)
        canvas?.drawPoint(rWidth + spaceX, rHeight + spaceY * 2, centerPaint)
        canvas?.drawPoint(lWidth - spaceX, lHeight + spaceY * 2, centerPaint)
    }
}