package com.nexgenscript.notilisson.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.nexgenscript.notilisson.ui.screens.components.TitleItem
import com.nexgenscript.notilisson.viewmodel.NotificationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TitleListScreen(
    viewModel: NotificationViewModel,
    appName: String,
    navController: NavController
) {
    val notifications by viewModel.notifications.observeAsState(emptyList())
    val titles = notifications.filter { it.appName == appName }.groupBy { it.title }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(appName) } // Displays the app name in the AppBar
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(titles.keys.toList()) { title ->
                    val cleanedTitle = title.replace(Regex("\\(.*?\\)"), "").trim()

                    TitleItem(cleanedTitle) {
                        navController.navigate("notificationList/$appName/$title")
                    }
                }
            }
        }
    }
}
