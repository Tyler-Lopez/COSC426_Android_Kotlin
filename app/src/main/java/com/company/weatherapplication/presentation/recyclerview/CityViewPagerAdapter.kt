package com.company.weatherapplication.presentation.recyclerview

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.company.weatherapplication.R
import com.company.weatherapplication.data.City
import com.company.weatherapplication.databinding.CityViewPagerBinding
import com.company.weatherapplication.util.getDay
import com.company.weatherapplication.util.kelvinToCelsius
import com.company.weatherapplication.util.kelvinToFahrenheit
import com.company.weatherapplication.util.weatherStatusToClimacon

class CityViewPagerAdapter(
    val cities: List<City>,
    val context: Context,
    val isCelsius: LiveData<Boolean>,
    val lifecycleOwner: LifecycleOwner

) : RecyclerView.Adapter<CityViewPagerAdapter.CityViewPagerViewHolder>() {
    inner class CityViewPagerViewHolder(val binding: CityViewPagerBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityViewPagerViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = CityViewPagerBinding.inflate(layoutInflater, parent, false)
        return CityViewPagerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CityViewPagerViewHolder, position: Int) {

        val city = cities[position]
        holder.binding.apply {

            mainWeatherLocation.text = city.cityName
            mainWeatherStatus.text = city.weatherCondition

            dayOneName.text = getDay(1)
            dayTwoName.text = getDay(2)
            dayThreeName.text = getDay(3)

            dayOneClimacon.text = weatherStatusToClimacon(city.oneDayCondition, context)
            dayTwoClimacon.text = weatherStatusToClimacon(city.twoDayCondition, context)
            dayThreeClimacon.text = weatherStatusToClimacon(city.threeDayCondition, context)
            mainWeatherClimacon.text = weatherStatusToClimacon(city.weatherCondition, context)

            detailedLayoutMain.setBackgroundResource(
                when (city.weatherCondition) {
                    "Thunderstorm" -> R.drawable.gradient_thunderstorm
                    "Drizzle" -> R.drawable.gradient_drizzle
                    "Rain" -> R.drawable.gradient_rain
                    "Snow" -> R.drawable.gradient_snow
                    "Clouds" -> R.drawable.gradient_clouds
                    "Clear" -> R.drawable.gradient_clear
                    else -> R.drawable.gradient_clouds
                }
            )
            // Set temperature initially to F, then listen for changes to C
            dayDetailTemp.text = "%.1f".format(city.temp.kelvinToFahrenheit())
            dayDetailHighAndLow.text = context.getString(
                R.string.summary_high_low,
                city.tempMax.kelvinToFahrenheit(),
                city.tempMin.kelvinToFahrenheit()
            )
            val celsiusObserver = Observer<Boolean> {
                dayDetailTemp.text = when (it) {
                    true -> "%.1f".format(city.temp.kelvinToCelsius())
                    false -> "%.1f".format(city.temp.kelvinToFahrenheit())
                }
                dayDetailHighAndLow.text = when (it) {
                    true -> context.getString(
                        R.string.summary_high_low,
                        city.tempMax.kelvinToCelsius(),
                        city.tempMin.kelvinToCelsius()
                    )
                    false -> context.getString(
                        R.string.summary_high_low,
                        city.tempMax.kelvinToFahrenheit(),
                        city.tempMin.kelvinToFahrenheit()
                    )
                }
            }
            isCelsius.observe(lifecycleOwner, celsiusObserver)

        }


    }

    override fun getItemCount(): Int = cities.size

}