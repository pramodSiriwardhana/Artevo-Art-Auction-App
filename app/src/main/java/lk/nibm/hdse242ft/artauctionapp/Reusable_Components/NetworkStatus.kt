package lk.nibm.hdse242ft.artauctionapp.Reusable_Components

import android.content.Context
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.delay


@Composable
fun NetworkStatusChecker(
    context: Context = LocalContext.current,
    onConnected: () -> Unit,
    onDisconnected: () -> Unit
) {
    var showToast by remember { mutableStateOf(false) }
    var toastMessage by remember { mutableStateOf("") }
    var toastType by remember { mutableStateOf(ToastType.INFO) }

    // Observe network changes every 3 seconds
    LaunchedEffect(Unit) {
        while (true) {
            val isOnline = NetworkUtils.hasInternetConnection(context)
            if (isOnline) {
                onConnected()
            } else {
                toastMessage = "No Internet Connection"
                toastType = ToastType.ERROR
                showToast = true
                onDisconnected()
            }
            delay(3000) // check every 3s
        }
    }

    if (showToast) {
        SwipeDismissToast(
            message = toastMessage,
            type = toastType
        ) { showToast = false }
    }
}
