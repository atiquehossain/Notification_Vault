package com.nexgenscript.notilisson.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "replies")
data class ReplyEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "notification_id") val notificationId: Int, // Links to the notification
    @ColumnInfo(name = "reply_message") val replyMessage: String, // User's reply
    @ColumnInfo(name = "timestamp") val timestamp: Long, // Time of reply
    @ColumnInfo(name = "status") val status: String = "pending" // Status: sent, failed, pending
)
