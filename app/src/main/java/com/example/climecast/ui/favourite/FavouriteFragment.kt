package com.example.climecast.ui.favourite

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.climecast.model.Location
import com.example.climecast.database.WeatherLocalDataSourceImpl
import com.example.climecast.databinding.FragmentFavouriteBinding
import com.example.climecast.repository.WeatherRepositoryImpl
import com.example.climecast.network.WeatherRemoteDataSourceImpl
import com.example.climecast.ui.favourite.adapters.FavouriteClickListener
import com.example.climecast.ui.favourite.adapters.FavouriteLocationsAdapter
import com.example.climecast.ui.favourite.viewmodel.FavouriteViewModel
import com.example.climecast.ui.favourite.viewmodel.FavouriteViewModelFactory
import kotlinx.coroutines.launch

class FavouriteFragment : Fragment(), FavouriteClickListener,
    FavouriteDialogFragment.FavouriteDialogListener {

    private lateinit var viewModel: FavouriteViewModel
    private lateinit var favouriteViewModelFactory: FavouriteViewModelFactory

    private lateinit var favouriteLocationAdapter: FavouriteLocationsAdapter

    private var deletedLocation: Location? = null

    private var _binding: FragmentFavouriteBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavouriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpViewModel()
        setListeners()
        setObserver()

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

    private fun setObserver() {
        lifecycleScope.launch {
            viewModel.favouriteLocationsStateFlow.collect { list ->
                _binding?.let {
                    setAdapter(list)
                }
            }
        }
    }

    private fun setListeners() {
        binding.addNewLoactionButton.setOnClickListener {
            val action = FavouriteFragmentDirections.actionFavouriteFragmentToMapsFragment()
            findNavController().navigate(action)
        }
    }

    private fun setAdapter(list: List<Location>) {
        favouriteLocationAdapter = FavouriteLocationsAdapter(list, this)
        binding.favouriteLocationsRecyclerView.apply {
            adapter = favouriteLocationAdapter
            layoutManager = LinearLayoutManager(requireActivity()).apply {
                orientation = RecyclerView.VERTICAL
            }
        }
        favouriteLocationAdapter.notifyDataSetChanged()
    }



    override fun onShowDetails(location: Location) {


    }
    override fun onDeleteClick(location: Location) {
        deletedLocation = location
        val alertFragmentDialog=FavouriteDialogFragment()
        alertFragmentDialog.setAlertDialogListener(this)
        alertFragmentDialog.show(parentFragmentManager,"alertFragmentDialog")

    }
    override fun onPositiveButtonClick() {
        deletedLocation?.let { viewModel.deleteLocationFromFavourite(it) }
        favouriteLocationAdapter.notifyDataSetChanged()
    }


}
