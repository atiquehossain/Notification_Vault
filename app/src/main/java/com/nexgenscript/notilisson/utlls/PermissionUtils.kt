package com.nexgenscript.notilisson.utlls
import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.text.TextUtils


fun isNotificationListenerEnabled(context: Context): Boolean {
        val contentResolver = Settings.Secure.getString(context.contentResolver, "enabled_notification_listeners")
        val packageName = context.packageName
        return !TextUtils.isEmpty(contentResolver) && contentResolver.contains(packageName)
    }

    fun requestNotificationAccess(context: Context) {
        val intent = Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)
        context.startActivity(intent)
    }

