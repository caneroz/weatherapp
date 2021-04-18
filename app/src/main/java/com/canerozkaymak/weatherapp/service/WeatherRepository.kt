package com.canerozkaymak.weatherapp.service

class WeatherRepository() {
    var weatherService: WeatherService = NetworkFactory.provideWeatherService()

    suspend fun getWeather(latitude: String, longitude: String) =
        weatherService.getCurrentWeather(latitude, longitude)

}