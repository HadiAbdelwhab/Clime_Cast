import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.climecast.R
import com.example.climecast.database.Location
import com.example.climecast.database.WeatherLocalDataSourceImpl
import com.example.climecast.databinding.FragmentMapBinding
import com.example.climecast.model.LatLong
import com.example.climecast.model.WeatherRepositoryImpl
import com.example.climecast.network.WeatherRemoteDataSourceImpl
import com.example.climecast.ui.map.viewmodel.MapViewModel
import com.example.climecast.ui.map.viewmodel.MapViewModelFactory
import org.osmdroid.config.Configuration
import org.osmdroid.api.IMapController
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.library.BuildConfig
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import org.osmdroid.views.overlay.Marker
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.views.overlay.MapEventsOverlay

class MapFragment : Fragment() {

    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!

    private lateinit var mapController: IMapController
    private lateinit var locationOverlay: MyLocationNewOverlay
    private lateinit var selectedLocationMarker: Marker

    private lateinit var viewModel: MapViewModel
    private lateinit var factory: MapViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        factory = MapViewModelFactory(
            WeatherRepositoryImpl.getInstance(
                WeatherRemoteDataSourceImpl.getInstance(),
                WeatherLocalDataSourceImpl.getInstance(requireActivity())
            )
        )

        viewModel = ViewModelProvider(this, factory)[MapViewModel::class.java]

        initializeMapView()
        initializeLocationOverlay()
        centerMapToUserLocation()
        setListeners()
    }

    private fun setListeners() {
        binding.chooseLocationButton.setOnClickListener {

            if (::selectedLocationMarker.isInitialized) {
                val selectedLocation = selectedLocationMarker.position
                val location = LatLong()

                location.lat = selectedLocation.latitude
                location.long = selectedLocation.longitude

                val favouriteLocation = Location(
                    getCityFromLocation(location.lat, location.long), location.lat, location.long
                )


                viewModel.addLocationToFavourite(favouriteLocation)

                val action = MapFragmentDirections.actionMapFragmentToFavouriteFragment()
                findNavController().navigate(action)
            } else {
                Toast.makeText(requireContext(), "Select a location first", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        binding.mapView.overlays.add(MapEventsOverlay(object : MapEventsReceiver {
            override fun singleTapConfirmedHelper(p: GeoPoint?): Boolean {
                return true
            }

            override fun longPressHelper(p: GeoPoint?): Boolean {
                p?.let {
                    updateSelectedLocationMarker(it)
                }
                return false
            }
        }))
    }

    private fun updateSelectedLocationMarker(location: GeoPoint) {
        if (!::selectedLocationMarker.isInitialized) {
            selectedLocationMarker = Marker(binding.mapView).apply {
                setIcon(resources.getDrawable(R.drawable.ic_location_pin))
                setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            }
            binding.mapView.overlays.add(selectedLocationMarker)
        }

        selectedLocationMarker.position = location
        selectedLocationMarker.setInfoWindow(null)
        selectedLocationMarker.title = "Selected Location"
        selectedLocationMarker.snippet = "${location.latitude}, ${location.longitude}"
        selectedLocationMarker.showInfoWindow()
        binding.mapView.invalidate()
    }

    private fun initializeMapView() {
        Configuration.getInstance()
            .load(requireContext(), PreferenceManager.getDefaultSharedPreferences(requireContext()))
        Configuration.getInstance().userAgentValue = BuildConfig.LIBRARY_PACKAGE_NAME

        binding.mapView.apply {
            setUseDataConnection(true)
            setTileSource(TileSourceFactory.MAPNIK)
            setMultiTouchControls(true)
            mapController = controller
            mapController.setZoom(14)
        }
    }

    private fun initializeLocationOverlay() {
        val gpsMyLocationProvider = GpsMyLocationProvider(requireActivity())
        locationOverlay = MyLocationNewOverlay(gpsMyLocationProvider, binding.mapView).apply {
            enableMyLocation()
            enableFollowLocation()
        }
        binding.mapView.overlays.add(locationOverlay)
    }

    private fun centerMapToUserLocation() {
        locationOverlay.runOnFirstFix {
            activity?.runOnUiThread {
                mapController.animateTo(locationOverlay.myLocation)
            }
        }
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}
