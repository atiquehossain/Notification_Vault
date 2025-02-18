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
           app_name, icon, category, profileImageBase64, uniqueMessageId, content, 
           can_reply, is_replied
    FROM notifications 
    WHERE app_name NOT IN ('Android System') 
        AND IFNULL(category, '') NOT IN ('call', 'missed_call')
        AND uniqueMessageId IS NOT NULL
        AND LOWER(content) NOT LIKE '%new messages%'
    GROUP BY human_read_time, title, content
    HAVING id = MAX(id)
    ORDER BY time DESC
""")
    fun getFilteredNotifications(): Flow<List<NotificationEntity>>

    @Query("""
    SELECT id, title, package_name, time, human_read_time, date, only_time, 
           app_name, icon, category, profileImageBase64, uniqueMessageId, content, 
           can_reply, is_replied
    FROM notifications 
    WHERE app_name = :appName
        AND title = :title
        AND app_name NOT IN ('Android System') 
        AND IFNULL(category, '') NOT IN ('call', 'missed_call')
        AND uniqueMessageId IS NOT NULL
        AND LOWER(content) NOT LIKE '%new messages%'
    GROUP BY human_read_time, title, content
    HAVING id = MAX(id)
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

        @Query("SELECT * FROM notifications WHERE app_name = :appName AND title LIKE :groupName || '%' ORDER BY time DESC")
        fun getNotificationsByAppAndGroup(appName: String, groupName: String): Flow<List<NotificationEntity>>



}

