package com.nexgenscript.notilisson

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreen(
    viewModel: NotificationViewModel,
    selectedApp: String?,
    onAppSelected: (String?) -> Unit
) {
    val notifications by viewModel.notifications.observeAsState(emptyList())
    val notificationCount by viewModel.notificationCount.observeAsState(0) // ✅ Fetch count here

    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }

    // Filter notifications based on search query & selected app
    val filteredNotifications = notifications.filter { notification ->
        (selectedApp == null || notification.packageName == selectedApp) &&
                (searchQuery.text.isEmpty() || notification.title.contains(searchQuery.text, ignoreCase = true)) &&
                (notification.title != notification.appName) // Exclude items where title == appName
    }

    Scaffold(
     /*   topBar = {
            TopAppBar(
                title = { Text("Notifications", style = MaterialTheme.typography.headlineSmall) },
                actions = {
                    SearchBar(searchQuery) { searchQuery = it }
                }
            )
        },*/
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.clearAllNotifications() },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Delete, contentDescription = "Clear All")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            Text(
                text = "Today's Notifications:  $notificationCount",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(8.dp)
            )

            if (filteredNotifications.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No notifications available", style = MaterialTheme.typography.bodyLarge)
                }
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(filteredNotifications) { notification ->
                        NotificationCard(notification, viewModel::deleteNotification)
                    }
                }
            }
        }
    }
}
