package com.example.climecast.ui.alerts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.climecast.R
import com.example.climecast.model.AlertDTO

class AlertsAdapter(
    private val alerts: List<AlertDTO>
) : RecyclerView.Adapter<AlertsAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val startTimeTextView: TextView = itemView.findViewById(R.id.start_time_text_view)
        val endTimeTextView: TextView = itemView.findViewById(R.id.end_time_text_view)
        val startDateTextView: TextView = itemView.findViewById(R.id.start_date_text_view)
        val endDateTextView: TextView = itemView.findViewById(R.id.end_date_text_view)
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
        holder.startTimeTextView.text = alerts[position].startTime.time.toString()
        holder.endTimeTextView.text = alerts[position].endTime.time.toString()
        holder.startDateTextView.text = alerts[position].startDate.time.toString()
        holder.endDateTextView.text = alerts[position].endDate.time.toString()
    }
}