package com.example.climecast.ui.alerts.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.climecast.R
import com.example.climecast.util.Constants.CHANNEL_ID
import com.example.climecast.util.Constants.DESCRIPTION_KEY
import com.example.climecast.util.Constants.ICON_KEY
import com.example.climecast.util.Constants.TEMP_KEY
import com.example.climecast.util.WeatherUtils


private const val TAG = "NotificationReceiver"

class NotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val description = intent?.getStringExtra(DESCRIPTION_KEY)
        val icon = intent?.getStringExtra(ICON_KEY)
        val temperature = WeatherUtils.kelvinToCelsius(intent?.getDoubleExtra(TEMP_KEY, 0.0)!!)


        context?.let { ctx ->
            createNotificationChannel(ctx) // Ensure the notification channel is created

            val notificationManager =
                ctx.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            val builder = NotificationCompat.Builder(ctx, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Current Weather")
                .setContentText("Weather: $description \n $temperature")
                .setPriority(NotificationCompat.PRIORITY_HIGH)

            notificationManager.notify(System.currentTimeMillis().toInt(), builder.build())
        }
    }

    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = context.getString(R.string.channel_name)
            val description = context.getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance)
            channel.description = description

            val notificationManager =
                context.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }
}
/*Glide.with()
.load("https://openweathermap.org/img/wn/${data.current.weather[0].icon}@2x.png")
.into(binding.currentWeatherImageView)*/