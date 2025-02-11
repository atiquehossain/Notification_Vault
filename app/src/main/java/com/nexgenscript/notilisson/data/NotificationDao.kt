package com.nexgenscript.notilisson.data


import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface NotificationDao {
    @Query("SELECT * FROM notifications ORDER BY time DESC")
    fun getAllNotifications(): Flow<List<NotificationEntity>>  // ✅ Return Flow instead of List

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(notification: NotificationEntity)

    @Delete
    suspend fun deleteNotification(notification: NotificationEntity)

    @Query("DELETE FROM notifications")
    suspend fun clearAll()

    @Query("""
    SELECT * FROM notifications t1
    WHERE id = (
        SELECT MIN(id) FROM notifications t2
        WHERE t1.only_time = t2.only_time
        AND t1.package_name = t2.package_name
    )
    ORDER BY time DESC
""")
    fun getFilteredNotifications(): Flow<List<NotificationEntity>>

}

