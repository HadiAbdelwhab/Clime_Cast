package com.example.climecast.ui.alerts

import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TimePickerDialog
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
import com.example.climecast.ui.alerts.notification.NotificationIntentService
import com.example.climecast.util.Constants.CHANNEL_ID
import com.example.climecast.util.Constants.NOTIFICATION_PREM
import com.example.climecast.ui.alerts.notification.NotificationReceiver
import com.example.climecast.util.Constants.LATITUDE_KEY
import com.example.climecast.util.Constants.LONGITUDE_KEY
import com.example.climecast.util.Constants.TIME_STAMP_KEY
import java.util.Calendar
import java.util.TimeZone

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


        requestNotificationPermission()
        setListeners()


    }

    private fun setListeners() {
        binding.addAlarmButton.setOnClickListener {

            showDateTimePicker()


        }
    }

    private fun showDateTimePicker() {
        val calendar = Calendar.getInstance()
        val currentYear = calendar.get(Calendar.YEAR)
        val currentMonth = calendar.get(Calendar.MONTH)
        val currentDay = calendar.get(Calendar.DAY_OF_MONTH)
        val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
        val currentMinute = calendar.get(Calendar.MINUTE)

        // Show DatePickerDialog
        val datePickerDialog = DatePickerDialog(
            requireActivity(),
            { _, year, month, day ->
                // Show TimePickerDialog after selecting the date
                showTimePicker(year, month, day)
            },
            currentYear,
            currentMonth,
            currentDay
        )
        datePickerDialog.datePicker.minDate =
            System.currentTimeMillis() // Set minimum date to today
        datePickerDialog.show()
    }

    private fun showTimePicker(year: Int, month: Int, day: Int) {
        val calendar = Calendar.getInstance()
        val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
        val currentMinute = calendar.get(Calendar.MINUTE)

        // Show TimePickerDialog
        val timePickerDialog = TimePickerDialog(
            requireActivity(),
            { _, hourOfDay, minute ->
                // Handle the selected date and time
                handleDateTimeSelected(year, month, day, hourOfDay, minute)
            },
            currentHour,
            currentMinute,
            true
        )
        timePickerDialog.show()
    }

    private fun handleDateTimeSelected(
        year: Int,
        month: Int,
        day: Int,
        hourOfDay: Int,
        minute: Int
    ) {
        // Handle the selected date and time here
        val calendar = Calendar.getInstance()
        calendar.timeZone = TimeZone.getTimeZone("UTC") // Set time zone to UTC
        calendar.set(year, month, day, hourOfDay, minute)

        // Convert selected date and time to Unix timestamp
        val timeStamp = calendar.timeInMillis / 1000 // Convert milliseconds to seconds

        // Perform your action with the selected date and time
        goToNotificationIntentService(39.099724, -94.578331, timeStamp)
    }

    private fun goToNotificationIntentService(
        latitude: Double,
        longitude: Double,
        timestamp: Long
    ) {
        val intent = Intent(requireActivity(), NotificationIntentService::class.java)
        intent.putExtra(LATITUDE_KEY, latitude)
        intent.putExtra(LONGITUDE_KEY, longitude)
        intent.putExtra(TIME_STAMP_KEY, timestamp)
        NotificationIntentService.myEnqueueWork(requireActivity(), intent)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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
