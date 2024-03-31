package com.example.climecast.ui.favourite

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.climecast.database.WeatherLocalDataSourceImpl
import com.example.climecast.databinding.FragmentFavouritDetailsBinding
import com.example.climecast.model.WeatherData
import com.example.climecast.model.WeatherResponse
import com.example.climecast.network.ApiState
import com.example.climecast.network.WeatherRemoteDataSourceImpl
import com.example.climecast.repository.WeatherRepositoryImpl
import com.example.climecast.ui.favourite.viewmodel.FavouriteViewModel
import com.example.climecast.ui.favourite.viewmodel.FavouriteViewModelFactory
import com.example.climecast.util.SharedPreferencesManger
import com.example.climecast.util.WeatherUtils
import com.google.gson.Gson
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private const val TAG = "FavouriteDetailsFragmen"

class FavouriteDetailsFragment : Fragment() {

    private lateinit var viewModel: FavouriteViewModel
    private lateinit var favouriteViewModelFactory: FavouriteViewModelFactory


    private var _binding: FragmentFavouritDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavouritDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpViewModel()
        val args = FavouriteDetailsFragmentArgs.fromBundle(requireArguments())
        val language =
            SharedPreferencesManger.getSharedPreferencesManagerLanguage(requireActivity())

        viewModel.getWeatherForecast(
            args.latitude.toDouble(),
            args.longitude.toDouble(),
            language
        )
        setupObservers()

    }

    private fun setUpViewModel() {
        favouriteViewModelFactory = FavouriteViewModelFactory(
            WeatherRepositoryImpl.getInstance(
                WeatherRemoteDataSourceImpl.getInstance(),
                WeatherLocalDataSourceImpl.getInstance(requireActivity())
            )
        )
        viewModel =
            ViewModelProvider(this, favouriteViewModelFactory)[FavouriteViewModel::class.java]
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            viewModel.weatherForecastStateFlow.collect { result ->
                when (result) {
                    is ApiState.Success -> {
                        binding.currentWeatherCard.visibility = View.VISIBLE
                        updateUI(result.data)


                        //Log.i(TAG, "setupObservers: " + json)
                        binding.favouriteProgressBar.visibility = View.GONE
                    }

                    is ApiState.Error -> {
                        Log.i(TAG, "onViewCreated: $result")
                        binding.favouriteProgressBar.visibility = View.GONE

                    }

                    ApiState.Loading -> {
                        binding.favouriteProgressBar.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun updateUI(data: WeatherResponse) {
        val tempUnit =
            SharedPreferencesManger.getSharedPreferencesManagerTempUnit(requireActivity())
        val windUnit =
            SharedPreferencesManger.getSharedPreferencesManagerWindUnit(requireActivity())

        binding.currentTemperatureTextView.text = when (tempUnit) {
            "celsius" -> WeatherUtils.kelvinToCelsius(data.current.temperature)
            "Fahrenheit" -> WeatherUtils.kelvinToFahrenheit(data.current.temperature)
            else -> data.current.temperature.toString()
        }

        binding.currentDataAndTimeTextView.text =
            convertUnixTimestampToFormattedDate(data.current.timestamp)

        binding.currentWeatherDescriptionTextView.text = data.current.weather[0].description
        binding.windTextView.text = when (windUnit) {
            "meter/second" -> "${data.current.windSpeed} m/s"
            else -> "${WeatherUtils.meterPerSecondToMilesPerHour(data.current.windSpeed)} m/h"
        }

        Glide.with(requireActivity())
            .load("https://openweathermap.org/img/wn/${data.current.weather[0].icon}@2x.png")
            .into(binding.currentWeatherImageView)

        binding.humidityTextView.text = data.current.humidity.toString()
        binding.cloudsTextView.text = data.current.clouds.toString()


    }

    private fun convertUnixTimestampToFormattedDate(unixTimestamp: Long): String {
        val dateFormat = SimpleDateFormat("EEE, dd MMMM, HH:mm", Locale.getDefault())
        val date = Date(unixTimestamp * 1000)
        return dateFormat.format(date)
    }


}