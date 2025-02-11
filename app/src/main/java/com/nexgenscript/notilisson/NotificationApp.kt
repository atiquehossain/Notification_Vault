package com.nexgenscript.notilisson

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nexgenscript.notilisson.data.NotificationEntity

@Composable
fun NotificationApp(
    isServiceEnabled: Boolean,
    onRequestPermission: () -> Unit,
    onRestartService: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Notification Listener Status: ${if (isServiceEnabled) "Enabled" else "Disabled"}")

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onRequestPermission) {
            Text(text = "Request Notification Access")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onRestartService) {
            Text(text = "Restart Notification Service")
        }
    }
}

@Composable
fun NotificationCard(notification: NotificationEntity) {
    Card(
        modifier = Modifier.padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = notification.appName, style = MaterialTheme.typography.titleMedium)
            Text(text = notification.title, style = MaterialTheme.typography.bodyMedium)
            Text(text = notification.content, style = MaterialTheme.typography.bodySmall)
            Text(text = notification.time, style = MaterialTheme.typography.bodySmall)
        }
    }
}



