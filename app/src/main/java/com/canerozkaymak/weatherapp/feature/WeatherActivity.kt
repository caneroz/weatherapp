package com.canerozkaymak.weatherapp.feature

import WeatherModel
import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.canerozkaymak.weatherapp.R
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main_weather.*
import java.util.*


class WeatherActivity : AppCompatActivity() {

    private val LOCATION_PERMISSION_CODE = 101
    private val REQUEST_LOCATION_SETTINGS = 102

    lateinit var weatherViewModel: WeatherViewModel
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var menu: Menu

    var isRefresh = false

    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_weather)

        setSupportActionBar(toolbar)
        toolbar.title = title

        weatherViewModel = ViewModelProviders.of(this)[WeatherViewModel::class.java]


        createLocationRequest()

        swipe_layout.setOnRefreshListener {
            isRefresh = true
            createLocationRequest()
        }

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
                for (location in locationResult.locations) {
                    if (location != null) {
                        weatherViewModel.loadCurrentWeather(
                            location.latitude.toString(),
                            location.longitude.toString()
                        )
                        break
                    }
                }
            }
        }

        weatherViewModel.weatherLiveData_.observe(this, Observer { weather ->

            setWeatherUI(weather)

            showWeather()

        })

        weatherViewModel.errorMessage.observe(this, Observer {
            parentLayout.stopLoading()
            Snackbar.make(
                parentLayout, it,
                Snackbar.LENGTH_SHORT
            ).show()

            if (isRefresh) swipe_layout.isRefreshing = false
        })
    }


    fun createLocationRequest() {
        locationRequest = LocationRequest.create().apply {
            interval = 10000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)


        val client: SettingsClient = LocationServices.getSettingsClient(this)
        val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())

        task.addOnSuccessListener { _ ->
            checkLocation()
        }

        task.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                try {
                    exception.startResolutionForResult(
                        this,
                        REQUEST_LOCATION_SETTINGS
                    )
                } catch (sendEx: IntentSender.SendIntentException) {

                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_LOCATION_SETTINGS -> {
                if (resultCode.equals(Activity.RESULT_OK)) {
                    checkLocation()
                } else {
                    Snackbar.make(
                        parentLayout, R.string.please_open_location_settings,
                        Snackbar.LENGTH_SHORT
                    ).show()

                    showNotAllowed()
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_refresh, menu)
        this.menu = menu

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

            R.id.menu_refresh -> {

                swipe_layout.isRefreshing = true

                createLocationRequest()

                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    fun checkLocation() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
                fusedLocationClient.lastLocation
                    .addOnSuccessListener { location: Location? ->
                        if (location != null) {
                            if (!isRefresh) parentLayout.startLoading()
                            weatherViewModel.loadCurrentWeather(
                                location.latitude.toString(),
                                location.longitude.toString()
                            )
                        } else {
                            if (!isRefresh) parentLayout.startLoading()
                            fusedLocationClient.requestLocationUpdates(
                                locationRequest,
                                locationCallback,
                                Looper.getMainLooper()
                            )
                        }
                    }
            }
            else -> {
                requestPermissions(
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    LOCATION_PERMISSION_CODE
                )
            }
        }
    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            LOCATION_PERMISSION_CODE -> {
                if ((grantResults.isNotEmpty() &&
                            grantResults[0] == PackageManager.PERMISSION_GRANTED)
                ) {
                    fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
                    fusedLocationClient.lastLocation
                        .addOnSuccessListener { location: Location? ->
                            parentLayout.startLoading()
                            weatherViewModel.loadCurrentWeather(
                                location?.latitude.toString(),
                                location?.longitude.toString()
                            )
                        }
                } else {
                    Snackbar.make(
                        parentLayout, R.string.please_allow_location,
                        Snackbar.LENGTH_SHORT
                    ).show()

                    showNotAllowed()
                }
                return
            }

        }
    }

    private fun setWeatherUI(weather: WeatherModel) {
        weather.timezone.let { tv_timezone.text = it }
        val date = Calendar.getInstance().getTime()
        tv_time.text = date.toString()
        weather.currently.temperature.let {
            tv_temperature.text = getString(R.string.temperature, it.toInt())
        }
        weather.currently.summary.let { tv_summary.text = it }
        weather.currently.icon.let { icon_view.setWeatherIcon(it) }

        weather.currently.apparentTemperature.let {
            tv_apparenttemperature_value.text =
                getString(R.string.apparent_temperature_value, it.toInt())
        }
        weather.currently.humidity.let {
            tv_humidity_value.text = getString(R.string.humidity_value, (it * 100).toInt())
        }
        weather.currently.windSpeed.let {
            tv_windspeed_value.text = getString(R.string.wind_speed_value, it.toInt())
        }
        weather.currently.precipProbability.let {
            tv_precip_probability_value.text =
                getString(R.string.precip_probability_value, (it * 100).toInt())
        }

        if (isRefresh) {
            swipe_layout.isRefreshing = false
        } else {
            parentLayout.stopLoading()
        }
    }

    private fun showWeather() {
        cv_first.visibility = View.VISIBLE
        cv_second.visibility = View.VISIBLE
        tv_location_not_allowed.visibility = View.GONE
    }

    private fun showNotAllowed() {
        tv_location_not_allowed.visibility = View.VISIBLE
        cv_first.visibility = View.GONE
        cv_second.visibility = View.GONE

        if (isRefresh)
            swipe_layout.isRefreshing = false
    }
}