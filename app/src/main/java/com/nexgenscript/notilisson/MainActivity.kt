package com.nexgenscript.notilisson

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import com.nexgenscript.notilisson.data.NotificationDatabase
import com.nexgenscript.notilisson.navigation.AppNavigation
import com.nexgenscript.notilisson.utlls.isNotificationListenerEnabled
import com.nexgenscript.notilisson.utlls.requestNotificationAccess
import com.nexgenscript.notilisson.viewmodel.NotificationViewModel
import com.nexgenscript.notilisson.viewmodel.NotificationViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val dao = NotificationDatabase.getDatabase(application).notificationDao()
        val viewModel = ViewModelProvider(this, NotificationViewModelFactory(dao))[NotificationViewModel::class.java]

        // ✅ Check and request notification access
        if (!isNotificationListenerEnabled(this)) {
            requestNotificationAccess(this)
        }

        setContent {
            val navController = rememberNavController()
            AppNavigation(navController, viewModel)
        }
    }
}
