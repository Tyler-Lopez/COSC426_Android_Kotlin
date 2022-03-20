package com.company.weatherapplication

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.view.View
import android.widget.TextView

@SuppressLint("AppCompatCustomView")
class ClimaconView(
    context: Context,
    attrs: AttributeSet
) : TextView(context, attrs) {
    init {
        typeface = Typeface.createFromAsset(context.assets, "climacons.ttf")
    }
}