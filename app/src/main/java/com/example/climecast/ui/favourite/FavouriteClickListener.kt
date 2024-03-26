package com.example.climecast.ui.favourite

import com.example.climecast.database.Location

interface FavouriteClickListener {

    fun onDeleteClick(location: Location)
}