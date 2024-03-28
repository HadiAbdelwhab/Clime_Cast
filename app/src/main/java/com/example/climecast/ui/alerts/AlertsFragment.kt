package com.example.climecast.ui.alerts

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.climecast.databinding.FragmentAlertsBinding
import com.example.climecast.ui.alerts.notification.NotificationIntentService
import com.example.climecast.util.Constants.NOTIFICATION_PREM
import com.example.climecast.util.Constants.LATITUDE_KEY
import com.example.climecast.util.Constants.LONGITUDE_KEY
import com.example.climecast.util.Constants.TIME_STAMP_KEY
import com.example.climecast.util.SharedPreferencesManger
import java.util.Calendar
import java.util.TimeZone

class AlertsFragment : Fragment() {


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
            System.currentTimeMillis()
        datePickerDialog.show()
    }

    private fun showTimePicker(year: Int, month: Int, day: Int) {
        val calendar = Calendar.getInstance()
        val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
        val currentMinute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            requireActivity(),
            { _, hourOfDay, minute ->
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
        val calendar = Calendar.getInstance()
        calendar.timeZone = TimeZone.getTimeZone("UTC") // Set time zone to UTC
        calendar.set(year, month, day, hourOfDay, minute)

        val timeStamp = calendar.timeInMillis / 1000 // Convert milliseconds to seconds

        goToNotificationIntentService(timeStamp)
    }

    private fun goToNotificationIntentService(timestamp: Long) {
        val intent = Intent(requireActivity(), NotificationIntentService::class.java)
        val longitude =
            SharedPreferencesManger.getSharedPreferencesManagerCurrentLongitude(requireActivity())
        val latitude =
            SharedPreferencesManger.getSharedPreferencesManagerCurrentLatitude(requireActivity())
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


}
