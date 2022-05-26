package com.vantula.yandexweatherapp.view.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.load
import coil.request.ImageRequest
import com.google.android.material.snackbar.Snackbar
import com.vantula.yandexweatherapp.R
import com.vantula.yandexweatherapp.databinding.FragmentDetailsBinding
import com.vantula.yandexweatherapp.model.Weather
import com.vantula.yandexweatherapp.utils.BUNDLE_KEY
import com.vantula.yandexweatherapp.viewmodel.AppStateDetailsFragment
import com.vantula.yandexweatherapp.viewmodel.DetailsViewModel

class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding: FragmentDetailsBinding
        get() {
            return _binding!!
        }

    private val detailsViewModel: DetailsViewModel by lazy {
        ViewModelProvider(this).get(DetailsViewModel::class.java)
    }

    lateinit var localWeather: Weather

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        detailsViewModel.getLiveData().observe(viewLifecycleOwner) {
            renderData(it)
        }
        arguments?.let {
            it.getParcelable<Weather>(BUNDLE_KEY)?.let {
                localWeather = it
                detailsViewModel.getWeatherFromRemoteServer(localWeather.city.lat,
                    localWeather.city.lon,
                    localWeather.lang)
            }
        }
    }

    private fun renderData(appState: AppStateDetailsFragment) {
        when (appState) {
            is AppStateDetailsFragment.Error -> {

            }
            is AppStateDetailsFragment.Loading -> {

            }
            is AppStateDetailsFragment.Success -> {
                val weather = appState.weatherData
                setWeatherData(weather)
            }
        }
    }

    private fun setWeatherData(weather: Weather) {
        with(binding) {
            weather.city = localWeather.city
            saveWeather(weather)
            with(localWeather) {
                cityName.text = city.name
                cityCoordinates.text = "${city.lat} ${city.lon}"
                temperatureValue.text = "${weather.temperature}"
                feelsLikeValue.text = "${weather.feelsLike}"
                conditionValue.text = detailsViewModel.translateConditionEngToRus(weather.condition)
                headerIcon.load("https://freepngimg.com/thumb/city/36275-3-city-hd.png")
                weatherIcon.loadUrl("https://yastatic.net/weather/i/icons/funky/dark/${weather.icon}.svg")
            }
        }
    }

    private fun FragmentDetailsBinding.saveWeather(weather: Weather) {
        saveWeatherIcon.setImageResource(R.drawable.save_icon)
        saveWeatherIcon.setOnClickListener {
            detailsViewModel.saveWeather(weather)
            Snackbar.make(mainView, "Погода сохранена", Snackbar.LENGTH_LONG).show()
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }


    companion object {
        fun newInstance(bundle: Bundle): DetailsFragment = DetailsFragment().apply {
            arguments = bundle
        }
    }

}