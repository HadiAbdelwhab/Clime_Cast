package com.example.climecast.database.dao

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.PrimaryKey
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.climecast.database.WeatherDatabase
import com.example.climecast.model.NotificationItem
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class NotificationDaoTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: WeatherDatabase
    private lateinit var dao: NotificationDao

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            WeatherDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = database.notificationDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertNotification() = runBlocking {
        val notification = NotificationItem("desc", "icon", 15.0, 5616516.0.toLong())
        dao.insertNotification(notification)

        val result = dao.getAlerts().firstOrNull()
        assertEquals(1, result?.size)
        assertEquals(notification, result?.get(0))
    }

    @Test
    fun deleteAlertByTimestamp() = runBlocking {
        val notification = NotificationItem("desc", "icon", 15.0, 5616516.0.toLong())
        dao.insertNotification(notification)

        dao.deleteAlertByTimestamp(notification.timestamp)

        val result = dao.getAlerts().firstOrNull()
        assertEquals(0, result?.size)
    }

    @Test
    fun getAlerts() = runBlocking {
        val notification1 = NotificationItem("desc", "icon", 15.0, 5616516.0.toLong())
        val notification2 = NotificationItem("desc2", "icon2", 15.0, 5616517.0.toLong())
        dao.insertNotification(notification1)
        dao.insertNotification(notification2)

        val result = dao.getAlerts().firstOrNull()
        assertEquals(2, result?.size)
        assertEquals(notification1, result?.get(0))
        assertEquals(notification2, result?.get(1))
    }
}
