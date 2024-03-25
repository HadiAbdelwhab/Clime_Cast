package com.example.climecast.util

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import java.time.LocalDateTime
import java.time.ZoneId

private const val TAG = "NotificationWorker"

class NotificationsSchedulerImpl(
    private val context: Context
) : NotificationsScheduler {

    private val alarmManger = context.getSystemService(AlarmManager::class.java)
    override fun schedule(item: LocalDateTime) {
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("EXTRA", "Weather is fine")
        }
        alarmManger.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            item.atZone(ZoneId.systemDefault()).toEpochSecond() * 1000,
            PendingIntent.getBroadcast(
                context,
                1026,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
            )

        )
    }

    override fun cancel(item: LocalDateTime) {
        alarmManger.cancel(
            PendingIntent.getBroadcast(
                context,
                1026,
                Intent(context, AlarmReceiver::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
            )
        )
    }

}