package com.nexgenscript.notilisson
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import com.nexgenscript.notilisson.viewmodel.NotificationViewModel

@Composable
fun NotificationListScreen(
    viewModel: NotificationViewModel,
    appName: String,
    title: String
) {
    val notifications by viewModel.getNotificationsByAppAndTitle(appName, title).observeAsState(emptyList())

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(notifications) { notification ->
                    NotificationCard(notification, viewModel::deleteNotification)
                }
            }
        }
    }
}
