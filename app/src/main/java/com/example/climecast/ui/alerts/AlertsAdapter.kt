package com.example.climecast.ui.alerts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.climecast.R
import com.example.climecast.model.NotificationItem
import android.text.format.DateFormat
import java.text.SimpleDateFormat
import java.util.*
class AlertsAdapter(
    private val alerts: List<NotificationItem>
) : RecyclerView.Adapter<AlertsAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val timeTextView: TextView = itemView.findViewById(R.id.alert_time_text_view)
        val dateTextView: TextView = itemView.findViewById(R.id.alert_date_text_view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.alert_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return alerts.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val timeStamp = alerts[position].timestamp

        // Format time
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val formattedTime = timeFormat.format(Date(timeStamp))

        // Format date
        val dateFormat = SimpleDateFormat("dMMM", Locale.getDefault())
        val formattedDate = dateFormat.format(Date(timeStamp))

        // Set formatted time and date to TextViews
        holder.timeTextView.text = formattedTime
        holder.dateTextView.text = formattedDate
    }
}