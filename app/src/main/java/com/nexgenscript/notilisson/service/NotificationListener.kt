package com.nexgenscript.notilisson.service

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
        var content = extras.getString("android.text")
        val timestamp = sbn.postTime
        val category = sbn.notification.category ?: "Unknown"
        val appName = getAppNameFromPackage(packageName)
        val icon = sbn.notification.smallIcon?.resId?.toString() ?: "No Icon"

        // Convert timestamp to date & time
        val (date, onlyTime) = convertTimestamp(timestamp)

        // Try extracting image from multiple sources
        val imageBase64 = extractImageAsBase64(extras)

        // If the content is null or indicates a photo, update it accordingly
        if (content == null || content.lowercase(Locale.getDefault()) == "photo") {
            content = "📷 Photo"
        }

        Log.d("NotificationListener", "Message from: $appName ($packageName)")
        Log.d("NotificationListener", "Title: $title")
        Log.d("NotificationListener", "Content: $content")
        Log.d("NotificationListener", "Category: $category")
        Log.d("NotificationListener", "Icon ID: $icon")
        Log.d("NotificationListener", "Date: $date, Time: $onlyTime")
        Log.d("NotificationListener", "Image Extracted: ${imageBase64 != null}")

        CoroutineScope(Dispatchers.IO).launch {
            val notification = NotificationEntity(
                title = title,
                content = content,
                packageName = packageName,
                time = timestamp,
                humanReadTime = getHumanReadableTime(timestamp),
                date = date,
                onlyTime = onlyTime,
                appName = appName,
                icon = icon,
                category = category,
                profileImageBase64 = imageBase64
            )
            db.notificationDao().insertNotification(notification)
        }
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
                ?: (extras.getParcelable("android.largeIcon", android.graphics.drawable.Icon::class.java)?.let { getBitmapFromIcon(it) })
        } else {
            @Suppress("DEPRECATION")
            extras.getParcelable("android.picture")
                ?: extras.getParcelable("android.bigPicture")
                ?: (extras.getParcelable<android.graphics.drawable.Icon>("android.largeIcon")?.let { getBitmapFromIcon(it) })
        }

        return bitmap?.let { encodeImageToBase64(it) }
    }

    private fun getBitmapFromIcon(icon: android.graphics.drawable.Icon): Bitmap? {
        return icon.loadDrawable(applicationContext)?.let { drawable ->
            if (drawable is BitmapDrawable) drawable.bitmap else null
        }
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
