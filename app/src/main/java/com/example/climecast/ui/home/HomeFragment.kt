package com.example.climecast.ui.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import com.google.android.gms.location.LocationRequest
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.climecast.database.LocationsLocalDataSourceImpl
import com.example.climecast.databinding.FragmentHomeBinding
import com.example.climecast.model.DailyData
import com.example.climecast.model.HourlyData
import com.example.climecast.model.WeatherResponse
import com.example.climecast.model.WeatherRepositoryImpl
import com.example.climecast.network.ApiState
import com.example.climecast.network.WeatherRemoteDataSourceImpl
import com.example.climecast.ui.home.adapters.DaysWeatherDataAdapter
import com.example.climecast.ui.home.adapters.HoursWeatherDataAdapter
import com.example.climecast.ui.home.viewmodel.HomeViewModel
import com.example.climecast.ui.home.viewmodel.HomeViewModelFactory
import com.example.climecast.util.SharedPreferencesManger
import com.example.climecast.util.WeatherUtils
import com.example.climecast.util.WeatherUtils.Companion.kelvinToCelsius
import com.example.climecast.util.WeatherUtils.Companion.kelvinToFahrenheit
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices.getFusedLocationProviderClient
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class HomeFragment : Fragment() {

    companion object {
        private const val REQUEST_LOCATION_CODE = 505
        private const val TAG = "HomeFragment"
    }

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: HomeViewModel
    private lateinit var homeViewModelFactory: HomeViewModelFactory

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private lateinit var daysWeatherDataAdapter: DaysWeatherDataAdapter
    private lateinit var hoursWeatherDataAdapter: HoursWeatherDataAdapter
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewModel()
        setupObservers()
        setupLocation()
    }

    private fun setupViewModel() {
        homeViewModelFactory = HomeViewModelFactory(
            WeatherRepositoryImpl.getInstance(
                WeatherRemoteDataSourceImpl.getInstance(),
                LocationsLocalDataSourceImpl.getInstance(requireActivity())
            )
        )
        viewModel = ViewModelProvider(this, homeViewModelFactory)[HomeViewModel::class.java]
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            viewModel.weatherForecastStateFlow.collect { result ->
                when (result) {
                    is ApiState.Success -> {
                        updateUI(result.data)
                        binding.homeProgressBar.visibility = View.GONE
                    }

                    is ApiState.Error -> {
                        Log.i(TAG, "onViewCreated: $result")
                        binding.homeProgressBar.visibility = View.GONE
                    }

                    ApiState.Loading -> {
                        binding.homeProgressBar.visibility = View.VISIBLE
                    }
                }
            }
        }
    }
    private fun updateUI(data: WeatherResponse) {
        setUpDaysAdapter(data.daily)
        setUpHoursAdapter(data.hourly)

        val tempUnit = SharedPreferencesManger.getSharedPreferencesManagerTempUnit(requireActivity())
        val windUnit = SharedPreferencesManger.getSharedPreferencesManagerWindUnit(requireActivity())

        binding.currentTemperatureTextView.text = when (tempUnit) {
            "celsius" -> WeatherUtils.kelvinToCelsius(data.current.temperature)
            "Fahrenheit" -> WeatherUtils.kelvinToFahrenheit(data.current.temperature)
            else -> data.current.temperature.toString()
        }

        binding.windTextView.text = when (windUnit) {
            "meter/second" -> "${data.current.windSpeed} m/s"
            else -> "${WeatherUtils.meterPerSecondToMilesPerHour(data.current.windSpeed)} m/h"
        }

        Glide.with(requireActivity())
            .load("https://openweathermap.org/img/wn/${data.current.weather[0].icon}@2x.png")
            .into(binding.currentWeatherImageView)

        binding.humidityTextView.text = data.current.humidity.toString()
        binding.cloudsTextView.text = data.current.clouds.toString()
        binding.currentCityTextView.text = getCityFromLocation(latitude, longitude)
    }

    private fun setUpHoursAdapter(hourlyDataList: List<HourlyData>) {
        hoursWeatherDataAdapter = HoursWeatherDataAdapter(hourlyDataList, requireActivity())
        with(binding.hoursRecyclerView) {
            adapter = hoursWeatherDataAdapter
            layoutManager = LinearLayoutManager(requireActivity(), RecyclerView.HORIZONTAL, false)
        }
    }

    private fun setUpDaysAdapter(dailyDataList: List<DailyData>) {
        daysWeatherDataAdapter = DaysWeatherDataAdapter(dailyDataList, requireActivity())
        with(binding.dayRecyclerView) {
            adapter = daysWeatherDataAdapter
            layoutManager = LinearLayoutManager(requireActivity(), RecyclerView.VERTICAL, false)
        }
    }

    private fun setupLocation() {
        val language=SharedPreferencesManger.getSharedPreferencesManagerLanguage(requireActivity())

        if (checkPermissions()) {
            if (isLocationEnabled()) {
                requestLocationUpdates(language)
            } else {
                enableLocationServices()
            }
        } else {
            requestLocationPermissions()
        }
    }

    private fun requestLocationUpdates(language:String) {
        fusedLocationProviderClient = getFusedLocationProviderClient(requireActivity())
        if (ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationProviderClient.requestLocationUpdates(
            LocationRequest.create().apply {
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            },
            object : LocationCallback() {
                override fun onLocationResult(p0: LocationResult) {
                    super.onLocationResult(p0)
                    val location = p0.lastLocation
                    latitude = location!!.latitude
                    longitude = location!!.longitude
                    viewModel.getWeatherForecast(latitude, longitude,language)
                    fusedLocationProviderClient.removeLocationUpdates(this)
                }
            },
            Looper.getMainLooper()
        )
    }

    private fun enableLocationServices() {
        Toast.makeText(requireActivity(), "Turn on your location", Toast.LENGTH_LONG).show()
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        startActivity(intent)
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    private fun checkPermissions(): Boolean {
        return (ContextCompat.checkSelfPermission(
            requireActivity(),
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED)
                || (ContextCompat.checkSelfPermission(
            requireActivity(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED)
    }

    private fun requestLocationPermissions() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            REQUEST_LOCATION_CODE
        )
    }

    private fun getCityFromLocation(latitude: Double, longitude: Double): String {
        val geocoder = Geocoder(requireContext())
        val addresses: List<Address>? = geocoder.getFromLocation(latitude, longitude, 1)
        val cityName = addresses?.get(0)?.adminArea
        val countryName = addresses?.get(0)?.countryName
        return "$cityName $countryName"
    }

    private fun convertUnixTimestampToFormattedDate(unixTimestamp: Long): String {
        val dateFormat = SimpleDateFormat("EEE, dd MMMM, HH:mm", Locale.getDefault())
        val date = Date(unixTimestamp * 1000)
        return dateFormat.format(date)
    }



}