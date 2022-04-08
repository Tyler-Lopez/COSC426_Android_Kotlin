package com.company.weatherapplication.responses

data class DayForecast(
    val clouds: CloudsX,
    val dt: Double,
    val dt_txt: String,
    val main: MainX,
    val pop: Double,
    val sys: SysX,
    val visibility: Double,
    val weather: List<WeatherX>,
    val wind: WindX
)