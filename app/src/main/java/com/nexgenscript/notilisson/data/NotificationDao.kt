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



    @Delete
    suspend fun deleteNotification(notification: NotificationEntity)

    @Query("DELETE FROM notifications")
    suspend fun clearAll()

    @Query("""
    SELECT id, title, package_name, time, human_read_time, date, only_time, 
           app_name, icon, category, profileImageBase64, uniqueMessageId, 
           GROUP_CONCAT(content, ' | ') AS content, can_reply, is_replied, is_group_summary
    FROM notifications 
    WHERE app_name NOT IN ('Android System') 
        AND content NOT IN ('No Content')
        AND category NOT IN ('call', 'missed_call')
    GROUP BY package_name, only_time, uniqueMessageId, is_group_summary
    ORDER BY time DESC
""")
    fun getFilteredNotifications(): Flow<List<NotificationEntity>>

    @Query("""
    SELECT * FROM notifications 
    WHERE app_name = :appName
    AND title = :title     
    AND IFNULL(category, '') NOT IN ('call', 'missed_call')
    GROUP BY uniqueMessageId 
    ORDER BY time DESC
""")
    fun getNotificationsByAppAndTitle(appName: String, title: String): Flow<List<NotificationEntity>>

    @Query("SELECT DISTINCT app_name FROM notifications")
    fun getAllApps(): Flow<List<String>>

    @Query("SELECT COUNT(*) FROM notifications WHERE uniqueMessageId  = :uniqueMessageId ")
    suspend fun checkIfNotificationExists(uniqueMessageId : String): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(notification: NotificationEntity)



}

