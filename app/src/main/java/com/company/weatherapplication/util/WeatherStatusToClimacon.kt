package com.company.weatherapplication.util

import android.content.Context
import com.company.weatherapplication.R

fun weatherStatusToClimacon(condition: String, context: Context) =
    when (condition) {
        "Thunderstorm" -> context.getString(R.string.climacons_thunderstorm)
        "Drizzle" -> context.getString(R.string.climacons_drizzle)
        "Rain" -> context.getString(R.string.climacons_rain)
        "Snow" -> context.getString(R.string.climacons_snow)
        "Clouds" -> context.getString(R.string.climacons_cloud)
        "Clear" -> context.getString(R.string.climacons_clear)
        else -> context.getString(R.string.climacons_cloud)
    }
