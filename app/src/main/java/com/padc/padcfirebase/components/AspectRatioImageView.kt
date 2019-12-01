package com.padc.padcfirebase.components

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView

class AspectRatioImageView : AppCompatImageView {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val fourThreeHeight = MeasureSpec.getSize(widthMeasureSpec) * 9 / 18
        val fourThreeHeightSpec =
            MeasureSpec.makeMeasureSpec(fourThreeHeight, MeasureSpec.EXACTLY)
        super.onMeasure(widthMeasureSpec, fourThreeHeightSpec)
    }
}