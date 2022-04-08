package com.company.weatherapplication

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.company.weatherapplication.data.City
import com.company.weatherapplication.data.ObservedCities
import com.company.weatherapplication.databinding.FragmentViewPagerBinding
import com.company.weatherapplication.databinding.ListFragmentBinding
import com.company.weatherapplication.presentation.recyclerview.CityAdapter
import com.company.weatherapplication.presentation.recyclerview.CityViewPagerAdapter
import com.company.weatherapplication.responses.Forecast
import com.company.weatherapplication.responses.WeatherReport
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class ViewPagerFragment : Fragment() {

    private var binding: FragmentViewPagerBinding? = null
    private lateinit var vpCities: ViewPager2
    private lateinit var cityVpAdapter: CityViewPagerAdapter
    private val cities: MutableList<City> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_view_pager, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Initialize binding before set content view
        binding = FragmentViewPagerBinding.bind(view)

        val viewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]

        vpCities = binding!!.viewpager

        cityVpAdapter = CityViewPagerAdapter(
            cities = cities,
            context = requireActivity(),
            isCelsius = viewModel.isCelsius,
            lifecycleOwner = viewLifecycleOwner
        )

        vpCities.adapter = cityVpAdapter
        if (cities.isEmpty())
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
                    // Weather report contains ALL data from JSON
                    val weatherReport = Gson().fromJson(json, WeatherReport::class.java)
                    val apiUrlForecast = URL(
                        String.format(
                            Constants.WEATHER_FORECAST_API,
                            weatherReport.coord.lat,
                            weatherReport.coord.lon,
                            Constants.WEATHER_API_KEY
                        )
                    )
                    val httpConnectionForecast = apiUrlForecast.openConnection() as HttpURLConnection
                    httpConnectionForecast.connect()
                    val inputStreamForecast = httpConnectionForecast.inputStream
                    val jsonForecast = BufferedReader(InputStreamReader(inputStreamForecast)).readLine()
                    val forecast = Gson().fromJson(jsonForecast, Forecast::class.java)
                    cities.add(
                        City(
                            temp = weatherReport.main.temp,
                            tempMax = weatherReport.main.temp_max,
                            tempMin = weatherReport.main.temp_min,
                            zipcode = zipcode,
                            weatherCondition = weatherReport.weather[0].main,
                            oneDayCondition = forecast.list[0].weather[0].main,
                            twoDayCondition = forecast.list[1].weather[0].main,
                            threeDayCondition = forecast.list[2].weather[0].main,
                            cityName = weatherReport.name,
                            stateName = "Todo"
                        )
                    )
                    requireActivity().runOnUiThread {
                        cityVpAdapter.notifyItemChanged(cities.lastIndex)
                        binding!!.springDots.setViewPager2(vpCities)
                        // Initially invisible before data is loaded
                        binding!!.ll.visibility = View.VISIBLE
                        binding!!.plusButton.visibility = View.VISIBLE
                    }
                }
            }.start()

        binding!!.celciusButton.setOnClickListener {
            binding!!.celciusButton.setTextColor(
                ContextCompat.getColor(
                    requireActivity().applicationContext,
                    R.color.gold
                )
            )
            binding!!.fahrenheitButton.setTextColor(
                ContextCompat.getColor(
                    requireActivity().applicationContext,
                    R.color.egg_blue
                )
            )
            viewModel.isCelsius.value = true
        }

        binding!!.fahrenheitButton.setOnClickListener {
            binding!!.fahrenheitButton.setTextColor(
                ContextCompat.getColor(
                    requireActivity().applicationContext,
                    R.color.gold
                )
            )
            binding!!.celciusButton.setTextColor(
                ContextCompat.getColor(
                    requireActivity().applicationContext,
                    R.color.egg_blue
                )
            )
            viewModel.isCelsius.value = false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}