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
    @ColumnInfo(name = "category") val category: String?,
    @ColumnInfo(name = "group_key") val groupKey: String?, // Group identifier
    @ColumnInfo(name = "is_group_summary") val isGroupSummary: Boolean = false, // Track if it's a group summary
    @ColumnInfo(name = "icon", typeAffinity = ColumnInfo.BLOB) val icon: ByteArray?, // Store as raw image data
    @ColumnInfo(name = "uniqueMessageId") val uniqueMessageId: String, // Unique hash for message
    @ColumnInfo(name = "profileImageBase64") val profileImageBase64: String?, // Base64 for sender's image
    @ColumnInfo(name = "can_reply") val canReply: Boolean = false, // Whether reply is possible
    @ColumnInfo(name = "conversation_id") val conversationId: String?, // Used to track threads
    @ColumnInfo(name = "is_replied") val isReplied: Boolean = false // Track if the user has replied
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as NotificationEntity

        if (id != other.id) return false
        if (title != other.title) return false
        if (content != other.content) return false
        if (packageName != other.packageName) return false
        if (time != other.time) return false
        if (humanReadTime != other.humanReadTime) return false
        if (date != other.date) return false
        if (onlyTime != other.onlyTime) return false
        if (appName != other.appName) return false
        if (category != other.category) return false
        if (groupKey != other.groupKey) return false
        if (isGroupSummary != other.isGroupSummary) return false
        if (icon != null) {
            if (other.icon == null) return false
            if (!icon.contentEquals(other.icon)) return false
        } else if (other.icon != null) return false
        if (uniqueMessageId != other.uniqueMessageId) return false
        if (profileImageBase64 != other.profileImageBase64) return false
        if (canReply != other.canReply) return false
        if (conversationId != other.conversationId) return false
        if (isReplied != other.isReplied) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + title.hashCode()
        result = 31 * result + content.hashCode()
        result = 31 * result + packageName.hashCode()
        result = 31 * result + time.hashCode()
        result = 31 * result + humanReadTime.hashCode()
        result = 31 * result + date.hashCode()
        result = 31 * result + onlyTime.hashCode()
        result = 31 * result + appName.hashCode()
        result = 31 * result + (category?.hashCode() ?: 0)
        result = 31 * result + (groupKey?.hashCode() ?: 0)
        result = 31 * result + isGroupSummary.hashCode()
        result = 31 * result + (icon?.contentHashCode() ?: 0)
        result = 31 * result + uniqueMessageId.hashCode()
        result = 31 * result + (profileImageBase64?.hashCode() ?: 0)
        result = 31 * result + canReply.hashCode()
        result = 31 * result + (conversationId?.hashCode() ?: 0)
        result = 31 * result + isReplied.hashCode()
        return result
    }
}