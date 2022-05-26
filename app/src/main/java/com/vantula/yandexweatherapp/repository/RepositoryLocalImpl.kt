package com.vantula.yandexweatherapp.repository

import com.vantula.yandexweatherapp.model.City
import com.vantula.yandexweatherapp.model.Weather
import com.vantula.yandexweatherapp.model.getRussianCities
import com.vantula.yandexweatherapp.model.getWorldCities
import com.vantula.yandexweatherapp.room.HistoryWeatherEntity
import com.vantula.yandexweatherapp.utils.App

class RepositoryLocalImpl : RepositoryCitiesList, RepositoryHistoryWeather {
    override fun getWeatherFromLocalStorageRus(): List<Weather> {
        return getRussianCities()
    }

    override fun getWeatherFromLocalStorageWorld(): List<Weather> {
        return getWorldCities()
    }

    override fun getAllHistoryWeather(): List<Weather> {
        return convertHistoryWeatherEntityToWeather(App.getHistoryWeatherDao()
            .getAllHistoryWeather())
    }

    override fun saveWeather(weather: Weather) {
        App.getHistoryWeatherDao().insert(convertWeatherToHistoryWeatherEntity(weather))
    }

    override fun deleteSingleHistoryWeather(weather: Weather) {
        App.getHistoryWeatherDao().delete(convertWeatherToHistoryWeatherEntity(weather))
    }

    private fun convertHistoryWeatherEntityToWeather(entityList: List<HistoryWeatherEntity>): List<Weather> {
        return entityList.map {
            Weather(
                City(it.city, it.lat, it.lon),
                it.temperature,
                it.feelsLike,
                it.icon,
                it.condition,
                it.id
            )
        }
    }

    private fun convertWeatherToHistoryWeatherEntity(weather: Weather): HistoryWeatherEntity {
        return HistoryWeatherEntity(weather.id,
            weather.city.name,
            weather.city.lat,
            weather.city.lon,
            weather.temperature,
            weather.feelsLike,
            weather.icon,
            weather.condition)
    }

}