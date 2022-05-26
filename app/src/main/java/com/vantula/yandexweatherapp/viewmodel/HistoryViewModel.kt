package com.vantula.yandexweatherapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vantula.yandexweatherapp.model.Weather
import com.vantula.yandexweatherapp.repository.RepositoryLocalImpl

class HistoryViewModel(
    private val liveData: MutableLiveData<AppStateMainFragment> = MutableLiveData(),
    private val repositoryLocalImpl: RepositoryLocalImpl = RepositoryLocalImpl(),
) : ViewModel() {

    fun getLiveData(): LiveData<AppStateMainFragment> = liveData

    fun getAllHistory() {
        Thread {
            val listWeather = repositoryLocalImpl.getAllHistoryWeather()
            liveData.postValue(AppStateMainFragment.Success(listWeather))
        }.start()

    }

    fun deleteSingleHistory(weather: Weather) {
        Thread {
            repositoryLocalImpl.deleteSingleHistoryWeather(weather)
        }.start()
    }

}