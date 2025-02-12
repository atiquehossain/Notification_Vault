package com.nexgenscript.notilisson
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun DropdownMenu(appNames: List<String>, selectedApp: String?, onAppSelected: (String?) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        Button(onClick = { expanded = true }) {
            Text(text = selectedApp ?: "Select App")
        }

        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            DropdownMenuItem(text = { Text("All Apps") }, onClick = {
                onAppSelected(null)
                expanded = false
            })
            appNames.forEach { appName ->
                DropdownMenuItem(text = { Text(appName) }, onClick = {
                    onAppSelected(appName)
                    expanded = false
                })
            }
        }
    }
}

@Preview
@Composable
fun PreviewDropdownMenu() {
    DropdownMenu(
        appNames = listOf("WhatsApp", "Facebook", "Instagram"),
        selectedApp = null,
        onAppSelected = {}
    )
}
