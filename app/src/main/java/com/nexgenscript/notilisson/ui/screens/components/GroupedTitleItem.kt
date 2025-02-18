package com.nexgenscript.notilisson.ui.screens.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.nexgenscript.notilisson.data.NotificationEntity


@Composable
fun GroupedTitleItem(
    groupName: String,
    messages: List<NotificationEntity>,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        Text(
            text = groupName,
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = "${messages.size} messages",
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray
        )
    }
}




