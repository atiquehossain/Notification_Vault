package com.nexgenscript.notilisson.data


import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface NotificationDao {


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
    GROUP BY id, time, package_name, only_time, uniqueMessageId, is_group_summary, title, app_name
    ORDER BY time DESC
""")
    fun getFilteredNotifications(): Flow<List<NotificationEntity>>



    @Query("""
    SELECT id, title, package_name, time, human_read_time, date, only_time, 
           app_name, icon, category, profileImageBase64, uniqueMessageId, content, 
           can_reply, is_replied, is_group_summary
    FROM notifications 
    WHERE app_name = :appName
        AND title = :title
        AND IFNULL(category, '') NOT IN ('call', 'missed_call')
        AND uniqueMessageId IS NOT NULL
    ORDER BY time DESC
""")
    fun getNotificationsByAppAndTitle(
        appName: String,
        title: String
    ): Flow<List<NotificationEntity>>



    @Query("SELECT COUNT(*) FROM notifications WHERE uniqueMessageId  = :uniqueMessageId ")
    suspend fun checkIfNotificationExists(uniqueMessageId: String): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(notification: NotificationEntity)


}

