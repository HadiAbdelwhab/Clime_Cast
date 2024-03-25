package com.example.climecast.ui.alerts

import android.app.Activity
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.example.climecast.databinding.FragmentAlertsBinding
import com.example.climecast.util.Constants.CHANNEL_ID
import com.example.climecast.util.Constants.NOTIFICATION_PREM
import com.example.climecast.util.NotificationsScheduler
import com.example.climecast.util.NotificationsSchedulerImpl
import java.time.LocalDateTime


class AlertsFragment : Fragment() {

    private var _binding: FragmentAlertsBinding? = null
    private val binding get() = _binding!!

    //private lateinit var scheduler: NotificationsSchedulerImpl

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAlertsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
            NOTIFICATION_PREM
        )
        checkNotificationEnabled()



    }


   /* private fun createNewAlert(d:Long) {
        val time = LocalDateTime.now().plusSeconds(d) // Schedule notification after 10 seconds

        scheduler = NotificationsSchedulerImpl(requireActivity())
        scheduler.schedule(time)
    }*/

    private fun checkNotificationEnabled() {
        val notificationManager =
            requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Check if general notification permission is granted
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = notificationManager.getNotificationChannel(CHANNEL_ID)
            val areNotificationsEnabled = channel?.importance != NotificationManager.IMPORTANCE_NONE
           // createNewAlert()
            // Notify ViewModel that notifications are enabled or not
            //notificationsViewModel.setNotificationsEnabled(areNotificationsEnabled)
        } else {
            val areNotificationsEnabled =
                notificationManager.areNotificationsEnabled()
            // Notify ViewModel that notifications are enabled or not
            //notificationsViewModel.setNotificationsEnabled(areNotificationsEnabled)
        }
    }
}