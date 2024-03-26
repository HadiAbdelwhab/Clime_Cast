package com.example.climecast.ui.alerts.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import java.time.LocalDateTime
import java.time.ZoneId

private const val TAG = "NotificationWorker"

class NotificationsSchedulerImpl(
    private val context: Context
) : NotificationsScheduler {

    private val alarmManger = context.getSystemService(AlarmManager::class.java)
    override fun schedule(item: AlarmItem) {
        val intent = Intent(context, NotificationReceiver::class.java).apply {
            putExtra("EXTRA_MESSAGE", item.message)
        }
        val alarmTime = item.time.atZone(ZoneId.systemDefault()).toEpochSecond() * 1000L
        alarmManger.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            alarmTime,
            PendingIntent.getBroadcast(
                context,
                item.hashCode(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
        Log.e("Alarm", "Alarm set at $alarmTime")
    }

    override fun cancel(item: AlarmItem) {
        alarmManger.cancel(
            PendingIntent.getBroadcast(
                context,
                1026,
                Intent(),//(context, NotificationReceiver::class.java
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }

}

data class AlarmItem(
    val time: LocalDateTime,
    val message: String
)