package com.nexgenscript.notilisson.service

import android.content.pm.PackageManager
import android.os.Build
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import androidx.annotation.RequiresApi
import com.nexgenscript.notilisson.data.NotificationDatabase
import com.nexgenscript.notilisson.data.NotificationEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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
        val icon = sbn.notification.smallIcon?.resId?.toString() ?: "No Icon"

        // Convert timestamp to date & time
        val (date, onlyTime) = convertTimestamp(timestamp)

        Log.d("NotificationListener", "📩 Message from: $appName ($packageName)")
        Log.d("NotificationListener", "Title: $title")
        Log.d("NotificationListener", "Content: $content")
        Log.d("NotificationListener", "Category: $category")
        Log.d("NotificationListener", "Icon ID: $icon")
        Log.d("NotificationListener", "Date: $date, Time: $onlyTime")

        CoroutineScope(Dispatchers.IO).launch {
            val notification = NotificationEntity(
                title = title,
                content = content,
                time = timestamp,
                humanReadTime = getHumanReadableTime(timestamp),
                date = date,       // 🆕 Store the date
                onlyTime = onlyTime, // 🆕 Store the time
                appName = appName,
                packageName = packageName,
                icon = icon,
                category = category
            )
            db.notificationDao().insertNotification(notification)
        }
    }

    fun convertTimestamp(timestamp: Long): Pair<String, String> {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())

        val date = dateFormat.format(Date(timestamp))
        val onlyTime = timeFormat.format(Date(timestamp))

        return Pair(date, onlyTime)
    }



    fun getHumanReadableTime(timestamp: Long): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }


    override fun onNotificationRemoved(sbn: StatusBarNotification) {
        Log.d("NotificationListener", "🗑️ Notification removed: ${sbn.packageName}")
    }

    // 🔹 Function to get app name from package name
    private fun getAppNameFromPackage(packageName: String): String {
        return try {
            val pm = packageManager
            val appInfo = pm.getApplicationInfo(packageName, 0)
            pm.getApplicationLabel(appInfo).toString()
        } catch (e: PackageManager.NameNotFoundException) {
            packageName // Return package name if app name is not found
        }
    }


}
