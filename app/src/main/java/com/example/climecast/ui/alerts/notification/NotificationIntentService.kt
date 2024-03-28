package com.example.climecast.ui.alerts.notification

import android.content.Intent
import android.content.Context
import android.util.Log
import androidx.core.app.JobIntentService
import com.example.climecast.database.WeatherLocalDataSourceImpl
import com.example.climecast.model.NotificationItem
import com.example.climecast.network.WeatherRemoteDataSourceImpl
import com.example.climecast.repository.WeatherRepositoryImpl
import com.example.climecast.util.Constants.LATITUDE_KEY
import com.example.climecast.util.Constants.LONGITUDE_KEY
import com.example.climecast.util.Constants.TIME_STAMP_KEY
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
            val longitude = intent.getStringExtra(LONGITUDE_KEY).toString().toDouble()
            val latitude = intent.getStringExtra(LATITUDE_KEY).toString().toDouble()
            val timeStamp = intent.getLongExtra(TIME_STAMP_KEY, 0)
            Log.i(TAG, "onHandleWork long: $longitude  lat $latitude + time $timeStamp")
            val response = repository.getWeatherForecastByTime(
                latitude,
                longitude,
                timeStamp
            )
            if (response.isSuccessful) {
                Log.i(TAG, "onHandleWork: isSuccessful ${response.body()}")
                val scheduler = NotificationsSchedulerImpl(this@NotificationIntentService)
                val item = NotificationItem(
                    description = response.body()!!.data[0].weather[0].description,
                    temperature = response.body()!!.data[0].temp,
                    icon = response.body()!!.data[0].weather[0].icon,
                    timestamp = timeStamp
                )
                Log.i(TAG, "onHandleWork: $item")

                repository.insetNotification(item)
                scheduler.schedule(item)


            }else{

                Log.i(TAG, "onHandleWork: failed call ${response.code()}")
            }
        }
    }
}
