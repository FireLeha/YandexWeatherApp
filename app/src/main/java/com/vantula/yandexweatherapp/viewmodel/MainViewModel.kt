package com.vantula.yandexweatherapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vantula.yandexweatherapp.repository.RepositoryLocalImpl

class MainViewModel(
    private val liveData: MutableLiveData<AppStateMainFragment> = MutableLiveData(),
    private val repositoryLocalImpl: RepositoryLocalImpl = RepositoryLocalImpl(),
) : ViewModel() {



    fun getLiveData(): LiveData<AppStateMainFragment> = liveData

    fun getWeatherFromLocalStorageRus() = getWeatherFromLocalServer(true)
    fun getWeatherFromLocalStorageWorld() = getWeatherFromLocalServer(false)

    private fun getWeatherFromLocalServer(isRussian: Boolean) {
        liveData.postValue(AppStateMainFragment.Loading(0))
        Thread {
            with(repositoryLocalImpl) {
                if (isRussian) {
                    liveData.postValue(AppStateMainFragment.Success(getWeatherFromLocalStorageRus()))
                } else if (!isRussian) {
                    liveData.postValue(AppStateMainFragment.Success(getWeatherFromLocalStorageWorld()))
                } else {
                    liveData.postValue(AppStateMainFragment.Error(IllegalStateException("")))
                }
            }
        }.start()
    }

}