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
import androidx.navigation.NavController
import com.nexgenscript.notilisson.viewmodel.NotificationViewModel

@Composable
fun TitleListScreen(
    viewModel: NotificationViewModel,
    appName: String,
    navController: NavController
) {
    val notifications by viewModel.notifications.observeAsState(emptyList())
    val titles = notifications.filter { it.appName == appName }.groupBy { it.title }

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(titles.keys.toList()) { title ->
                    TitleItem(title) {
                        navController.navigate("notificationList/$appName/$title")
                    }
                }
            }
        }
    }
}