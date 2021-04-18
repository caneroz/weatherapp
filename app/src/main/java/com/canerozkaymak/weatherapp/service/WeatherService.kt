package com.canerozkaymak.weatherapp.service

import WeatherModel
import retrofit2.http.GET
import retrofit2.http.Path


interface WeatherService {
    @GET("{latitude}, {longitude}")
    suspend fun getCurrentWeather(
        @Path("latitude") latitude: String,
        @Path("longitude") longitude: String
    ): WeatherModel
}