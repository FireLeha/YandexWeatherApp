package com.vantula.yandexweatherapp.viewmodel

import com.vantula.yandexweatherapp.model.Weather

sealed class AppStateMainFragment {
    data class Loading(val progress:Int):AppStateMainFragment()
    data class Success  (val weatherData: List<Weather>):AppStateMainFragment()
    data class Error( val error:Throwable):AppStateMainFragment()
}