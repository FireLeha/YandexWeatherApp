package com.vantula.yandexweatherapp.view.main

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.vantula.yandexweatherapp.R
import com.vantula.yandexweatherapp.databinding.FragmentMainBinding
import com.vantula.yandexweatherapp.model.Weather
import com.vantula.yandexweatherapp.utils.BUNDLE_KEY
import com.vantula.yandexweatherapp.utils.IS_RUSSIAN_KEY
import com.vantula.yandexweatherapp.utils.SHARED_PREFERENCES_KEY
import com.vantula.yandexweatherapp.view.details.DetailsFragment
import com.vantula.yandexweatherapp.viewmodel.AppStateMainFragment
import com.vantula.yandexweatherapp.viewmodel.MainViewModel

class MainFragment : Fragment(), OnMyItemClickListener {

    private var _binding: FragmentMainBinding? = null
    private val binding: FragmentMainBinding
        get() {
            return _binding!!
        }

    private val adapter: CitiesAdapter by lazy { CitiesAdapter(this) }
    private lateinit var sp: SharedPreferences

    private var isRussian = true

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sp = activity?.getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE)!!
        initView()
        viewModel.getLiveData().observe(viewLifecycleOwner) { renderData(it) }
    }

    private fun initView() {
        with(binding) {
            mainFragmentRecyclerView.adapter = adapter
            loadCitiesList()
            mainFragmentFAB.setOnClickListener {
                sentRequest()
            }
        }
    }

    private fun saveCitiesList(isRussian: Boolean) {
        val editor = sp.edit()
        editor?.putBoolean(IS_RUSSIAN_KEY, isRussian)
        editor?.apply()
    }

    private fun loadCitiesList() {
        if (sp.getBoolean(IS_RUSSIAN_KEY, false)) {
            viewModel.getWeatherFromLocalStorageRus()
            binding.mainFragmentFAB.setImageResource(R.drawable.ic_russia)
        } else {
            viewModel.getWeatherFromLocalStorageWorld()
            binding.mainFragmentFAB.setImageResource(R.drawable.ic_earth)
        }
    }

    private fun sentRequest() {
        isRussian = sp.getBoolean(IS_RUSSIAN_KEY, false)
        isRussian = !isRussian
        if (isRussian) {
            viewModel.getWeatherFromLocalStorageRus()
            binding.mainFragmentFAB.setImageResource(R.drawable.ic_russia)
            saveCitiesList(isRussian)
        } else {
            viewModel.getWeatherFromLocalStorageWorld()
            binding.mainFragmentFAB.setImageResource(R.drawable.ic_earth)
            saveCitiesList(isRussian)
        }
    }

    private fun renderData(appStateMainFragment: AppStateMainFragment) {
        with(binding) {
            when (appStateMainFragment) {
                is AppStateMainFragment.Error -> {
                    mainFragmentLoadingLayout.visibility = View.GONE
                    binding.root.showSnackBarWithAction(resources.getString(R.string.snackbar_with_action),
                        Snackbar.LENGTH_LONG)
                }
                is AppStateMainFragment.Loading -> {
                    mainFragmentLoadingLayout.visibility = View.VISIBLE
                }
                is AppStateMainFragment.Success -> {
                    mainFragmentLoadingLayout.visibility = View.GONE
                    adapter.setWeather(appStateMainFragment.weatherData)
                    binding.root.showSnackBarWithoutAction(
                        resources.getString(R.string.snackbar_without_action), Snackbar.LENGTH_LONG)
                }
            }
        }
    }

    private fun View.showSnackBarWithoutAction(text: String, length: Int) {
        Snackbar.make(this, text, length).show()
    }

    private fun View.showSnackBarWithAction(text: String, length: Int) {
        Snackbar.make(this, text, length)
            .setAction("Попробовать ещё раз")
            { sentRequest() }.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        fun newInstance() = MainFragment()
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
        TODO("Not yet implemented")
    }

}