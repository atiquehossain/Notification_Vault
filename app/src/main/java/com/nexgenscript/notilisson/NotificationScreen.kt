package com.nexgenscript.notilisson

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Modifier
import com.nexgenscript.notilisson.data.NotificationEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreen(
    notifications: List<NotificationEntity>,
    selectedApp: String?,
    onAppSelected: (String?) -> Unit,
    onDelete: (NotificationEntity) -> Unit
) {
    val appNames = notifications.map { it.packageName }.distinct() // Get unique app package names

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Notifications", style = MaterialTheme.typography.headlineMedium) },
                actions = {
                    DropdownMenu(appNames, selectedApp, onAppSelected)
                }
            )
        }
    ) { paddingValues ->
        val filteredNotifications = notifications.filter {
            selectedApp == null || it.packageName == selectedApp
        }

        if (filteredNotifications.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(MaterialTheme.colorScheme.background),
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {
                Text("No notifications available", style = MaterialTheme.typography.bodyLarge)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(MaterialTheme.colorScheme.background)
            ) {
                items(notifications) { notification ->
                    NotificationCard(notification)
                }
            }
        }
    }
}
