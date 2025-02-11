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
import androidx.compose.ui.draw.clip
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

    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }

    // Filter notifications based on search query & selected app
    val filteredNotifications = notifications.filter { notification ->
        (selectedApp == null || notification.packageName == selectedApp) &&
                (searchQuery.text.isEmpty() || notification.title.contains(searchQuery.text, ignoreCase = true)) &&
                (notification.title != notification.appName) // Exclude items where title == appName
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Notifications", style = MaterialTheme.typography.headlineSmall) },
                actions = {
                    SearchBar(searchQuery) { searchQuery = it }
                 /*   DropdownMenu(
                        items = notifications.map { it.packageName }.distinct(),
                        selectedApp = selectedApp,
                        onAppSelected = onAppSelected
                    )*/
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.clearAllNotifications() },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Delete, contentDescription = "Clear All")
            }
        }
    ) { paddingValues ->
        if (filteredNotifications.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(MaterialTheme.colorScheme.background),
                contentAlignment = Alignment.Center
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
                items(filteredNotifications) { notification ->
                    NotificationCard(notification, viewModel::deleteNotification)
                }
            }
        }
    }
}

@Composable
fun SearchBar(query: TextFieldValue, onQueryChange: (TextFieldValue) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.surfaceVariant),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Default.Search, contentDescription = "Search", modifier = Modifier.padding(8.dp))
        BasicTextField(
            value = query,
            onValueChange = onQueryChange,
            modifier = Modifier.weight(1f),
            textStyle = MaterialTheme.typography.bodyLarge
        )
    }
}

