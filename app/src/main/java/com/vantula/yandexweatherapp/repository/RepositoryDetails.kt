package com.vantula.yandexweatherapp.repository

import com.vantula.yandexweatherapp.model.WeatherDTO
import retrofit2.Callback

interface RepositoryDetails {
    fun getWeatherFromServer(lat:Double,lon:Double, lang:String, callback: Callback<WeatherDTO>)
}