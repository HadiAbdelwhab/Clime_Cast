package com.example.climecast.ui.alerts.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.climecast.model.NotificationItem
import com.example.climecast.util.Constants.DESCRIPTION_KEY
import com.example.climecast.util.Constants.ICON_KEY
import com.example.climecast.util.Constants.TEMP_KEY
import com.example.climecast.util.Constants.TIME_STAMP_KEY


private const val TAG = "NotificationsSchedulerImpl"

class NotificationsSchedulerImpl(
    private val context: Context
) : NotificationsScheduler {

    private val alarmManager = context.getSystemService(AlarmManager::class.java)
    override fun schedule(item: NotificationItem) {
        val intent = Intent(context, NotificationReceiver::class.java).apply {
            putExtra(DESCRIPTION_KEY, item.description)
            putExtra(ICON_KEY, item.icon)
            putExtra(TEMP_KEY, item.temperature)
            putExtra(TIME_STAMP_KEY, item.timestamp)

            Log.i(TAG, "schedule: $item")
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            item.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )



        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            item.timestamp,
            pendingIntent
        )

    }


    override fun cancel(item: NotificationItem) {
        alarmManager.cancel(
            PendingIntent.getBroadcast(
                context,
                item.hashCode(),
                Intent(),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }

}

