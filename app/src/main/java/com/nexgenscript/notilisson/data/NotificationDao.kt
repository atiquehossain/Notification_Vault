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
    fun getAllNotifications(): Flow<List<NotificationEntity>>

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
    AND app_name NOT IN ('Android System', 'Google')
    AND content NOT IN ('No Content')
    ORDER BY time DESC
""")

    fun getFilteredNotifications(): Flow<List<NotificationEntity>>

    @Query("""
        SELECT * FROM notifications 
        WHERE app_name = :appName AND title = :title
        ORDER BY time DESC
    """)
    fun getNotificationsByAppAndTitle(appName: String, title: String): Flow<List<NotificationEntity>>

    @Query("SELECT DISTINCT app_name FROM notifications")
    fun getAllApps(): Flow<List<String>>

    @Query("SELECT DISTINCT title FROM notifications WHERE app_name = :appName")
    fun getTitlesByApp(appName: String): Flow<List<String>>


}

