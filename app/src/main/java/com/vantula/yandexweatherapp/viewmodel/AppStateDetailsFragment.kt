package com.vantula.yandexweatherapp.viewmodel

import com.vantula.yandexweatherapp.model.Weather

sealed class AppStateDetailsFragment {
    data class Loading(val progress:Int):AppStateDetailsFragment()
    data class Success  (val weatherData: Weather):AppStateDetailsFragment()
    data class Error( val error:Throwable):AppStateDetailsFragment()
}