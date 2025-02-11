package com.nexgenscript.notilisson

import android.app.Notification
import android.content.pm.PackageManager
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import com.nexgenscript.notilisson.data.NotificationDatabase
import com.nexgenscript.notilisson.data.NotificationEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotificationListener : NotificationListenerService() {

    private val db by lazy { NotificationDatabase.getDatabase(applicationContext) }

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        val packageName = sbn.packageName
        val extras = sbn.notification.extras
        val title = extras.getString("android.title") ?: "No Title"
        val content = extras.getString("android.text") ?: "No Content"
        val timestamp = sbn.postTime

        // Get app name from package name
        val appName = getAppNameFromPackage(packageName)

        // Check if it's a messaging app
        if (isFromMessengerApp(sbn)) {
            Log.d("NotificationListener", "📩 Message from: $appName ($packageName)")
            Log.d("NotificationListener", "Title: $title")
            Log.d("NotificationListener", "Content: $content")

            // Store only messaging app notifications in Room database
            CoroutineScope(Dispatchers.IO).launch {
                val notification = NotificationEntity(
                    title = title,
                    content = content,
                    time = timestamp,
                    appName = appName,
                    packageName = packageName
                )
                db.notificationDao().insertNotification(notification)
            }
        } else {
            Log.d("NotificationListener", "❌ Ignored non-messaging app: $appName ($packageName)")
        }
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

    // 🔹 Function to check if a notification is from a messaging app
    private fun isFromMessengerApp(sbn: StatusBarNotification): Boolean {
        val extras = sbn.notification.extras

        // Check if the notification is categorized as a message
        val isMessageCategory = sbn.notification.category == Notification.CATEGORY_MESSAGE

        // Check if the notification has messaging-style data
        val hasMessagingStyle = extras.getParcelableArray("android.messages") != null

        return isMessageCategory || hasMessagingStyle
    }
}
