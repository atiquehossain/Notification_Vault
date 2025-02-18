
package com.nexgenscript.notilisson.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
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

    // State to track expanded groups
    val expandedGroups = remember { mutableStateMapOf<String, Boolean>() }

    val groupedNotifications = notifications
        .filter { it.appName == appName }
        .groupBy { notification ->
            val groupName = notification.title.replace(Regex("\\(.*?\\)"), "").split(":").first().trim()
            if (groupName == notification.title) null else groupName // Null for individual messages
        }

    val groupedTitles = groupedNotifications.keys.filterNotNull().toSet() // Ensure it's a Set for efficient lookups

    val titles = notifications
        .filter { it.appName == appName }
        .groupBy { it.title }
        .filterKeys { title ->
            val cleanedTitle = title.replace(Regex("\\(.*?\\)"), "").split(":").first().trim()
            cleanedTitle !in groupedTitles
        }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(appName) })
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            items(groupedNotifications.filterKeys { it != null }.toList()) { (groupName, messages) ->
                GroupedTitleItem(groupName = groupName!!, messages = messages)
            }


            items(titles.keys.toList()) { title ->
                val cleanedTitle = title.replace(Regex("\\(.*?\\)"), "").trim()

                TitleItem(cleanedTitle) {
                    navController.navigate("notificationList/$appName/$title")
                }
            }
        }
    }
}


/*
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
*/


/*package com.nexgenscript.notilisson.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.unit.dp
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

    // Process notifications
    val groupedNotifications = notifications
        .filter { it.appName == appName }
        .groupBy { notification ->
            val groupName = notification.title.replace(Regex("\\(.*?\\)"), "").split(":").first().trim()
            if (groupName == notification.title) null else groupName // Null for standalone messages
        }

    Scaffold(
        topBar = { TopAppBar(title = { Text(appName) }) }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(paddingValues)
        ) {
            groupedNotifications.forEach { (groupName, messages) ->
                if (groupName != null) {
                    // Group name - Clicking navigates to MemberListScreen
                    item {
                        TitleItem(
                            title = groupName,
                            onClick = {
                                navController.navigate("groupNotificationsList/$appName/$groupName")
                            }
                        )
                    }
                } else {
                    // Standalone messages - Clicking navigates to NotificationListScreen directly
                    messages.forEach { notification ->
                        item {
                            TitleItem(
                                title = notification.title,
                                onClick = {
                                    navController.navigate("notificationList/$appName/${notification.title}")
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}*/










