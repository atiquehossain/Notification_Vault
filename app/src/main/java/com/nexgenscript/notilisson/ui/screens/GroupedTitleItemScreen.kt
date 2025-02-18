package com.nexgenscript.notilisson.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.navigation.NavController
import com.nexgenscript.notilisson.viewmodel.NotificationViewModel
import androidx.compose.material3.*
import androidx.compose.ui.Modifier

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
import com.nexgenscript.notilisson.ui.screens.components.NotificationItem


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupedTitleItemScreen(
    viewModel: NotificationViewModel,
    appName: String,
    groupName: String,
    navController: NavController
) {
    val notifications by viewModel.getNotificationsByAppAndGroup(appName, groupName).observeAsState(emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Group: $groupName", style = MaterialTheme.typography.titleLarge)
                        Text("App: $appName", style = MaterialTheme.typography.titleSmall)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding)) {
            items(notifications) { notification ->
                NotificationItem(
                    notification = notification,
                    onDelete = { viewModel.deleteNotification(notification) }
                )
            }
        }
    }
}