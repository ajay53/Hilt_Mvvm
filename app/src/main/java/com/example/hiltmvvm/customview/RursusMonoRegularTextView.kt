package com.example.hiltmvvm.customview

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.example.hiltmvvm.R

class RursusMonoRegularTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    init {
        val typeFace = ResourcesCompat.getFont(context, R.font.rursus_compact_mono)
        this.setTypeface(typeFace, Typeface.NORMAL)

        this.setTextColor(ContextCompat.getColor(context, R.color.text))
    }
    /*constructor(context: Context) : super(context) {
        val typeFace = ResourcesCompat.getFont(context, R.font.rursus_compact_mono)
        this.setTypeface(typeFace, Typeface.NORMAL)

        this.setTextColor(ContextCompat.getColor(context, R.color.text))
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        val typeFace = ResourcesCompat.getFont(context, R.font.rursus_compact_mono)
        this.setTypeface(typeFace, Typeface.NORMAL)

        this.setTextColor(ContextCompat.getColor(context, R.color.text))
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context, attrs, defStyle
    ) {
        val typeFace = ResourcesCompat.getFont(context, R.font.rursus_compact_mono)
        this.setTypeface(typeFace, Typeface.NORMAL)

        this.setTextColor(ContextCompat.getColor(context, R.color.text))
    }*/
}