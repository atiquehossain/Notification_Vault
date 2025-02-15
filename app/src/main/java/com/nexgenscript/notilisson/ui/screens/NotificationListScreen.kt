package com.nexgenscript.notilisson.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.nexgenscript.notilisson.ui.screens.components.NotificationMessage
import com.nexgenscript.notilisson.viewmodel.NotificationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationListScreen(
    viewModel: NotificationViewModel,
    appName: String,
    title: String
) {
    val notifications by viewModel.getNotificationsByAppAndTitle(appName, title).observeAsState(emptyList())
    val groupedNotifications = notifications.groupBy { it.date }
    var textInput by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            title,
                            style = MaterialTheme.typography.titleLarge,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(appName, style = MaterialTheme.typography.titleSmall)
                    }
                }
            )
        },
        bottomBar = {
            TextField(
                value = textInput,
                onValueChange = { textInput = it },
                placeholder = { Text("Type a message...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                trailingIcon = {
                    IconButton(onClick = {
                        if (textInput.isNotBlank()) {
                         //   viewModel.addNotification(textInput)
                            textInput = ""
                        }
                    }) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.Send, contentDescription = "Send")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            groupedNotifications.forEach { (date, notifications) ->
                item {
                    Text(
                        text = date,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                items(notifications) { notification ->
                  //  NotificationMessage(notification = notification, isUser = notification.isUser, onDelete = viewModel::deleteNotification)
                    NotificationMessage(notification = notification, isUser = false, onDelete = viewModel::deleteNotification)
                }
            }
        }
    }
}

