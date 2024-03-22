package com.example.climecast.ui.home.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.climecast.R
import com.example.climecast.model.DailyData
import com.example.climecast.util.SharedPreferencesManger
import com.example.climecast.util.WeatherUtils.Companion.kelvinToCelsius
import com.example.climecast.util.WeatherUtils.Companion.kelvinToFahrenheit
import java.text.SimpleDateFormat
import java.util.*

class DaysWeatherDataAdapter(
    private val dailyData: List<DailyData>,
    private val context: Context,
) : RecyclerView.Adapter<DaysWeatherDataAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val dayTextView: TextView = itemView.findViewById(R.id.day_text_view)
        internal val descriptionTextView: TextView =
            itemView.findViewById(R.id.day_weather_description)
        internal val dayMaxDegree: TextView = itemView.findViewById(R.id.day_max_degree)
        internal val dayMinDegree: TextView = itemView.findViewById(R.id.day_min_degree)
        internal val weatherIcon: ImageView = itemView.findViewById(R.id.day_weather_image_view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.day_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dailyData.size
    }

    @SuppressLint("CheckResult")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dailyItem = dailyData[position]
        val formattedDate = if (position == 0) {
            "Tomorrow"
        } else {
            formatDate(dailyItem.timestamp)
        }

        val unit = SharedPreferencesManger.getSharedPreferencesManagerTempUnit(context)
        if (unit == "celsius") {

            holder.dayMaxDegree.text = kelvinToCelsius(dailyItem.temperature.max)
            holder.dayMinDegree.text = kelvinToCelsius(dailyItem.temperature.min)

        } else if (unit == "Fahrenheit") {
            holder.dayMaxDegree.text = kelvinToFahrenheit(dailyItem.temperature.max)
            holder.dayMinDegree.text = kelvinToFahrenheit(dailyItem.temperature.min)
        } else {
            holder.dayMaxDegree.text = dailyItem.temperature.max.toString()
            holder.dayMinDegree.text = dailyItem.temperature.min.toString()
        }
        holder.dayTextView.text = formattedDate
        holder.descriptionTextView.text = dailyItem.weather[0].description
        Glide.with(context)
            .load("https://openweathermap.org/img/wn/" + dailyItem.weather[0].icon + "@2x.png")
            .into(holder.weatherIcon)
    }

    private fun formatDate(timestamp: Long): String {
        val calendar = Calendar.getInstance().apply {
            timeInMillis = timestamp * 1000
        }
        val dateFormat = SimpleDateFormat("EEEE", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }




}
