package com.vantula.yandexweatherapp.view.main

import com.vantula.yandexweatherapp.model.Weather

interface OnMyItemClickListener {
    fun onItemClick(weather: Weather)
    fun deleteWeatherFun(weather: Weather)
}