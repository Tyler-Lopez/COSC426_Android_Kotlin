package com.company.weatherapplication.data

fun String.toState() : String =
    when (this) {
        "Ypsilanti" -> "MI"
        "Yuma" -> "AZ"
        "Fort Wainwright" -> "AK"
        "Bay City" -> "MI"
        else -> "MI"
    }