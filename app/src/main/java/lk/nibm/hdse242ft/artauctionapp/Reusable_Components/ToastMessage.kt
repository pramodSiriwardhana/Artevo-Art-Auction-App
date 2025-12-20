package lk.nibm.hdse242ft.artauctionapp.Reusable_Components

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

enum class ToastType {
    SUCCESS, ERROR, INFO, WARNING
}

@Composable
fun SwipeDismissToast(
    message: String,
    type: ToastType = ToastType.INFO,
    duration: Long = 3000L,
    onDismiss: () -> Unit
) {
    var visible by remember { mutableStateOf(true) }

    // Auto-dismiss after duration

    LaunchedEffect(message, type) {
        if (type != ToastType.SUCCESS) {
            delay(duration) // only auto-dismiss errors/info
            visible = false
            delay(300)
            onDismiss()
        } else {
            // Success: allow full duration
            delay(duration)
            visible = false
            delay(300)
            onDismiss()
        }
    }

    val backgroundColor = when (type) {
        ToastType.SUCCESS -> Color(0xFF4CAF50)
        ToastType.ERROR -> Color(0xFFF44336)
        ToastType.INFO -> Color(0xFF2196F3)
        ToastType.WARNING -> Color(0xFFFFC107) // yellow/orange color
    }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(animationSpec = tween(300)) + slideInVertically(
            initialOffsetY = { if (type == ToastType.SUCCESS) -it else it / 2 },
            animationSpec = tween(300)
        ),
        exit = fadeOut(animationSpec = tween(1300)) + slideOutVertically(
            targetOffsetY = { if (type == ToastType.SUCCESS) -it else -it / 2 },
            animationSpec = tween(1300)
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
        ) {
            if (type == ToastType.SUCCESS) {
                // Top-center for success
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(60.dp)) // distance from top
                    ToastBox(message, backgroundColor) { visible = false }
                }
            } else {
                // Center for error/info
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    ToastBox(message, backgroundColor) { visible = false }
                }
            }
        }
    }
}

@Composable
private fun ToastBox(
    message: String,
    backgroundColor: Color,
    onSwipeDismiss: () -> Unit
) {
    Box(
        modifier = Modifier
            .pointerInput(Unit) {
                detectVerticalDragGestures { _, dragAmount ->
                    if (dragAmount < -20) { // swipe up
                        onSwipeDismiss()
                    }
                }
            }
            .background(backgroundColor, RoundedCornerShape(16.dp))
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        Text(
            text = message,
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
