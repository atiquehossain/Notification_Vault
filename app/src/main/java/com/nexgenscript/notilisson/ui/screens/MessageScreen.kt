package com.nexgenscript.notilisson.ui.screens


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.nexgenscript.notilisson.data.NotificationDatabase
import com.nexgenscript.notilisson.navigation.BottomNavigationBar
import com.nexgenscript.notilisson.utlls.isNotificationListenerEnabled
import com.nexgenscript.notilisson.utlls.requestNotificationAccess
import com.nexgenscript.notilisson.viewmodel.NotificationViewModel
import com.nexgenscript.notilisson.viewmodel.NotificationViewModelFactory
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun MessageScreen(
    navController: NavController,
    selectedTab: String,
    onTabSelected: (String) -> Unit
) {
    val context = LocalContext.current

    if (isNotificationListenerEnabled(context)) {
        MainContent(navController , selectedTab, onTabSelected)
    } else {
        PermissionRequiredScreen()
    }
}

@Composable
fun MainContent(
    navController: NavController,
    selectedTab: String,
    onTabSelected: (String) -> Unit
) {
    val context = LocalContext.current

    val dao = NotificationDatabase.getDatabase(context).notificationDao()
    val factory = NotificationViewModelFactory(dao)
    val viewModel: NotificationViewModel = viewModel(factory = factory)
    val notifications by viewModel.notifications.observeAsState(initial = emptyList())

    val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    val greeting = when (hour) {
        in 5..11 -> "Good Morning"
        in 12..16 -> "Good Afternoon"
        in 17..21 -> "Good Evening"
        else -> "Good Night"
    }

    // Calculate today's date in "yyyy-MM-dd" format
    val todayDate = remember {
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    }
    // Filter notifications for today (assuming notification.date is in the same format)
    val todayNotifications = notifications.filter { it.date == todayDate }
    val totalToday = todayNotifications.size

    // Calculate the app with maximum notifications
    val appNotificationCounts = notifications.groupingBy { it.appName }.eachCount()
    val maxEntry = appNotificationCounts.maxByOrNull { it.value }
    val maxAppName = maxEntry?.key ?: "N/A"
   // val maxCount = maxEntry?.value ?: 0

    Scaffold(
        bottomBar = {
            BottomNavigationBar(selectedTab = selectedTab, onTabSelected = onTabSelected)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Header with dynamic greeting and subtitle
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column {
                    Text(
                        text = greeting,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Here's your notification summary for today.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            // Summary Row: Today's Total Notifications and Top App
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Card for Today's Total Notifications
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Today's Total",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = totalToday.toString(),
                            style = MaterialTheme.typography.headlineMedium
                        )
                    }
                }
                // Card for App with Maximum Notifications
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Top App",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = maxAppName,
                            style = MaterialTheme.typography.headlineSmall
                        )



                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f)
                )

            Button(onClick = { navController.navigate("appList") }) {
                Text("Go to App List")


            }
        }
    }
}


    @Composable
    fun PermissionRequiredScreen() {
        val context = LocalContext.current
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Notifications,
                    contentDescription = "Notifications",
                    modifier = Modifier.size(96.dp),
                    tint = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Notification Access Required",
                    style = MaterialTheme.typography.headlineSmall,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "To save your notifications, please enable notification access in settings. This permission is required for the app to function properly.",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = { requestNotificationAccess(context) },
                    modifier = Modifier.fillMaxWidth(0.8f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "Enable Notifications",
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            }
        }
    }

