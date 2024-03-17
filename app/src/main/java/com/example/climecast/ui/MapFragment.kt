package com.example.climecast.ui

import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.climecast.databinding.FragmentMapBinding
import org.osmdroid.config.Configuration
import org.osmdroid.api.IMapController
import org.osmdroid.library.BuildConfig
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import org.osmdroid.tileprovider.tilesource.TileSourceFactory

class MapFragment : Fragment() {

    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!

    private lateinit var mapController: IMapController
    private lateinit var locationOverlay: MyLocationNewOverlay

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeMapView()
        initializeLocationOverlay()
        centerMapToUserLocation()

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
    }

    private fun centerMapToUserLocation() {
        locationOverlay.runOnFirstFix {
            activity?.runOnUiThread {
                binding.mapView.overlays.clear()
                binding.mapView.overlays.add(locationOverlay)
                mapController.animateTo(locationOverlay.myLocation)
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
