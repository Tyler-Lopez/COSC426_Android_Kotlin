package com.company.weatherapplication

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import com.company.weatherapplication.data.SelectedCity
import com.company.weatherapplication.data.SelectedTemp
import com.company.weatherapplication.data.toState
import com.company.weatherapplication.databinding.ActivityDetailedBinding

class DetailedActivity : AppCompatActivity() {

    private var binding: ActivityDetailedBinding? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailedBinding.inflate(layoutInflater)
        setContentView(binding!!.root)




        val selectedCity = SelectedCity.city
        binding!!.dayDetailTemp.text = when (SelectedTemp.isCelsius) {
            true -> "%.1f".format(selectedCity!!.temp - 273.15)
            false -> "%.0f".format((selectedCity!!.temp - 273.15) * (9.0/5.0) + 32)
        }
        binding!!.mainWeatherLocation.text =
            "${selectedCity?.cityName}, ${selectedCity?.cityName?.toState()}"
        binding!!.mainWeatherStatus.text = selectedCity?.weatherCondition

        if (SelectedTemp.isCelsius)
            binding!!.temperature.text = "C"

        binding!!.mainWeatherClimacon.text =
            when (selectedCity?.weatherCondition) {
                "Thunderstorm" -> getString(R.string.climacons_thunderstorm)
                "Drizzle" -> getString(R.string.climacons_drizzle)
                "Rain" -> getString(R.string.climacons_rain)
                "Snow" -> getString(R.string.climacons_snow)
                "Clouds" -> getString(R.string.climacons_cloud)
                "Clear" -> getString(R.string.climacons_clear)
                else -> getString(R.string.climacons_cloud)
            }

        binding!!.detailedLayoutMain.setBackgroundResource(
            when (selectedCity?.weatherCondition) {
                "Thunderstorm" -> R.drawable.gradient_thunderstorm
                "Drizzle" -> R.drawable.gradient_drizzle
                "Rain" -> R.drawable.gradient_rain
                "Snow" -> R.drawable.gradient_snow
                "Clouds" -> R.drawable.gradient_clouds
                "Clear" -> R.drawable.gradient_clear
                else -> R.drawable.gradient_clouds
            }
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}