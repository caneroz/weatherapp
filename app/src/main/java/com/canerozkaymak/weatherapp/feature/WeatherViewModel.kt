package com.canerozkaymak.weatherapp.feature

import WeatherModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.canerozkaymak.weatherapp.base.BaseViewModel
import com.canerozkaymak.weatherapp.service.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class WeatherViewModel : BaseViewModel() {
    var weatherRepository = WeatherRepository()

    private val weatherLiveData: MutableLiveData<WeatherModel> = MutableLiveData()

    val weatherLiveData_: LiveData<WeatherModel> = weatherLiveData

    fun loadCurrentWeather(latitude: String, longitude: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                weatherLiveData.postValue(weatherRepository.getWeather(latitude, longitude))
            } catch (ex: Exception) {
                handleError(ex)
            }
        }
    }

}