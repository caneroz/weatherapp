package com.canerozkaymak.weatherapp.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.HttpException
import java.net.UnknownHostException

abstract class BaseViewModel() : ViewModel() {
    val errorMessage = MutableLiveData<String>()

    //handling service errors
    fun handleError(error: Throwable) {
        when (error) {
            is HttpException ->  errorMessage.postValue("Page not found")
            is UnknownHostException -> errorMessage.postValue("Could not reach the host")
            else -> errorMessage.postValue(error.message)
        }

    }
}