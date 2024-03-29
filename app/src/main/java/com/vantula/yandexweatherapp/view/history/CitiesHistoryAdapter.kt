package com.vantula.yandexweatherapp.view.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.vantula.yandexweatherapp.R
import com.vantula.yandexweatherapp.databinding.FragmentHistoryRecyclerCityItemBinding
import com.vantula.yandexweatherapp.model.Weather
import com.vantula.yandexweatherapp.view.main.OnMyItemClickListener

class CitiesHistoryAdapter(val listener: OnMyItemClickListener) :
    RecyclerView.Adapter<CitiesHistoryAdapter.HistoryCityViewHolder>() {

    private var weatherData: List<Weather> = listOf()

    fun setWeather(data: List<Weather>) {
        this.weatherData = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): CitiesHistoryAdapter.HistoryCityViewHolder = HistoryCityViewHolder(LayoutInflater
        .from(parent.context)
        .inflate(R.layout.fragment_history_recycler_city_item, parent, false))

    override fun onBindViewHolder(
        holder: CitiesHistoryAdapter.HistoryCityViewHolder,
        position: Int,
    ) =
        holder.bind(this.weatherData[position])


    override fun getItemCount(): Int = weatherData.size

    inner class HistoryCityViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(weather: Weather) {
            with(FragmentHistoryRecyclerCityItemBinding.bind(itemView)) {
                cityName.text = weather.city.name
                temperature.text = "${weather.temperature}"
                feelsLike.text = "${weather.feelsLike}"
                weatherIconSmall.loadUrl("https://yastatic.net/weather/i/icons/funky/dark/${weather.icon}.svg")
                deleteIcon.setImageResource(R.drawable.delete_icon)
                deleteIcon.setOnClickListener {
                    listener.deleteWeatherFun(weather)
                }
                cityName.setOnClickListener {
                    listener.onItemClick(weather)
                }
            }
        }
    }

    private fun ImageView.loadUrl(url: String) {
        val imageLoader = ImageLoader.Builder(this.context)
            .componentRegistry { add(SvgDecoder(this@loadUrl.context)) }
            .build()

        val request = ImageRequest.Builder(this.context)
            .data(url)
            .target(this)
            .build()

        imageLoader.enqueue(request)
    }
}