package com.example.climecast.ui.alerts

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.climecast.databinding.FragmentAlertsBinding
import com.example.climecast.ui.alerts.notification.AlarmItem
import com.example.climecast.util.Constants.CHANNEL_ID
import com.example.climecast.util.Constants.NOTIFICATION_PREM
import com.example.climecast.ui.alerts.notification.NotificationReceiver
import com.example.climecast.ui.alerts.notification.NotificationsSchedulerImpl
import java.time.LocalDateTime
import java.util.Calendar

class AlertsFragment : Fragment() {


    private val NOTIFICATION_ID = 1001
    private var _binding: FragmentAlertsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAlertsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //createNotificationChannel()
        requestNotificationPermission()
        //checkNotificationEnabled()
        //scheduleNotification()


        val scheduler = NotificationsSchedulerImpl(requireActivity())
        val item = AlarmItem(
            time = LocalDateTime.now().plusSeconds(10),
            message = "Message from alert"
        )
        //item?.let { scheduler::schedule }
        scheduler.schedule(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "alarm_id"
            val channelName = "alarm_name"
            val notificationManager =
                activity?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun requestNotificationPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
            NOTIFICATION_PREM
        )
    }

    private fun checkNotificationEnabled() {
        val notificationManager =
            requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val areNotificationsEnabled = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = notificationManager.getNotificationChannel(CHANNEL_ID)
            channel?.importance != NotificationManager.IMPORTANCE_NONE
        } else {
            notificationManager.areNotificationsEnabled()
        }
        // Notify ViewModel that notifications are enabled or not
        //notificationsViewModel.setNotificationsEnabled(areNotificationsEnabled)
    }

    private fun scheduleNotification() {
        val alarmManager = activity?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(requireContext(), NotificationReceiver::class.java)
        /*intent.putExtra(NotificationReceiver.NOTIFICATION_ID, NOTIFICATION_ID)
        intent.putExtra(NotificationReceiver.NOTIFICATION_TITLE, "Title")
        intent.putExtra(NotificationReceiver.NOTIFICATION_MESSAGE, "Message")*/

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            1026,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE // or PendingIntent.FLAG_MUTABLE
        )

        // Schedule the notification to be fired after 10 seconds
        val triggerTime = Calendar.getInstance().timeInMillis + 10000 // 10 seconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                triggerTime,
                pendingIntent
            )
        } else {
            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                triggerTime,
                pendingIntent
            )
        }
    }
}
