package com.nexgenscript.notilisson

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
fun NotificationCard(notification: NotificationEntity, onDelete: (NotificationEntity) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(MaterialTheme.shapes.large),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .background(MaterialTheme.colorScheme.surface)
        ) {
            Text(text = notification.appName, style = MaterialTheme.typography.titleMedium)
            Text(text = notification.title, style = MaterialTheme.typography.bodyLarge)
            Text(text = notification.content, style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
            Text(text = notification.humanReadTime, style = MaterialTheme.typography.bodySmall, color = Color.LightGray)

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                TextButton(onClick = { onDelete(notification) }) {
                    Text("Delete", color = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}




