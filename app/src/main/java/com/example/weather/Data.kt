package com.example.weather

class WeatherList(val list: List<WeatherSubList>)
class WeatherSubList(val weather: List<Weather>,
                     val main: NumberData,
                     val clouds: Clouds,
                     val wind: Wind,
                     val dt_txt: String)

class NumberData(
    val temp: Float,
    val pressure: Float,
    val humidity: Float
)

class Clouds(val all: Float)
class Wind(val speed: Float)

class Weather(
    val main: String? = "",
    val icon: String? = ""
)

class MyData(val weather: Weather,
             val temperature: Float,
             val pressure: Float,
             val humidity: Float,
             val clouds: Float,
             val wind: Float,
             val date: String)