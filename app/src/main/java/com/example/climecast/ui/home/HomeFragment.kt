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
import androidx.preference.PreferenceManager
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
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private const val REQUEST_LOCATION_CODE = 505
private const val TAG = "HomeFragment"

class HomeFragment : Fragment() {


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

    @SuppressLint("MissingPermission")
    override fun onStart() {
        super.onStart()
        //geocoder = Geocoder(this, Locale.getDefault())
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                getFreshLocation()
            } else {
                enableLocationServices()
            }
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ),
                REQUEST_LOCATION_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_LOCATION_CODE) {
            if (grantResults.size > 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getFreshLocation()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeViewModelFactory = HomeViewModelFactory(
            WeatherRepositoryImpl.getInstance(
                WeatherRemoteDataSourceImpl.getInstance(),
                LocationsLocalDataSourceImpl.getInstance(requireActivity())
            )
        )

        viewModel = ViewModelProvider(this, homeViewModelFactory)[HomeViewModel::class.java]
        lifecycleScope.launch {
            viewModel.weatherForecastStateFlow.collect { result ->
                when (result) {
                    is ApiState.Success -> {
                        Log.i(TAG, "onViewCreated: " + result.data)
                        updateUI(result.data)

                    }

                    is ApiState.Error -> Log.i(TAG, "onViewCreated: $result")
                    ApiState.Loading -> Toast.makeText(
                        requireActivity(),
                        "loading",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun updateUI(data: WeatherResponse) {


        //loadSettings(data.hourly, data.daily)
        setUpDaysAdapter(data.daily)
        setUpHoursAdapter(data.hourly)
        binding.currentTemperatureTextView.text = data.current.temperature.toString()

        Glide.with(requireActivity())
            .load("https://openweathermap.org/img/wn/" + data.current.weather[0].icon + "@2x.png")
            .into(binding.currentWeatherImageView)
        binding.humidityTextView.text = data.current.humidity.toString()
        binding.windTextView.text = data.current.windSpeed.toString()
        binding.cloudsTextView.text = data.current.clouds.toString()
        binding.currentCityTextView.text = getCityFromLocation(latitude, longitude)
    }

    private fun setUpHoursAdapter(hourlyDataList: List<HourlyData>) {
        hoursWeatherDataAdapter = HoursWeatherDataAdapter(hourlyDataList, requireActivity())
        binding.hoursRecyclerView.apply {
            adapter = hoursWeatherDataAdapter
            layoutManager = LinearLayoutManager(requireActivity()).apply {
                orientation = RecyclerView.HORIZONTAL
            }

        }
    }

    private fun setUpDaysAdapter(dailyDataList: List<DailyData>) {
        daysWeatherDataAdapter = DaysWeatherDataAdapter(dailyDataList, requireActivity())

        binding.dayRecyclerView.apply {
            adapter = daysWeatherDataAdapter
            layoutManager = LinearLayoutManager(requireActivity()).apply {
                orientation = RecyclerView.VERTICAL
            }
        }


    }

    @SuppressLint("MissingPermission")
    private fun getFreshLocation() {
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())
        fusedLocationProviderClient.requestLocationUpdates(
            LocationRequest.Builder(0).apply {
                setPriority(PRIORITY_HIGH_ACCURACY)
            }.build(),
            object : LocationCallback() {
                override fun onLocationResult(p0: LocationResult) {
                    super.onLocationResult(p0)

                    val location = p0.lastLocation
                    //longitudeTextView.text = location?.longitude?.toString() ?: "N/A"
                    //latitudeTextView.text = location?.latitude?.toString() ?: "N/A"

                    latitude = location?.latitude!!
                    longitude = location.longitude!!
                    viewModel.getWeatherForecast(latitude!!, longitude!!)


                }
            }, Looper.myLooper()
        )
    }

    private fun enableLocationServices() {
        Toast.makeText(requireActivity(), "Turn on your location", Toast.LENGTH_LONG).show()
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        startActivity(intent)
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
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

    private fun getCityFromLocation(latitude: Double, longitude: Double): String {
        val geocoder = Geocoder(requireContext())
        val addresses: List<Address>? = geocoder.getFromLocation(latitude, longitude, 1)
        Log.i(TAG, "getAddressFromLocation: " + addresses.toString())
        val cityName = addresses?.get(0)?.adminArea
        val countryName = addresses?.get(0)?.countryName

        return "${cityName ?: ""} $countryName"
    }

    fun convertUnixTimestampToFormattedDate(unixTimestamp: Long): String {
        val dateFormat = SimpleDateFormat("EEE, dd MMMM, HH:mm", Locale.getDefault())
        val date = Date(unixTimestamp * 1000)
        return dateFormat.format(date)
    }



   /* private fun loadSettings(hourlyDataList: List<HourlyData>, dailyDataList: List<DailyData>) {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireActivity())

        val temperatureUnit = sharedPreferences.getString("temp_unit", "Kelvin")
        if (temperatureUnit == "celsius") {
           )
        } else if (temperatureUnit == "Fahrenheit") {
            setUpDaysAdapter(dailyDataList)
            setUpHoursAdapter(hourlyDataList)
        }


    }*/


}