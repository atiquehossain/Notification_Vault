package com.nexgenscript.notilisson.service

import android.app.Notification
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Base64
import android.util.Log
import androidx.annotation.RequiresApi
import com.nexgenscript.notilisson.data.NotificationDatabase
import com.nexgenscript.notilisson.data.NotificationEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class NotificationListener : NotificationListenerService() {

    private val db by lazy { NotificationDatabase.getDatabase(applicationContext) }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onNotificationPosted(sbn: StatusBarNotification) {
        val packageName = sbn.packageName
        val extras = sbn.notification.extras
        val title = extras.getString("android.title") ?: "No Title"
        val content = extras.getString("android.text") ?: "No Content"
        val timestamp = sbn.postTime
        val category = sbn.notification.category ?: "Unknown"
        val appName = getAppNameFromPackage(packageName)

        // Check for group key & summary
        val groupKey = sbn.notification.group
        val isGroupSummary = sbn.notification.flags and Notification.FLAG_GROUP_SUMMARY != 0

        // Convert timestamp to date & time
        val (date, onlyTime) = convertTimestamp(timestamp)

        // Extract profile image
        val imageBase64 = extractImageAsBase64(extras)

        // Extract conversation ID if available
        val conversationId = extras.getString("android.conversation")

        // Check if reply is possible
        val canReply = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            extras.getParcelableArrayList("android.remoteInputHistory", CharSequence::class.java)?.isNotEmpty() ?: false
        } else {
            @Suppress("DEPRECATION")
            extras.getParcelableArray("android.remoteInputHistory")?.isNotEmpty() ?: false
        }

        // Convert Small Icon to ByteArray
        val iconBitmap = getBitmapFromIcon(sbn.notification.smallIcon)
        val iconByteArray = iconBitmap?.let { encodeImageToByteArray(it) }

        // Generate unique message ID (Include groupKey for uniqueness)
      //  val uniqueMessageId = generateSHA256("$packageName|$title|$content|$groupKey")
        val uniqueMessageId = generateSHA256("$packageName|$title|$content|$groupKey|$timestamp|$conversationId|$category")

        // Logging notifications
        when {
            groupKey != null && isGroupSummary -> {
                Log.d("NotificationListener", "📌 Group Summary Notification: $title from $appName ($packageName)")
            }
            groupKey != null -> {
                Log.d("NotificationListener", "🔗 Grouped Notification: $title from $appName ($packageName) [Group: $groupKey]")
            }
            else -> {
                Log.d("NotificationListener", "📨 Normal Notification: $title from $appName ($packageName)")
            }
        }

        CoroutineScope(Dispatchers.IO).launch {
            val exists = db.notificationDao().checkIfNotificationExists(uniqueMessageId) > 0

            if (!exists) {
                val notification = NotificationEntity(
                    title = title,
                    content = content,
                    packageName = packageName,
                    time = timestamp,
                    humanReadTime = getHumanReadableTime(timestamp),
                    date = date,
                    onlyTime = onlyTime,
                    appName = appName,
                    category = category,
                    icon = iconByteArray,
                    uniqueMessageId = uniqueMessageId,
                    profileImageBase64 = imageBase64,
                    canReply = canReply,
                    conversationId = conversationId,
                    isReplied = false, // Default as false
                    groupKey = groupKey, // Added groupKey for tracking grouped notifications
                    isGroupSummary = isGroupSummary // Added flag for group summary notifications
                )
                db.notificationDao().insertNotification(notification)
            } else {
                Log.d("NotificationListener", "Duplicate notification skipped: $content")
            }
        }
    }

    private fun getBitmapFromIcon(icon: android.graphics.drawable.Icon?): Bitmap? {
        return icon?.loadDrawable(applicationContext)?.let { drawable ->
            if (drawable is BitmapDrawable) drawable.bitmap else null
        }
    }

    private fun encodeImageToByteArray(bitmap: Bitmap): ByteArray {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
        return baos.toByteArray()
    }

    private fun generateSHA256(input: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(input.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }

    private fun convertTimestamp(timestamp: Long): Pair<String, String> {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        val date = dateFormat.format(Date(timestamp))
        val onlyTime = timeFormat.format(Date(timestamp))
        return Pair(date, onlyTime)
    }

    private fun getHumanReadableTime(timestamp: Long): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }

    private fun extractImageAsBase64(extras: android.os.Bundle): String? {
        val bitmap: Bitmap? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            extras.getParcelable("android.picture", Bitmap::class.java)
                ?: extras.getParcelable("android.bigPicture", Bitmap::class.java)
                ?: (extras.getParcelable(
                    "android.largeIcon",
                    android.graphics.drawable.Icon::class.java
                )?.let { getBitmapFromIcon(it) })
        } else {
            @Suppress("DEPRECATION")
            extras.getParcelable("android.picture")
                ?: extras.getParcelable("android.bigPicture")
                ?: (extras.getParcelable<android.graphics.drawable.Icon>("android.largeIcon")
                    ?.let { getBitmapFromIcon(it) })
        }

        return bitmap?.let { encodeImageToBase64(it) }
    }

    private fun encodeImageToBase64(bitmap: Bitmap): String {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
        val byteArray = baos.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    private fun getAppNameFromPackage(packageName: String): String {
        return try {
            val pm = packageManager
            val appInfo = pm.getApplicationInfo(packageName, 0)
            pm.getApplicationLabel(appInfo).toString()
        } catch (e: PackageManager.NameNotFoundException) {
            packageName
        }
    }
}
