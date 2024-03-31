package com.example.climecast.ui.alerts

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.climecast.database.WeatherLocalDataSourceImpl
import com.example.climecast.databinding.FragmentAlertsBinding
import com.example.climecast.model.NotificationItem
import com.example.climecast.network.WeatherRemoteDataSourceImpl
import com.example.climecast.repository.WeatherRepositoryImpl
import com.example.climecast.ui.alerts.notification.NotificationIntentService
import com.example.climecast.ui.alerts.notification.NotificationsSchedulerImpl
import com.example.climecast.ui.alerts.viewmodel.AlertViewModelFactory
import com.example.climecast.ui.alerts.viewmodel.AlertsViewModel
import com.example.climecast.util.Constants.NOTIFICATION_PREM
import com.example.climecast.util.Constants.LATITUDE_KEY
import com.example.climecast.util.Constants.LONGITUDE_KEY
import com.example.climecast.util.Constants.TIME_STAMP_KEY
import com.example.climecast.util.SharedPreferencesManger
import kotlinx.coroutines.launch
import java.util.Calendar

private const val TAG = "AlertsFragment"

class AlertsFragment : Fragment(), AlertClickListener, AlertDialogFragment.AlertDialogListener {


    private var _binding: FragmentAlertsBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: AlertsViewModel
    private lateinit var factory: AlertViewModelFactory

    private lateinit var adapter: AlertsAdapter

    private var notificationItemToDelete: NotificationItem? = null

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

        setUpViewModel()

        setListeners()

        setObserver()

    }

    private fun setObserver() {
        lifecycleScope.launch {
            viewModel.alertsStateFlow.collect { list ->
                setUpAdapter(list)
                Log.i(TAG, "setObserver: ${list.size}")
            }
        }
    }

    private fun setUpViewModel() {
        factory = AlertViewModelFactory(
            WeatherRepositoryImpl.getInstance(
                WeatherRemoteDataSourceImpl.getInstance(),
                WeatherLocalDataSourceImpl.getInstance(requireActivity())
            )
        )

        viewModel =
            ViewModelProvider(this, factory)[AlertsViewModel::class.java]
    }

    private fun setUpAdapter(notificationItemList: List<NotificationItem>) {
        Log.i(TAG, "setUpAdapter: ${notificationItemList.size}")
        adapter = AlertsAdapter(notificationItemList, this)
        binding.alertRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireActivity()).apply {
                orientation = RecyclerView.VERTICAL
            }
            adapter = this@AlertsFragment.adapter
        }
        adapter.notifyDataSetChanged()
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
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH, day)
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
        calendar.set(Calendar.MINUTE, minute)
        calendar.set(Calendar.SECOND, 0) // Reset seconds to zero if necessary

        // Subtracting 2 hours
        // calendar.add(Calendar.HOUR_OF_DAY, -2)

        val timeStamp = calendar.timeInMillis // Convert calendar time to milliseconds
        Log.i(TAG, "handleDateTimeSelected: $timeStamp")
        goToNotificationIntentService(timeStamp)
    }


    private fun goToNotificationIntentService(timestamp: Long) {
        val intent = Intent(requireActivity(), NotificationIntentService::class.java)
        val longitude =
            SharedPreferencesManger.getSharedPreferencesManagerCurrentLongitude(
                requireActivity()
            )
        val latitude =
            SharedPreferencesManger.getSharedPreferencesManagerCurrentLatitude(
                requireActivity()
            )
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


    override fun cancelAlert(notificationItem: NotificationItem) {
        notificationItemToDelete = notificationItem
        val alertDialogFragment = AlertDialogFragment()
        alertDialogFragment.setAlertDialogListener(this)
        alertDialogFragment.show(parentFragmentManager, "AlertDialogFragment")
    }

    override fun onPositiveButtonClick() {
        val scheduler = NotificationsSchedulerImpl(requireActivity())
        notificationItemToDelete?.let { scheduler.cancel(it) }
        notificationItemToDelete?.let { viewModel.deleteAlert(it) }
        adapter.notifyDataSetChanged()
    }
}
