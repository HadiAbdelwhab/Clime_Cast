package com.example.climecast.ui.favourite

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.climecast.R
import com.example.climecast.databinding.FragmentAlertsBinding
import com.example.climecast.databinding.FragmentFavouriteBinding
import com.example.climecast.ui.MapFragment

class FavouriteFragment : Fragment() {

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
        binding.addNewLoactionButton.setOnClickListener {
           /* val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.nav_host_fragment, MapFragment()) // Replace MapFragment with your actual map fragment
            transaction.addToBackStack(null)
            transaction.commit()*/























            val action = FavouriteFragmentDirections.actionFavouriteFragmentToMapFragment()
            findNavController().navigate(action)


        }
    }

}