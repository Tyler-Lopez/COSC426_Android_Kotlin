package com.company.weatherapplication.data

data class City(
    val temp: Double,
    val tempMax: Double,
    val tempMin: Double,
    val zipcode: String,
    val weatherCondition: String,
    val oneDayCondition: String,
    val twoDayCondition: String,
    val threeDayCondition: String,
    val cityName: String,
    val stateName: String
)
