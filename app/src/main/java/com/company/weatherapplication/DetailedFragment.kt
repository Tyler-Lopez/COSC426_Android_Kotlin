package com.company.weatherapplication

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.company.weatherapplication.data.City
import com.company.weatherapplication.data.toState
import com.company.weatherapplication.databinding.DetailedFragmentBinding

class DetailedFragment : Fragment() {

    private var binding: DetailedFragmentBinding? = null
    private var activity: MainActivity? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.detailed_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = DetailedFragmentBinding.bind(view)

        val viewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]

        val cityObserver = Observer<City> { selectedCity ->
            binding!!.dayDetailTemp.text = when (viewModel.isCelsius.value) {
                true -> "%.1f".format(selectedCity!!.temp - 273.15)
                else -> "%.0f".format((selectedCity!!.temp - 273.15) * (9.0/5.0) + 32)
            }
            binding!!.mainWeatherLocation.text =
                "${selectedCity?.cityName}, ${selectedCity?.cityName?.toState()}"
            binding!!.mainWeatherStatus.text = selectedCity?.weatherCondition

            if (viewModel.isCelsius.value == true)
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
        viewModel.selectedCity.observe(viewLifecycleOwner, cityObserver)
    }

}