package com.example.weather

import android.content.Context
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("data/2.5/forecast")
    fun getForecast(
        @Query("q") placeName: String = "Moscow,ru",
        @Query("appid") appId: String = "ee82869b7f40ee0f29249ff4a12cddd1",
        @Query("units") units: String = "metric"
    ): Call<WeatherList>
}

fun createForecast(context: Context): WeatherApi {
    val client = OkHttpClient()
    val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()
    val retrofit = Retrofit.Builder()
        .client(client)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl(context.getString(R.string.base_url))
        .build()
    return retrofit.create(WeatherApi::class.java)
}