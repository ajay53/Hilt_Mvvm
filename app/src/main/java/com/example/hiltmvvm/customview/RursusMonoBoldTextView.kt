package com.example.hiltmvvm.customview

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.example.hiltmvvm.R

class RursusMonoBoldTextView : AppCompatTextView {

    constructor(context: Context) : super(context) {
        val typeFace = ResourcesCompat.getFont(context, R.font.rursus_compact_mono)
        this.setTypeface(typeFace, Typeface.BOLD)

        this.setTextColor(ContextCompat.getColor(context, R.color.text))
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        val typeFace = ResourcesCompat.getFont(context, R.font.rursus_compact_mono)
        this.setTypeface(typeFace, Typeface.BOLD)

        this.setTextColor(ContextCompat.getColor(context, R.color.text))
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context, attrs, defStyle
    ) {
        val typeFace = ResourcesCompat.getFont(context, R.font.rursus_compact_mono)
        this.setTypeface(typeFace, Typeface.BOLD)

        this.setTextColor(ContextCompat.getColor(context, R.color.text))
    }
}