package com.nexgenscript.notilisson.navigation

import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.nexgenscript.notilisson.ui.screens.*
import com.nexgenscript.notilisson.viewmodel.NotificationViewModel

@Composable
fun AppNavigation(navController: NavHostController, viewModel: NotificationViewModel) {
    var selectedTab by rememberSaveable { mutableStateOf("Message") } // ✅ Preserves state on config changes

    NavHost(navController = navController, startDestination = "Message") {
        composable("Message") {
            MessageScreen(navController, selectedTab) { newTab ->
                selectedTab = newTab
                navController.navigate(newTab) {
                    popUpTo("Message") { inclusive = false }
                }
            }
        }
        composable("Notifications") {
            SettingsScreen(navController, selectedTab) { newTab ->
                selectedTab = newTab
                navController.navigate(newTab) {
                    popUpTo("Notifications") { inclusive = false }
                }
            }
        }
        composable("settings") {
            SettingsScreen(navController, selectedTab) { newTab ->
                selectedTab = newTab
                navController.navigate(newTab) {
                    popUpTo("settings") { inclusive = false }
                }
            }
        }
        composable("appList") {
            AppListScreen(viewModel, navController)
        }
        composable("titleList/{appName}") { backStackEntry ->
            val appName = backStackEntry.arguments?.getString("appName") ?: ""
            TitleListScreen(viewModel, appName, navController)
        }
        composable("notificationList/{appName}/{title}") { backStackEntry ->
            val appName = backStackEntry.arguments?.getString("appName") ?: ""
            val title = backStackEntry.arguments?.getString("title") ?: ""
            NotificationListScreen(viewModel, appName, title)
        }
    }
}
