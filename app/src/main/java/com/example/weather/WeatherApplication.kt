package com.example.weather

import android.app.Application

class WeatherApplication : Application() {
    lateinit var weatherApi: WeatherApi
        private set

    var cache = List(0) { MyData(Weather(), Float.NaN, Float.NaN, Float.NaN, Float.NaN, Float.NaN, "") }
    var dateTime = 0L
    var currentDay = 0

    override fun onCreate() {
        super.onCreate()
        weatherApi = createForecast(this)
        app = this
    }

    companion object {
        lateinit var app: WeatherApplication
            private set
    }
}