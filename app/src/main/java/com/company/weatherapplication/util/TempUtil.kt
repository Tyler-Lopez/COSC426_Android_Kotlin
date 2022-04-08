package com.company.weatherapplication.util

fun Double.kelvinToCelsius(): Double =
    this - 273.15

fun Double.kelvinToFahrenheit(): Double =
    (this - 273.15) * (9.0 / 5.0) + 32
