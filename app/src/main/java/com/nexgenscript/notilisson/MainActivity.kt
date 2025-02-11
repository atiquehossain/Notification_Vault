package com.nexgenscript.notilisson

import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.nexgenscript.notilisson.data.NotificationDatabase
import com.nexgenscript.notilisson.data.NotificationEntity
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val db by lazy { NotificationDatabase.getDatabase(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 🔹 Step 1: Check if Notification Listener permission is granted
        if (!isNotificationServiceEnabled()) {
            requestNotificationPermission()
        }

        var notifications by mutableStateOf<List<NotificationEntity>>(emptyList())
        var selectedApp by mutableStateOf<String?>(null)

        lifecycleScope.launch {
            db.notificationDao().getAllNotifications().collectLatest { newNotifications ->
                notifications = newNotifications
            }
        }

        setContent {
            val viewModel = remember { NotificationViewModel(db.notificationDao()) }
            MaterialTheme(colorScheme = darkColorScheme()) {
                NotificationScreen(
                    viewModel = viewModel, // Pass ViewModel instead of notifications
                    selectedApp = selectedApp,
                    onAppSelected = { selectedApp = it }
                )
            }
        }

    }

    // 🔹 Function to check if Notification Listener permission is enabled
    private fun isNotificationServiceEnabled(): Boolean {
        val flat = Settings.Secure.getString(contentResolver, "enabled_notification_listeners")
        return !TextUtils.isEmpty(flat) && flat.contains(ComponentName(this, NotificationListener::class.java).flattenToString())
    }

    // 🔹 Function to request Notification Listener permission
    private fun requestNotificationPermission() {
        Toast.makeText(this, "Please enable notification access", Toast.LENGTH_LONG).show()
        startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS))
    }
}
