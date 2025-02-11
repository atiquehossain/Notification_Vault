package com.nexgenscript.notilisson;

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.nexgenscript.notilisson.data.NotificationDatabase
import com.nexgenscript.notilisson.data.NotificationEntity
import kotlinx.coroutines.launch
import androidx.compose.foundation.layout.*

class MainActivity : ComponentActivity() {

    private val db by lazy { NotificationDatabase.getDatabase(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var notifications by mutableStateOf<List<NotificationEntity>>(emptyList())
        var selectedApp by mutableStateOf<String?>(null)

        lifecycleScope.launch {
            notifications = db.notificationDao().getAllNotifications()
        }

        setContent {
            MaterialTheme(colorScheme = darkColorScheme()) {
                NotificationScreen(
                    notifications = notifications,
                    selectedApp = selectedApp,
                    onAppSelected = { selectedApp = it },
                    onDelete = { notification ->
                        lifecycleScope.launch {
                            db.notificationDao().deleteNotification(notification)
                            notifications = db.notificationDao().getAllNotifications()
                        }
                    }
                )
            }
        }
    }
}
