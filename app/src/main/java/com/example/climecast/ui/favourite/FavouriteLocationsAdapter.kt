package com.example.climecast.ui.favourite

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.climecast.R
import com.example.climecast.database.Location

class FavouriteLocationsAdapter(
    private val locations: List<Location>,
    private val listener: FavouriteClickListener
) : RecyclerView.Adapter<FavouriteLocationsAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val locationNameTextView: TextView = itemView.findViewById(R.id.location_name_text_view)
        val deleteLocationImageView: ImageView =
            itemView.findViewById(R.id.delete_favourite_item_image_view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.favourite_locations_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return locations.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.locationNameTextView.text = locations[position].city
        holder.deleteLocationImageView.setOnClickListener {
            listener.onDeleteClick(locations[position])
        }

    }
}