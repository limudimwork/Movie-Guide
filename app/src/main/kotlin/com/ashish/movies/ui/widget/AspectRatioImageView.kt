package com.ashish.movies.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import com.ashish.movies.R

/**
 * Created by Ashish on Dec 29.
 */
class AspectRatioImageView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null,
                                                     defStyleAttr: Int = 0) : ImageView(context, attrs, defStyleAttr) {

    private var heightRatio: Float = 1.0F

    init {
        val attributeArray = context.obtainStyledAttributes(attrs, R.styleable.AspectRatioImageView)
        this.heightRatio = attributeArray.getFloat(R.styleable.AspectRatioImageView_heightRatio, 1.0f)
        attributeArray.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (heightRatio > 0.0) {
            val width = MeasureSpec.getSize(widthMeasureSpec)
            val height = (width * heightRatio).toInt()
            setMeasuredDimension(width, height)
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        }
    }
}