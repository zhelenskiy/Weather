package com.example.weather

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.data_for_a_unit.view.*

class MainActivity : AppCompatActivity() {

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
        morningLayout.dayTime.text = getString(R.string.morning)
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
        sundayLayout.temperature.text = getString(R.string._28)
    }
}
