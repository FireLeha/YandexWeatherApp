package com.vantula.yandexweatherapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vantula.yandexweatherapp.model.Weather
import com.vantula.yandexweatherapp.model.WeatherDTO
import com.vantula.yandexweatherapp.model.getDefaultCity
import com.vantula.yandexweatherapp.repository.RepositoryLocalImpl
import com.vantula.yandexweatherapp.repository.RepositoryRemoteImpl
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailsViewModel(
    private val liveData: MutableLiveData<AppStateDetailsFragment> = MutableLiveData(),
    private val repositoryLocalImpl: RepositoryLocalImpl = RepositoryLocalImpl(),
) : ViewModel() {

    private val repositoryImpl: RepositoryRemoteImpl by lazy {
        RepositoryRemoteImpl()
    }

    fun saveWeather(weather: Weather) {
        Thread {
            repositoryLocalImpl.saveWeather(weather)
        }.start()

    }

    fun translateConditionEngToRus(conditionEng: String): String {
        when (conditionEng) {
            "clear" -> return "Ясно"
            "partly-cloudy" -> return "Малооблачно"
            "cloudy" -> return "Облачно с прояснениями"
            "overcast" -> return "Пасмурно"
            "drizzle" -> return "Морось"
            "light-rain" -> return "Небольшой дождь"
            "rain" -> return "Дождь"
            "moderate-rain" -> return "Умеренно сильный дождь"
            "heavy-rain" -> return "Сильный дождь"
            "continuous-heavy-rain" -> return "Длительный сильный дождь"
            "showers" -> return "Ливень"
            "wet-snow" -> return "Дождь со снегом"
            "light-snow" -> return "Небольшой снег"
            "snow" -> return "Снег"
            "snow-showers" -> return "Снегопад"
            "hail" -> return "Град"
            "thunderstorm" -> return "Гроза"
            "thunderstorm-with-rain" -> return "Дождь с грозой"
            "thunderstorm-with-hail" -> return "Гроза с градом"
            "night" -> return "Ночь"
            "morning" -> return "Утро"
            "day" -> return "День"
            "evening" -> return "вечер"
            else -> return "Ошибка"
        }
    }

    fun getLiveData(): LiveData<AppStateDetailsFragment> = liveData

    fun getWeatherFromRemoteServer(lat: Double, lon: Double, lang: String) {
        liveData.postValue(AppStateDetailsFragment.Loading(0))
        repositoryImpl.getWeatherFromServer(lat, lon, lang, callback)
    }

    fun converterDTOtoModel(weatherDTO: WeatherDTO): Weather {
        return Weather(getDefaultCity(),
            weatherDTO.fact.temp,
            weatherDTO.fact.feelsLike,
            weatherDTO.fact.icon,
            weatherDTO.fact.condition)
    }

    private val callback = object : Callback<WeatherDTO> {
        override fun onResponse(call: Call<WeatherDTO>, response: Response<WeatherDTO>) {
            if (response.isSuccessful) {
                response.body()?.let {
                    liveData.postValue(AppStateDetailsFragment.Success(converterDTOtoModel(it)))
                }
            } else {
                when (response.code()) {
                    403 -> {
                        liveData.postValue(AppStateDetailsFragment.Error(error("Forbidden")))
                    }
                    404 -> {
                        liveData.postValue(AppStateDetailsFragment.Error(error("Not Found")))
                    }
                }
            }
        }

        override fun onFailure(call: Call<WeatherDTO>, t: Throwable) {
            liveData.postValue(AppStateDetailsFragment.Error(error("Response failed")))
        }
    }

}