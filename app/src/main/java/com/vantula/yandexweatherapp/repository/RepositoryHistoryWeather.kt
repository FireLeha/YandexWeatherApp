package com.vantula.yandexweatherapp.repository

import com.vantula.yandexweatherapp.model.Weather

interface RepositoryHistoryWeather {
    fun getAllHistoryWeather():List<Weather>
    fun saveWeather(weather: Weather)
    fun deleteSingleHistoryWeather(weather: Weather)
}