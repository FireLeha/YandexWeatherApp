package com.vantula.yandexweatherapp.repository

import com.vantula.yandexweatherapp.model.WeatherDTO
import com.vantula.yandexweatherapp.utils.YANDEX_API_KEY
import com.vantula.yandexweatherapp.utils.YANDEX_API_URL_END_POINT
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface WeatherAPI {
    @GET(YANDEX_API_URL_END_POINT)
    fun getWeather(
        @Header(YANDEX_API_KEY) apikey: String,
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("ru_RU") lang: String,
    ): Call<WeatherDTO>
}