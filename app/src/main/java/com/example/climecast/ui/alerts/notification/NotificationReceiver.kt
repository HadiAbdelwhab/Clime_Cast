package com.example.climecast.ui.alerts.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.climecast.R
import com.example.climecast.database.WeatherLocalDataSourceImpl
import com.example.climecast.network.ApiState
import com.example.climecast.network.WeatherRemoteDataSourceImpl
import com.example.climecast.repository.WeatherRepositoryImpl
import com.example.climecast.ui.alerts.viewmodel.AlertViewModelFactory
import com.example.climecast.ui.alerts.viewmodel.AlertsViewModel
import com.example.climecast.util.Constants.CHANNEL_ID
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

private const val TAG = "NotificationReceiver"

class NotificationReceiver : BroadcastReceiver() {


    override fun onReceive(context: Context?, intent: Intent?) {


        val message = intent?.getStringExtra("EXTRA_MESSAGE") ?: return



        Log.i(TAG, "onReceive: $message")



        context?.let { ctx ->
            // Ensure the NotificationChannel is created before showing the notification
            createNotificationChannel(ctx)

            val channelId = "alarm_id"
            val notificationManager =
                ctx.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            val builder = NotificationCompat.Builder(ctx, channelId)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Alarm Demo")
                .setContentText("Notification sent with message: $message")
                .setPriority(NotificationCompat.PRIORITY_HIGH)

            // Notify with a unique ID to ensure multiple notifications are shown separately
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
