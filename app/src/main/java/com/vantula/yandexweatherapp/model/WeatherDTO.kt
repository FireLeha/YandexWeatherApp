package com.vantula.yandexweatherapp.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class WeatherDTO(
    val now: Long,
    @SerializedName("now_dt")
    val nowDt: String,
    val info: Info,
    val fact: Fact,
) : Parcelable

@Parcelize
data class Fact(
    @SerializedName("obs_time")
    val obsTime: Long,
    val temp: Int,

    @SerializedName("feels_like")
    val feelsLike: Int,
    val icon: String,
    val condition: String,
) : Parcelable

@Parcelize
data class Info(
    val url: String,
    val lat: Double,
    val lon: Double,
) : Parcelable