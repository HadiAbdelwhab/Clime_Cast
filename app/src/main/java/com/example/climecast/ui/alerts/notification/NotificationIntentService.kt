package com.example.climecast.ui.alerts.notification

import android.app.IntentService
import android.content.Intent
import android.content.Context
import android.util.Log
import androidx.core.app.JobIntentService
import androidx.core.app.JobIntentService.enqueueWork
import com.example.climecast.database.WeatherLocalDataSourceImpl
import com.example.climecast.network.WeatherRemoteDataSourceImpl
import com.example.climecast.repository.WeatherRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.time.LocalDateTime

private const val TAG = "NotificationIntentService"

class NotificationIntentService : JobIntentService() {

    private lateinit var repository: WeatherRepositoryImpl

    companion object {
        private const val JOB_ID = 1

        fun myEnqueueWork(context: Context, work: Intent) {
            Log.i(TAG, "myEnqueueWork: ")
            enqueueWork(context, NotificationIntentService::class.java, JOB_ID, work)
        }
    }

    override fun onHandleWork(intent: Intent) {
        Log.i(TAG, "onHandleIntent: ")

        repository = WeatherRepositoryImpl.getInstance(
            WeatherRemoteDataSourceImpl.getInstance(),
            WeatherLocalDataSourceImpl.getInstance(this)
        )

        GlobalScope.launch(Dispatchers.IO) {
            val response = repository.getWeatherForecastByTime(
                39.099724,
                -94.578331,
                1643803200
            )
            if (response.isSuccessful) {
                Log.i(TAG, "onHandleWork: ${response.body()}")
            }
        }
    }
}
