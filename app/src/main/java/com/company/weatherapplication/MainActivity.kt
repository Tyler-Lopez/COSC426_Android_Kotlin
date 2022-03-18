package com.company.weatherapplication

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import com.company.weatherapplication.data.City
import com.company.weatherapplication.data.ObservedCities
import com.company.weatherapplication.databinding.ActivityMainBinding
import com.company.weatherapplication.presentation.recyclerview.CityAdapter
import com.company.weatherapplication.responses.WeatherReport
import com.google.gson.Gson
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null
    private var ctx: Context = this
    private lateinit var rvCities: RecyclerView
    private lateinit var cityAdapter: CityAdapter
    private val cities: MutableList<City> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize binding before set content view
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        rvCities = binding!!.rvCities
        cityAdapter = CityAdapter(cities) {

        }

        rvCities.adapter = cityAdapter

        // Thread to read from the API
        Thread {
            for (zipcode in ObservedCities.zipcodes) {
                val apiUrl = URL(
                    String.format(
                        Constants.WEATHER_ZIP_API,
                        zipcode,
                        Constants.WEATHER_API_KEY
                    )
                )
                val httpConnection = apiUrl.openConnection() as HttpURLConnection
                httpConnection.connect()
                val inputStream = httpConnection.inputStream
                val json = BufferedReader(InputStreamReader(inputStream)).readLine()
                // https://stackoverflow.com/questions/41928803/how-to-parse-json-in-kotlin
                // Weather report contains ALL data from JSON
                val weatherReport = Gson().fromJson(json, WeatherReport::class.java)
                cities.add(
                    City(
                        weatherReport.main.temp,
                        zipcode,
                        weatherReport.name,
                        "Todo"
                    )
                )
                runOnUiThread { cityAdapter.notifyItemChanged(cities.lastIndex)  }
            }
        }.start()

    }

    // Necessary to prevent memory leaks on
    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}