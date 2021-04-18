package com.canerozkaymak.weatherapp.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import com.canerozkaymak.weatherapp.R
import com.google.android.material.progressindicator.CircularProgressIndicator

class ProgressiveRelativeLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : RelativeLayout(context, attrs, defStyle) {

    lateinit var progressBar: CircularProgressIndicator

    init {
        initView()
    }

    private fun initView() {
        LayoutInflater.from(context)
            .inflate(R.layout.progressive_relative_layout, this, true)

        progressBar = findViewById(R.id.progressBar)
    }

    fun stopLoading() {
        progressBar.visibility = View.GONE
    }

    fun startLoading() {
        progressBar.visibility = View.VISIBLE
    }

}
