package com.company.weatherapplication

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.company.weatherapplication.data.City

class MainViewModel : ViewModel() {
    var selectedCity: MutableLiveData<City> = MutableLiveData()
    var isCelsius: MutableLiveData<Boolean> = MutableLiveData(false)
}