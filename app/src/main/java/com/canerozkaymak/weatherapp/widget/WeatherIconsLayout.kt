package com.canerozkaymak.weatherapp.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import com.canerozkaymak.weatherapp.R
import com.thbs.skycons.library.*

class WeatherIconsLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : RelativeLayout(context, attrs, defStyle) {

    lateinit var clearDay: SunView
    lateinit var clearNight: MoonView
    lateinit var partlyCloudyDay: CloudSunView
    lateinit var partlyCloudyNight: CloudMoonView
    lateinit var cloudyDay: CloudView
    lateinit var rainView: CloudRainView
    lateinit var sleetView: CloudHvRainView
    lateinit var snowView: CloudSnowView
    lateinit var windView: WindView
    lateinit var fogView: CloudFogView

    init {
        initView()
    }

    private fun initView() {
        LayoutInflater.from(context)
            .inflate(R.layout.weather_icons_layout, this, true)

        clearDay = findViewById(R.id.view_clear_day)
        clearNight = findViewById(R.id.view_clear_night)
        partlyCloudyDay = findViewById(R.id.view_partly_cloudy_day)
        partlyCloudyNight = findViewById(R.id.view_partly_cloudy_night)
        cloudyDay = findViewById(R.id.view_cloudy)
        rainView = findViewById(R.id.view_rain)
        sleetView = findViewById(R.id.view_sleet)
        snowView = findViewById(R.id.view_snow)
        windView = findViewById(R.id.view_wind)
        fogView = findViewById(R.id.view_fog)

    }

    fun setWeatherIcon(name: String) {
        when (name) {
            "clear-day" -> clearDay.visibility = View.VISIBLE
            "clear-night" -> clearNight.visibility = View.VISIBLE
            "partly-cloudy-day" -> partlyCloudyDay.visibility = View.VISIBLE
            "partly-cloudy-night" -> partlyCloudyNight.visibility = View.VISIBLE
            "cloudy" -> cloudyDay.visibility = View.VISIBLE
            "rain" -> rainView.visibility = View.VISIBLE
            "sleet" -> sleetView.visibility = View.VISIBLE
            "snow" -> snowView.visibility = View.VISIBLE
            "wind" -> windView.visibility = View.VISIBLE
            "fog" -> fogView.visibility = View.VISIBLE
            else -> {

            }
        }
    }

}