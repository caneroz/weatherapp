package com.canerozkaymak.weatherapp.feature

import kotlinx.coroutines.*
import kotlinx.coroutines.test.setMain
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before

class WeatherViewModelTest {
    @ObsoleteCoroutinesApi
    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @ObsoleteCoroutinesApi
    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        Dispatchers.setMain(mainThreadSurrogate)
    }

    @Test
    fun loadCurrentWeather_location_equals() {
        val viewModel = WeatherViewModel()
        runBlocking {
            launch(Dispatchers.Main) {
                val weather = viewModel.weatherRepository.getWeather("40","26")
                assertEquals(weather.latitude, 40.0, 0.1)
            }
        }
    }
}