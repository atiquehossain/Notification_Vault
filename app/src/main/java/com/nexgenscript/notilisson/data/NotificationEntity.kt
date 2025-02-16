package com.nexgenscript.notilisson.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo

@Entity(tableName = "notifications")
data class NotificationEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "content") val content: String,
    @ColumnInfo(name = "package_name") val packageName: String,
    @ColumnInfo(name = "time") val time: Long,
    @ColumnInfo(name = "human_read_time") val humanReadTime: String,
    @ColumnInfo(name = "date") val date: String,
    @ColumnInfo(name = "only_time") val onlyTime: String,
    @ColumnInfo(name = "app_name") val appName: String,
    @ColumnInfo(name = "category") val category: String?,
    @ColumnInfo(name = "icon", typeAffinity = ColumnInfo.BLOB) val icon: ByteArray?,
    @ColumnInfo(name = "uniqueMessageId") val uniqueMessageId: String,
    @ColumnInfo(name = "profileImageBase64") val profileImageBase64: String?,
    @ColumnInfo(name = "can_reply") val canReply: Boolean = false,
    @ColumnInfo(name = "conversation_id") val conversationId: String?,
    @ColumnInfo(name = "is_replied") val isReplied: Boolean = false
)