package com.example.climecast.ui.home.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.climecast.R
import com.example.climecast.model.HourlyData
import com.example.climecast.util.SharedPreferencesManger
import com.example.climecast.util.WeatherUtils.Companion.kelvinToCelsius
import com.example.climecast.util.WeatherUtils.Companion.kelvinToFahrenheit
import java.text.SimpleDateFormat
import java.util.*

class HoursWeatherDataAdapter(
    private val hourlyWeatherData: List<HourlyData>,
    private val context: Context
) : RecyclerView.Adapter<HoursWeatherDataAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val degreeTextView: TextView = itemView.findViewById(R.id.hours_degree)
        internal val timeTextView: TextView = itemView.findViewById(R.id.time_text_view)
        internal val weatherIcon: ImageView = itemView.findViewById(R.id.hours_weather_icon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.hours_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return hourlyWeatherData.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val hourlyData = hourlyWeatherData[position]

        val unit = SharedPreferencesManger.getSharedPreferencesManagerTempUnit(context)
        if (unit == "celsius") {

            holder.degreeTextView.text = kelvinToCelsius(hourlyData.temperature)

        } else if (unit == "Fahrenheit") {
            holder.degreeTextView.text = kelvinToFahrenheit(hourlyData.temperature)
        } else {
            holder.degreeTextView.text = hourlyData.temperature.toString()
        }

        holder.timeTextView.text = formatTime(hourlyData.timestamp)
        Glide.with(context)
            .load("https://openweathermap.org/img/wn/" + hourlyData.weather[0].icon + "@2x.png")
            .into(holder.weatherIcon)
    }

    private fun formatTime(timestamp: Long): String {
        val calendar = Calendar.getInstance().apply {
            timeInMillis = timestamp * 1000
        }
        val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }


}
