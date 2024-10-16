package com.nuevo.gameness.utils

import android.content.Context
import android.graphics.Matrix
import android.graphics.RectF
import android.util.AttributeSet

class BottomCropImage : androidx.appcompat.widget.AppCompatImageView {
    constructor(
        context: Context
    ): super(context) { setup() }

    constructor(
        context: Context,
        attrs: AttributeSet
    ): super(context, attrs) { setup() }

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyle: Int
    ): super(context, attrs, defStyle) { setup() }

    private fun setup() {
        scaleType = ScaleType.MATRIX
    }

    override fun setFrame(l: Int, t: Int, r: Int, b: Int): Boolean {
        val matrix: Matrix = imageMatrix
        val scale: Float
        val viewWidth = width - paddingLeft - paddingRight
        val viewHeight = height - paddingTop - paddingBottom
        val drawableWidth = drawable?.intrinsicWidth ?: 0
        val drawableHeight = drawable?.intrinsicHeight ?: 0

        //Get the scale
        scale = if (drawableWidth * viewHeight > drawableHeight * viewWidth) {
            viewHeight.toFloat() / drawableHeight.toFloat()
        } else {
            viewWidth.toFloat() / drawableWidth.toFloat()
        }

        //Define the rect to take image portion from
        val drawableRect = RectF(
            0f, drawableHeight - viewHeight / scale,
            drawableWidth.toFloat(), drawableHeight.toFloat()
        )
        val viewRect = RectF(0f, 0f, viewWidth.toFloat(), viewHeight.toFloat())
        matrix.setRectToRect(drawableRect, viewRect, Matrix.ScaleToFit.FILL)
        imageMatrix = matrix
        return super.setFrame(l, t, r, b)
    }
}