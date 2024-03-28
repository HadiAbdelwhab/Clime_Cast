package com.example.climecast.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.climecast.model.NotificationItem
import kotlinx.coroutines.flow.Flow

@Dao
interface NotificationDao {
    @Insert
    suspend fun insertNotification(notificationItem: NotificationItem)

    @Query("DELETE FROM alerts_table WHERE timestamp = :primaryKey")
    suspend fun deleteAlertByTimestamp(primaryKey: Long)

    @Query("select * from alerts_table")
    fun getAlerts(): Flow<List<NotificationItem>>
}