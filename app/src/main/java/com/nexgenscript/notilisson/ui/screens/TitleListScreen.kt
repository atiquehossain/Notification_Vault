
package com.nexgenscript.notilisson.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.nexgenscript.notilisson.ui.screens.components.GroupedTitleItem
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

    // Filter notifications for the current app
    val appNotifications = notifications.filter { it.appName == appName }

    // Group notifications by their cleaned title
    val groupedNotifications = appNotifications.groupBy { notification ->
        val groupName = notification.title.replace(Regex("\\(.*?\\)"), "").split(":").first().trim()
        if (groupName == notification.title) null else groupName // Null for individual messages
    }

    // Extract grouped titles (non-null keys)
    val groupedTitles = groupedNotifications.keys.filterNotNull().toSet()

    // Extract individual titles (not part of any group)
    val individualTitles = appNotifications
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
            // Display grouped titles
            groupedNotifications.filterKeys { it != null }.forEach { (groupName, messages) ->
                item {
                    GroupedTitleItem(
                        groupName = groupName!!,
                        messages = messages,
                        onClick = {
                            navController.navigate("GroupedTitleItem/$appName/$groupName")
                        }
                    )
                }
            }

            // Display individual titles
            individualTitles.forEach { (title, messages) ->
                item {
                    TitleItem(
                        title = title,
                        onClick = {
                            navController.navigate("notificationList/$appName/$title")
                        }
                    )
                }
            }
        }
    }
}











