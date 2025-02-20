package com.nexgenscript.notilisson.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo

@Entity(tableName = "notifications")
data class NotificationEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,

    // Notification Content
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "content") val content: String,

    // App Information
    @ColumnInfo(name = "package_name") val packageName: String,
    @ColumnInfo(name = "app_name") val appName: String,
    @ColumnInfo(name = "category") val category: String?,

    // Timestamps
    @ColumnInfo(name = "time") val time: Long,
    @ColumnInfo(name = "human_read_time") val humanReadTime: String,
    @ColumnInfo(name = "date") val date: String,
    @ColumnInfo(name = "only_time") val onlyTime: String,

    // Unique Identifiers
    @ColumnInfo(name = "uniqueMessageId") val uniqueMessageId: String,
    @ColumnInfo(name = "message_id") val messageId: String? ,

    // Reply Status
    @ColumnInfo(name = "can_reply") val canReply: Boolean = false,
    @ColumnInfo(name = "is_replied") val isReplied: Boolean = false,

    // Media & Icons
    @ColumnInfo(name = "icon", typeAffinity = ColumnInfo.BLOB) val icon: ByteArray?,
    @ColumnInfo(name = "profileImageBase64") val profileImageBase64: String?,


)
