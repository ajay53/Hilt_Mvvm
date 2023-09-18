package com.example.hiltmvvm.customview

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class RursusMonoTextView : AppCompatTextView {

    private val fontName = "rursus_compact_mono.ttf"

    constructor(context: Context) : super(context) {
        val typeFace = Typeface.createFromAsset(context.assets, fontName)
        this.typeface = typeFace
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        val typeFace = Typeface.createFromAsset(context.assets, fontName)
        this.typeface = typeFace
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context, attrs, defStyle
    ) {
        val typeFace = Typeface.createFromAsset(context.assets, fontName)
        this.typeface = typeFace
    }
}