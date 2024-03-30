package com.example.climecast.ui.favourite

import com.example.climecast.model.Location

interface FavouriteClickListener {

    fun onDeleteClick(location: Location)

    fun onShowDetails(location: Location)
}