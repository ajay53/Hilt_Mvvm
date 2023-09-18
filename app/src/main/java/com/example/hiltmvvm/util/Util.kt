package com.example.hiltmvvm.util

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Insets
import android.location.LocationManager
import android.os.Build
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.DisplayMetrics
import android.util.Log
import android.view.WindowInsets
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.hiltmvvm.R

object Util {
    private const val TAG = "Util"

    //checking for GPS
    fun isGpsEnabled(context: Context): Boolean {
        val lm = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        var isGpsEnabled = false
        try {
            isGpsEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
        } catch (ex: java.lang.Exception) {
            Log.d(TAG, "isGpsEnabled: $ex")
        }

        return isGpsEnabled
    }

    //checking for location permission
    fun hasLocationPermission(context: Context): Boolean {
        val fineLoc: Boolean = ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        val coarseLoc: Boolean = ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        return fineLoc && coarseLoc
    }

    //setting and styling status of restaurant
    @JvmStatic
    @BindingAdapter("statusAdapter")
    fun setStatus(view: AppCompatTextView, isClosed: Boolean) {
        val status = if (isClosed) {
            "Currently CLOSED"
        } else {
            "Currently OPEN"
        }

        val spannable = SpannableString(status)
        spannable.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(view.context, R.color.black)),
            0, 9,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        if (isClosed) {
            spannable.setSpan(
                ForegroundColorSpan(ContextCompat.getColor(view.context, R.color.status_closed)),
                9, status.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        } else {
            spannable.setSpan(
                ForegroundColorSpan(ContextCompat.getColor(view.context, R.color.status_open)),
                9, status.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

        view.text = spannable
    }

    @JvmStatic
    @BindingAdapter("imageAdapter")
    fun setImageResource(view: AppCompatImageView, imageUrl: String) {
        val context: Context = view.context

        val options: RequestOptions = RequestOptions()
            .placeholder(R.drawable.ic_restaurant)
            .error(R.drawable.ic_restaurant)

        Glide.with(context).setDefaultRequestOptions(options).load(imageUrl).into(view)
    }

    //setting and styling rating of restaurant
    @JvmStatic
    @BindingAdapter("ratingAdapter")
    fun setRating(view: AppCompatTextView, rating: Double) {

        val colorId: Int = when {
            rating > 4.5 -> R.color.rating_great
            rating > 4.0 -> R.color.rating_good
            rating > 3.5 -> R.color.rating_average
            else -> R.color.rating_bad
        }

        view.text = rating.toString()
        view.setBackgroundResource(colorId)
    }
}