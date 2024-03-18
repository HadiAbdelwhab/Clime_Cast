package com.example.climecast.ui.home.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.climecast.R
import com.example.climecast.model.DailyData
import java.text.SimpleDateFormat
import java.util.*

class DaysWeatherDataAdapter(
    private val dailyData: List<DailyData>
) : RecyclerView.Adapter<DaysWeatherDataAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dayTextView: TextView = itemView.findViewById(R.id.day_text_view)
        val descriptionTextView: TextView = itemView.findViewById(R.id.day_weather_description)
        val dayMaxDegree: TextView = itemView.findViewById(R.id.day_max_degree)
        val dayMinDegree: TextView = itemView.findViewById(R.id.day_min_degree)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.day_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dailyData.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dailyItem = dailyData[position]
        val formattedDate = formatDate(dailyItem.timestamp)
        holder.dayTextView.text = formattedDate
        holder.descriptionTextView.text = dailyItem.weather[0].description
        holder.dayMaxDegree.text = dailyItem.temperature.max.toString()
        holder.dayMinDegree.text = dailyItem.temperature.min.toString()
    }

    private fun formatDate(timestamp: Long): String {
        val calendar = Calendar.getInstance().apply {
            timeInMillis = timestamp * 1000 // Convert seconds to milliseconds
        }
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }
}
