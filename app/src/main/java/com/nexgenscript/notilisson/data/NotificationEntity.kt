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
    @ColumnInfo(name = "time") val time: Long, // Unix timestamp
    @ColumnInfo(name = "human_read_time") val humanReadTime: String, // Readable time
    @ColumnInfo(name = "date") val date: String, // YYYY-MM-DD format
    @ColumnInfo(name = "only_time") val onlyTime: String, // HH:mm:ss format
    @ColumnInfo(name = "app_name") val appName: String,
    @ColumnInfo(name = "icon") val icon: String?, // Store as Base64 or file path
    @ColumnInfo(name = "category") val category: String? // Nullable, ensure safe handling
)

