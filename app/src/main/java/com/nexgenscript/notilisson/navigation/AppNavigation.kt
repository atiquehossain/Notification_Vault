package com.nexgenscript.notilisson.navigation


import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.nexgenscript.notilisson.ui.screens.AppListScreen
import com.nexgenscript.notilisson.ui.screens.NotificationListScreen
import com.nexgenscript.notilisson.ui.screens.TitleListScreen
import com.nexgenscript.notilisson.viewmodel.NotificationViewModel

@Composable
fun AppNavigation(navController: NavHostController, viewModel: NotificationViewModel) {
    NavHost(navController = navController, startDestination = "appList") {
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
