package com.vantula.yandexweatherapp.repository


import com.vantula.yandexweatherapp.BuildConfig
import com.vantula.yandexweatherapp.model.WeatherDTO
import com.vantula.yandexweatherapp.utils.App
import retrofit2.Callback

class RepositoryRemoteImpl :RepositoryDetails {
    override fun getWeatherFromServer(lat: Double, lon: Double, lang: String, callback: Callback<WeatherDTO>) {
        App.retrofit.getWeather(BuildConfig.WEATHER_API_KEY,lat, lon, lang).enqueue(callback)
    }
}