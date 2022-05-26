package com.vantula.yandexweatherapp.view.history

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.vantula.yandexweatherapp.R
import com.vantula.yandexweatherapp.databinding.FragmentHistoryBinding
import com.vantula.yandexweatherapp.model.Weather
import com.vantula.yandexweatherapp.utils.BUNDLE_KEY
import com.vantula.yandexweatherapp.view.details.DetailsFragment
import com.vantula.yandexweatherapp.view.main.OnMyItemClickListener
import com.vantula.yandexweatherapp.viewmodel.AppStateMainFragment
import com.vantula.yandexweatherapp.viewmodel.HistoryViewModel

class HistoryFragment : Fragment(), OnMyItemClickListener {

    private var _binding: FragmentHistoryBinding? = null
    private val binding: FragmentHistoryBinding
        get() {
            return _binding!!
        }

    private val adapter: CitiesHistoryAdapter by lazy {
        CitiesHistoryAdapter(this)
    }

    private val viewModel: HistoryViewModel by lazy {
        ViewModelProvider(this).get(HistoryViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getLiveData().observe(viewLifecycleOwner) { renderData(it) }
        viewModel.getAllHistory()
        binding.historyFragmentRecyclerview.adapter = adapter
    }

    private fun renderData(appStateMainFragment: AppStateMainFragment) {
        with(binding) {
            when (appStateMainFragment) {
                is AppStateMainFragment.Error -> {}
                is AppStateMainFragment.Loading -> {}
                is AppStateMainFragment.Success -> {
                    adapter.setWeather(appStateMainFragment.weatherData)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        fun newInstance() = HistoryFragment()
    }

    override fun onItemClick(weather: Weather) {
        Bundle().apply {
            putParcelable(BUNDLE_KEY, weather)
            activity?.run {
                supportFragmentManager.beginTransaction()
                    .add(R.id.container, DetailsFragment.newInstance(bundle = this@apply))
                    .addToBackStack("").commit()
            }
        }
    }

    override fun deleteWeatherFun(weather: Weather) {
        val builder = AlertDialog.Builder(requireContext())
        with(builder) {
            setPositiveButton("Да") { _, _ ->
                Thread {
                    viewModel.deleteSingleHistory(weather)
                    viewModel.getAllHistory()
                }.start()
            }
            setNegativeButton("Нет") { _, _ -> }
            setTitle("Удалить ${weather.city.name}?")
            setMessage("Вы уверены, что хотите удалить ${weather.city.name}?")
            create().show()
        }

    }


}