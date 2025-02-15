package com.nexgenscript.notilisson.ui.screens.components

import android.util.Base64
import android.graphics.BitmapFactory
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.foundation.Image
import com.nexgenscript.notilisson.data.NotificationEntity

@Composable
fun NotificationMessage(notification: NotificationEntity, isUser: Boolean, onDelete: (NotificationEntity) -> Unit) {
    var showDialog by remember { mutableStateOf(false) }

    val profileImage = notification.profileImageBase64?.let { decodeBase64ToImage(it) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start,
        verticalAlignment = Alignment.Bottom
    ) {
        if (!isUser) {
            profileImage?.let {
                Image(
                    bitmap = it,
                    contentDescription = "Profile Image",
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(20.dp))
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
        }
        Column {
            Text(
                text = notification.humanReadTime,
                style = MaterialTheme.typography.labelSmall,
                color = Color.Gray,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 2.dp)
            )
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(18.dp))
                    .background(if (isUser) MaterialTheme.colorScheme.primary else Color.LightGray)
                    .clickable { showDialog = true }
                    .padding(12.dp)
            ) {
                Text(
                    text = notification.content,
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (isUser) Color.White else Color.Black,
                    textAlign = TextAlign.Start
                )
            }
        }
    }

    // Confirmation Dialog
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Delete Message") },
            text = { Text("Are you sure you want to delete this message?") },
            confirmButton = {
                TextButton(onClick = {
                    onDelete(notification)
                    showDialog = false
                }) {
                    Text("Delete", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

fun decodeBase64ToImage(base64String: String?): ImageBitmap? {
    return base64String?.let {
        try {
            val decodedBytes = Base64.decode(it, Base64.DEFAULT)
            val bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
            bitmap?.asImageBitmap()
        } catch (e: Exception) {
            null
        }
    }
}