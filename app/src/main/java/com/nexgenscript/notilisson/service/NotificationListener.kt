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
        sbn.let {
            val packageName = sbn.packageName
            val extras = sbn.notification.extras
            val title = extras.getString("android.title") ?: "No Title"
            val content = extras.getString("android.text") ?: "No Content"
            val timestamp = sbn.postTime
            val category = sbn.notification.category ?: "Unknown"
            val appName = getAppNameFromPackage(packageName)
            val replyAvailable = getReplyAction(it.notification) != null

            //  val conversationId = extras.getString("android.conversation")
            val messageId = extras.getString("android.messageId") ?: ""
            val uniqueMessageId = hashMessage("$packageName$title$content$messageId")
            // Log notification details for debugging
            Log.d(
                "NotificationListener",
                "Package: $packageName, Title: $title, Content: $content, Timestamp: $timestamp,  UniqueMessageId: $uniqueMessageId"
            )

            CoroutineScope(Dispatchers.IO).launch {
              //  val exists = db.notificationDao().checkIfNotificationExists(uniqueMessageId, messageId) > 0

               // if (!exists) {
                    // Insert the notification
                    val notification = NotificationEntity(
                        title = title,
                        content = content,
                        packageName = packageName,
                        time = timestamp,
                        humanReadTime = getHumanReadableTime(timestamp),
                        date = convertTimestamp(timestamp).first,
                        onlyTime = convertTimestamp(timestamp).second,
                        appName = appName,
                        category = category,
                        icon = getBitmapFromIcon(sbn.notification.smallIcon)?.let {
                            encodeImageToByteArray(it)
                        },
                        uniqueMessageId = uniqueMessageId,
                        profileImageBase64 = extractImageAsBase64(extras),
                        canReply = replyAvailable,
                        isReplied = false,
                        messageId = messageId // Pass the messageId
                    )
                    db.notificationDao().insertNotification(notification)
               // }
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

    private fun getReplyAction(notification: Notification): Notification.Action? {
        return notification.actions?.find { it.remoteInputs != null }
    }

    private fun hashMessage(message: String): String {
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(message.toByteArray())
        return digest.joinToString("") { "%02x".format(it) }
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
