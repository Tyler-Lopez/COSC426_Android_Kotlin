package com.company.weatherapplication.responses

data class Forecast(
    val city: City,
    val cnt: Int,
    val cod: String,
    val list: List<DayForecast>,
    val message: Int
)