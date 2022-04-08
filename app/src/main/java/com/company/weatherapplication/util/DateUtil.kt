package com.company.weatherapplication.util

import java.util.*

fun getDay(dayOffset: Int): String {
    val calendar = Calendar.getInstance()
    for (i in 0 until dayOffset)
        calendar.add(Calendar.DATE, 1)

    return when (calendar.get(Calendar.DAY_OF_WEEK)) {
        1 -> "Sun"
        2 -> "Mon"
        3 -> "Tue"
        4 -> "Wed"
        5 -> "Thu"
        6 -> "Fri"
        else -> "Sat"
    }
}