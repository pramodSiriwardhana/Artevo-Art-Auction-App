package lk.nibm.hdse242ft.artauctionapp.Screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import lk.nibm.hdse242ft.artauctionapp.R
import lk.nibm.hdse242ft.artauctionapp.ui.theme.BgColor
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

@Composable
fun SplashScreen(modifier: Modifier = Modifier) {

    // Logo scale animation (breathing)
    val scaleAnim = rememberInfiniteTransition()
    val scale by scaleAnim.animateFloat(
        initialValue = 0.9f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = EaseInOutCubic),
            repeatMode = RepeatMode.Reverse
        )
    )

    // Text fade-in & upward movement
    var startAnimation by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { startAnimation = true }

    val alphaText by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(2500, easing = LinearOutSlowInEasing)
    )
    val offsetY by animateFloatAsState(
        targetValue = if (startAnimation) 0f else 40f,
        animationSpec = tween(2500, easing = LinearOutSlowInEasing)
    )

    // Animated background gradient
    val gradientAnim by rememberInfiniteTransition().animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(animation = tween(10000, easing = LinearEasing))
    )

    // Soft particles
    val particles = remember { List(30) { ParticleEnhanced() } }


    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = androidx.compose.ui.graphics.Brush.verticalGradient(
                    colors = listOf(
                        BgColor,
                        Color(0xFF1E293B).copy(alpha = 0.6f + 0.3f * gradientAnim)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {

        // Canvas for particles
        Canvas(modifier = Modifier.fillMaxSize()) {
            val centerX = size.width / 2
            val centerY = size.height / 2
            particles.forEach { p ->
                p.update()
                drawCircle(
                    color = Color.White.copy(alpha = p.alpha),
                    radius = p.radius,
                    center = Offset(centerX + p.x, centerY + p.y)
                )
                // subtle particle trail
                val sweep = ((System.currentTimeMillis() % 2000) / 2000f)
                drawLine(
                    color = Color.White.copy(alpha = 0.2f),
                    start = Offset(size.width * sweep - size.width, 0f),
                    end = Offset(size.width * sweep, size.height),
                    strokeWidth = 4f
                )
            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            // Logo with shimmer sweep
            Box(contentAlignment = Alignment.Center) {
                val logoAlpha by rememberInfiniteTransition().animateFloat(
                    initialValue = 0.85f,
                    targetValue = 1f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(1500, easing = EaseInOutCubic),
                        repeatMode = RepeatMode.Reverse
                    )
                )

                androidx.compose.foundation.Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Art Auction App Logo",
                    modifier = Modifier
                        .size(128.dp)
                        .scale(scale)
                        .alpha(logoAlpha)
                )

                // Shimmer sweep
                Canvas(modifier = Modifier.size(128.dp)) {
                    val sweep = ((System.currentTimeMillis() % 2000).toFloat()) / 2000f
                    drawLine(
                        color = Color.White.copy(alpha = 0.2f),
                        start = Offset(x = size.width * sweep - size.width, y = 0f),
                        end = Offset(x = size.width * sweep, y = size.height),
                        strokeWidth = 4f
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Art Auction",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier
                    .alpha(alphaText)
                    .offset(y = offsetY.dp)
            )

            Text(
                text = "Discover & Bid on Masterpieces",
                fontSize = 16.sp,
                color = Color.White,
                modifier = Modifier
                    .padding(top = 8.dp)
                    .alpha(alphaText)
                    .offset(y = offsetY.dp / 2)
            )
        }
    }
}

// Enhanced particle with soft trail & twinkle
class ParticleEnhanced {
    // Increase spawn range from 50 to 200 for wider spread
    var x = (-200..200).random().toFloat()
    var y = (-200..200).random().toFloat()
    var radius = (2..6).random().toFloat() // slightly larger particles
    var angle = Random.nextDouble(0.0, 360.0)
    var speed = Random.nextDouble(0.2, 0.7) // faster movement
    var alpha = Random.nextDouble(0.1, 0.5).toFloat()
    var alphaDirection = 1

    fun update() {
        x += (cos(angle) * speed).toFloat()
        y += (sin(angle) * speed).toFloat()
        angle += 0.01

        // subtle fade in/out
        alpha += 0.003f * alphaDirection
        if (alpha > 0.5f) alphaDirection = -1
        if (alpha < 0.1f) alphaDirection = 1
    }
}


