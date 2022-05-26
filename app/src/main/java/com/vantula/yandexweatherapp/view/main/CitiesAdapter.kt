package com.vantula.yandexweatherapp.view.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.vantula.yandexweatherapp.R
import com.vantula.yandexweatherapp.model.Weather
import kotlinx.android.synthetic.main.fragment_main_recycler_city_item.view.*

class CitiesAdapter(val listener: OnMyItemClickListener) :
    RecyclerView.Adapter<CitiesAdapter.MainViewHolder>() {

    private var weatherData: List<Weather> = listOf()

    fun setWeather(data: List<Weather>) {
        this.weatherData = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): CitiesAdapter.MainViewHolder = MainViewHolder(LayoutInflater
        .from(parent.context)
        .inflate(R.layout.fragment_main_recycler_city_item, parent, false))


    override fun onBindViewHolder(holder: CitiesAdapter.MainViewHolder, position: Int) =
        holder.bind(this.weatherData[position])

    override fun getItemCount(): Int = weatherData.size

    inner class MainViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(weather: Weather) {
            with(itemView) {
                findViewById<TextView>(R.id.mainFragmentRecyclerItemTextView).text =
                    weather.city.name
                mainFragmentRecyclerItemTextView.setOnClickListener {
                    listener.onItemClick(weather)
                }
            }
        }
    }

}