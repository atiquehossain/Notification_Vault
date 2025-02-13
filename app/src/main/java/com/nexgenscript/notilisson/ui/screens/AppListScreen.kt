import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.nexgenscript.notilisson.viewmodel.NotificationViewModel

@Composable
fun AppListScreen(
    viewModel: NotificationViewModel,
    navController: NavController
) {
    val notifications by viewModel.notifications.observeAsState(emptyList())
    val groupedApps = notifications.groupBy { it.appName }

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(groupedApps.keys.toList()) { appName ->
                    AppItem(appName) {
                        navController.navigate("titleList/$appName")
                    }
                }
            }
        }
    }
}