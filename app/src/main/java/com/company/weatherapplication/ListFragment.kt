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
import com.company.weatherapplication.data.City
import com.company.weatherapplication.data.ObservedCities
import com.company.weatherapplication.databinding.ListFragmentBinding
import com.company.weatherapplication.presentation.recyclerview.CityAdapter
import com.company.weatherapplication.responses.WeatherReport
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class ListFragment : Fragment() {

    private var binding: ListFragmentBinding? = null
    private lateinit var rvCities: RecyclerView
    private lateinit var cityAdapter: CityAdapter
    private val cities: MutableList<City> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.list_fragment, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Initialize binding before set content view
        binding = ListFragmentBinding.bind(view)

        val viewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]

        rvCities = binding!!.rvCities
        cityAdapter = CityAdapter(
            cities = cities,
            context = requireContext(),
            celsiusLiveData = viewModel.isCelsius,
            lifecycleOwner = viewLifecycleOwner,
            deleteItemOnLongClick = {
                cities.removeAt(it)
                cityAdapter.notifyItemChanged(it)
            },
            onClick = {
                viewModel.selectedCity.value = it
                println(viewModel.selectedCity.value)
                findNavController().navigate(R.id.action_listFragment_to_detailedFragment)
            })

        rvCities.adapter = cityAdapter

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
                    cities.add(
                        City(
                            weatherReport.main.temp,
                            zipcode,
                            weatherReport.weather[0].main,
                            weatherReport.name,
                            "Todo"
                        )
                    )
                    requireActivity().runOnUiThread { cityAdapter.notifyItemChanged(cities.lastIndex) }
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