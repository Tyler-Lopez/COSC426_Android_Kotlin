package com.company.weatherapplication.responses

data class MainX(
    val feels_like: Double,
    val grnd_level: Double,
    val humidity: Double,
    val pressure: Double,
    val sea_level: Double,
    val temp: Double,
    val temp_kf: Double,
    val temp_max: Double,
    val temp_min: Double
)