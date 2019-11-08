package com.example.weather

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.data_for_a_unit.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.math.roundToLong

private const val LOG_TAG = "Weather API"

class MainActivity : AppCompatActivity() {
    private var daysOfWeek = ArrayList<View>()
    private var indexes = HashMap<View, Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initializeData()

        darkTheme.setOnCheckedChangeListener { _, isDarkThemeChosen ->
            val parseColor: Int = if (isDarkThemeChosen) {
                background.setImageResource(R.drawable.dark_background)
                Color.parseColor("#DDDDDD")
            } else {
                background.setImageResource(R.drawable.light_background)
                Color.parseColor("#000000")
            }
            mainTemperature.setTextColor(parseColor)
            darkTheme.setTextColor(parseColor)
            mainImage.setColorFilter(parseColor)
        }
    }

    private fun initializeData() {
        daysOfWeek = arrayListOf(
            dayLayout1,
            dayLayout2,
            dayLayout3,
            dayLayout4,
            dayLayout5,
            dayLayout6,
            dayLayout7,
            dayLayout8,
            dayLayout9,
            dayLayout10,
            dayLayout11,
            dayLayout12
        )
        daysOfWeek.mapIndexed { index, view -> indexes[view] = index }
        humidity.time.text = getString(R.string.humidity)
        pressure.time.text = getString(R.string.pressure)
        wind.time.text = getString(R.string.wind)
        clouds.time.text = getString(R.string.clouds)
        /*morningLayout.dayTime.text = getString(R.string.morning)
        morningLayout.temperature.text = getString(R.string._19)
        afternoonLayout.dayTime.text = getString(R.string.afternoon)
        afternoonLayout.temperature.text = getString(R.string._23)
        eveningLayout.dayTime.text = getString(R.string.evening)
        eveningLayout.temperature.text = getString(R.string._21)
        nightLayout.dayTime.text = getString(R.string.night)
        nightLayout.temperature.text = getString(R.string._17)
        nightLayout.image.setImageResource(R.drawable.moon)

        mondayLayout.dayTime.text = getString(R.string.monday)
        mondayLayout.temperature.text = getString(R.string._23)
        tuesdayLayout.dayTime.text = getString(R.string.tuesday)
        tuesdayLayout.temperature.text = getString(R.string._21)
        wednesdayLayout.dayTime.text = getString(R.string.wednesday)
        wednesdayLayout.temperature.text = getString(R.string._22)
        thursdayLayout.dayTime.text = getString(R.string.thursday)
        thursdayLayout.temperature.text = getString(R.string._20)
        fridayLayout.dayTime.text = getString(R.string.friday)
        fridayLayout.temperature.text = getString(R.string._24)
        saturdayLayout.dayTime.text = getString(R.string.saturday)
        saturdayLayout.temperature.text = getString(R.string._25)
        sundayLayout.dayTime.text = getString(R.string.sunday)
        sundayLayout.temperature.text = getString(R.string._28)*/
        run()
    }

    private var call: Call<WeatherList>? = null

    private fun run() {
        if (System.currentTimeMillis() - WeatherApplication.app.dateTime >= 10 * 60 * 1000) {
            call = WeatherApplication.app.weatherApi.getForecast()
            call?.enqueue(object : Callback<WeatherList> {
                override fun onFailure(call: Call<WeatherList>, t: Throwable) {
                    Log.e(LOG_TAG, "Failed with", t)
                }

                override fun onResponse(call: Call<WeatherList>, response: Response<WeatherList>) {
                    Log.d(LOG_TAG, Thread.currentThread().name)
                    val body = response
                        .body()
                        ?.list
                        ?.map {
                            MyData(
                                weather = it.weather[0],
                                temperature = it.main.temp,
                                humidity = it.main.humidity,
                                pressure = it.main.pressure,
                                clouds = it.clouds.all,
                                wind = it.wind.speed,
                                date = it.dt_txt
                            )
                        }
                    WeatherApplication.app.cache = if (body != null) {
                        WeatherApplication.app.dateTime = System.currentTimeMillis()
                        body
                    } else WeatherApplication.app.cache
                    applyCache()
                    Log.d(LOG_TAG, "Finished with ${response.code()}, body: $body")
//                adapter.contacts = body ?: emptyList()
//                adapter.notifyDataSetChanged()
                }
            })
        }
        applyCache()
        daysOfWeek[0].performClick()
    }

    private fun applyCache() {
        val body = WeatherApplication.app.cache
        for (pair in daysOfWeek.zip(body)) {
            val layout = pair.first
            val data = pair.second
            val imageView = layout.findViewById(R.id.image) as ImageView
            imageView.contentDescription = data.weather.main
            (layout.findViewById(R.id.time) as TextView).text =
                SimpleDateFormat("dd.MM hh:mm", Locale.US)
                    .format(
                    SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.US)
                        .parse(data.date) ?: Date()
                )
            (layout.findViewById(R.id.temp) as TextView).text = convertTemperature(data)
            imageView.setImageResource(
                resources.getIdentifier(
                    "p" + data.weather.icon,
                    "drawable",
                    "com.example.weather"
                )
            )
            //                       Picasso.get()
            //                           .load(/*"https://ic.pics.livejournal.com/davydov_index/60378694/1002193/1002193_900.jpg"*/"http://openweathermap.org/img/wn/" + data.second.icon + "@2x.png")
            //                           .into(imageView)
        }
        updateCur()
    }

    override fun onDestroy() {
        super.onDestroy()
        call?.cancel()
        call = null
    }

    fun onClick(view: View) {
        (daysOfWeek[WeatherApplication.app.currentDay].findViewById(R.id.time) as TextView).typeface =
            Typeface.DEFAULT
        WeatherApplication.app.currentDay = indexes[view] ?: 0
        (daysOfWeek[WeatherApplication.app.currentDay].findViewById(R.id.time) as TextView).typeface =
            Typeface.DEFAULT_BOLD
        updateCur()
    }

    private fun updateCur() {
        val curIndex = WeatherApplication.app.currentDay
        val data = WeatherApplication.app.cache.getOrNull(curIndex)
        if (data != null) {
            humidity.temp.text = "${data.humidity}%"
            pressure.temp.text = "${0.75 * data.pressure} mmHg"
            clouds.temp.text = "${data.clouds}%"
            wind.temp.text = "${data.wind} m/s"
            mainTemperature.text = convertTemperature(data)
            mainImage
                .setImageDrawable(
                    (daysOfWeek[curIndex].findViewById(R.id.image) as ImageView)
                        .drawable
                        .constantState!!
                        .newDrawable()
                        .mutate()
                )
        }
    }

    private fun convertTemperature(data: MyData) =
        (data.temperature.roundToLong()).toString() + "°C"
}
