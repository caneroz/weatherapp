package com.canerozkaymak.weatherapp.service

import com.canerozkaymak.weatherapp.util.BASE_URL
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

class NetworkFactory {
    companion object {
        val retrofit: Retrofit = Retrofit.Builder()
            .client(OkHttpClient.Builder().build())
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .build()


        internal fun provideWeatherService(): WeatherService {
            return retrofit.create(WeatherService::class.java)
        }
    }
}
