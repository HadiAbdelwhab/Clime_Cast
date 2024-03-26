package com.example.climecast.ui.map

import android.location.Address
import android.location.Geocoder
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.climecast.R
import com.example.climecast.database.Location
import com.example.climecast.database.WeatherLocalDataSourceImpl
import com.example.climecast.databinding.FragmentMapsBinding
import com.example.climecast.model.WeatherRepositoryImpl
import com.example.climecast.network.WeatherRemoteDataSourceImpl
import com.example.climecast.ui.map.viewmodel.MapViewModel
import com.example.climecast.ui.map.viewmodel.MapViewModelFactory
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMapClickListener {

    private var _binding: FragmentMapsBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: MapViewModel
    private lateinit var factory: MapViewModelFactory

    private var googleMap: GoogleMap? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
        setUpViewModel()
        setListeners()
    }

    override fun onMapReady(gMap: GoogleMap) {
        googleMap = gMap
        val sydney = LatLng(-34.0, 151.0)
        gMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        gMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
        gMap.setOnMapClickListener(this)
    }

    override fun onMapClick(latLng: LatLng) {
        // Handle map click event
        googleMap?.clear() // Clear existing markers
        googleMap?.addMarker(MarkerOptions().position(latLng)) // Add a new marker
    }

    private fun setUpViewModel() {
        factory = MapViewModelFactory(
            WeatherRepositoryImpl.getInstance(
                WeatherRemoteDataSourceImpl.getInstance(),
                WeatherLocalDataSourceImpl.getInstance(requireActivity())
            )
        )
        viewModel = ViewModelProvider(this, factory)[MapViewModel::class.java]
    }

    private fun setListeners() {
        binding.chooseLocationButton.setOnClickListener {
            googleMap?.let { map ->
                val location = getCurrentLocation(map)
                val chosenCity = getCityFromLocation(location.latitude, location.longitude)

                if (chosenCity.isEmpty() || chosenCity == "City not found") {
                    showToast("Please select a valid location.")
                } else {
                    viewModel.addLocationToFavourite(
                        Location(
                            longitude = location.longitude,
                            latitude = location.latitude,
                            city = chosenCity
                        )
                    )
                    val action = MapsFragmentDirections.actionMapsFragmentToFavouriteFragment()
                    findNavController().navigate(action)
                }
            }
        }
    }


    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }


    private fun getCurrentLocation(googleMap: GoogleMap): LatLng {
        val cameraPosition = googleMap.cameraPosition.target
        return LatLng(cameraPosition.latitude, cameraPosition.longitude)
    }

    private fun getCityFromLocation(latitude: Double, longitude: Double): String {
        val geocoder = Geocoder(requireActivity())
        val addresses: MutableList<Address>? = geocoder.getFromLocation(latitude, longitude, 1)
        var cityText = "City not found"
        if (!addresses.isNullOrEmpty()) {
            val address = addresses[0]
            cityText = address.locality ?: address.subAdminArea ?: address.adminArea ?: cityText
        }
        return cityText
    }
}

