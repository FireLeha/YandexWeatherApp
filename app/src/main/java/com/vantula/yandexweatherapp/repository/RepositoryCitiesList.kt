package com.vantula.yandexweatherapp.repository

import com.vantula.yandexweatherapp.model.Weather

interface RepositoryCitiesList {
    fun getWeatherFromLocalStorageRus(): List<Weather>
    fun getWeatherFromLocalStorageWorld(): List<Weather>
}