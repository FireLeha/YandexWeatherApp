package com.vantula.yandexweatherapp.utils

import android.app.Application
import androidx.room.Room
import com.google.gson.GsonBuilder
import com.vantula.yandexweatherapp.repository.WeatherAPI
import com.vantula.yandexweatherapp.room.HistoryDatabase
import com.vantula.yandexweatherapp.room.HistoryWeatherDao
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        appInstance = this
    }

    companion object {
        private var appInstance: App? = null
        const val DB_NAME = "History.db"
        private var db: HistoryDatabase? = null

        val retrofit = Retrofit.Builder()
            .baseUrl(YANDEX_API_URL)
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder().setLenient().create()
                )
            )
            .build().create(WeatherAPI::class.java)

        fun getHistoryWeatherDao(): HistoryWeatherDao {
            if (db == null) {
                if (appInstance == null) {
                    throw  IllformedLocaleException("Все очень плохо")
                } else {
                    db = Room.databaseBuilder(appInstance!!.applicationContext,
                        HistoryDatabase::class.java,
                        DB_NAME)
                        .build()
                }
            }
            return db!!.historyWeatherDao()
        }
    }

}