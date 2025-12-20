package lk.nibm.hdse242ft.artauctionapp.Screens

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import lk.nibm.hdse242ft.artauctionapp.Local_DB.AppDatabase

@Composable
fun AppStartScreen(navController: NavController, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var showSplash by remember { mutableStateOf(true) }
    var navigateTo by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        val db = AppDatabase.getDatabase(context)
        val user = db.userDao().getUser()

        navigateTo = if (user != null) {
            when (user.role.lowercase()) {
                "seller" -> "artist_dashboard_screen"
                "bidder" -> "user_home"
                else -> "login"
            }
        } else {
            "login"
        }

        delay(3000) // splash duration
        showSplash = false
    }

    Box(modifier = modifier.fillMaxSize()) {
        SplashScreen() // Your existing splash UI
    }

    if (!showSplash && navigateTo != null) {
        LaunchedEffect(navigateTo) {
            navController.navigate(navigateTo!!) {
                popUpTo("login") { inclusive = true }
            }
        }
    }
}
