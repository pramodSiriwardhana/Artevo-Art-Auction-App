package lk.nibm.hdse242ft.artauctionapp.Screens

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import kotlinx.coroutines.delay
import lk.nibm.hdse242ft.artauctionapp.Api.Artwork
import lk.nibm.hdse242ft.artauctionapp.Api.Live_ArtworkResponse
import lk.nibm.hdse242ft.artauctionapp.Api.RetrofitInstance
import lk.nibm.hdse242ft.artauctionapp.Local_DB.AppDatabase
import lk.nibm.hdse242ft.artauctionapp.Reusable_Components.*
import lk.nibm.hdse242ft.artauctionapp.ui.theme.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.Duration
// Mock data classes for preview
data class ArtistStats(
    val totalSales: String,
    val liveAuctions: String,
    val totalBids: String,
    val totalArtworks: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArtistDashboardScreen(navController: NavController, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val sharedPref = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    val userId = sharedPref.getInt("user_id", -1)

    var liveAuctions by remember { mutableStateOf(listOf<Artwork>()) }
    var isConnected by remember { mutableStateOf(NetworkUtils.hasInternetConnection(context)) }
    var isLoading by remember { mutableStateOf(false) }

    // ---------------------------
    // Toast state
    // ---------------------------
    var toastMessage by remember { mutableStateOf<String?>(null) }
    var toastType by remember { mutableStateOf(ToastType.INFO) }

    // ✅ Only refresh when no artworks found
    // ✅ Keep auto-refreshing FOREVER until artworks appear
    LaunchedEffect(userId) {
        if (userId == -1) return@LaunchedEffect

        suspend fun tryFetch() {
            if (!NetworkUtils.hasInternetConnection(context)) {
                isConnected = false
                toastMessage = "No Internet Connection"
                toastType = ToastType.ERROR
                return
            }

            fetchLiveArtworks(
                userId,
                onStart = { isLoading = true },
                onComplete = { artworks ->
                    isLoading = false
                    liveAuctions = artworks

                    // ✅ Clear previous error on success
                    if (toastMessage?.startsWith("Network Error") == true ||
                        toastMessage?.startsWith("Failed") == true
                    ) {
                        toastMessage = null
                    }
                },
                onError = { errorMsg ->
                    toastMessage = errorMsg
                    toastType = ToastType.ERROR
                    isLoading = false
                }
            )
        }

        // 🌀 Continuous refresh until artworks appear
        while (true) {
            tryFetch()
//            delay(
//                if (liveAuctions.isEmpty()) 3000L // ⏳ keep refreshing every 3s when empty
//               else 60_000L   // 🕐 check every 1 min if artworks exist (optional safety)
//            )

            if (liveAuctions.isNotEmpty()) break  // ✅ Stop refreshing once data appears
            delay(3000L) // ⏳ retry every 3 seconds while empty
        }
    }



    // 🧠 Initial fetch
    LaunchedEffect(userId) {
        if (isConnected && userId != -1) {
            fetchLiveArtworks(userId, onStart = { isLoading = true }, onComplete = {
                isLoading = false
                liveAuctions = it
            }, onError = { isLoading = false })
        }
    }

    val stats = ArtistStats(
        totalSales = "Rs. 15,500",
        liveAuctions = liveAuctions.size.toString(),
        totalBids = "42",
        totalArtworks = "45"
    )
//    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context)
    val userDao = db.userDao()

    // Observe user from DB
    val user by userDao.getUserFlow().collectAsState(initial = null)

    val userName = user?.name ?: "Guest"

//    val userName = sharedPref.getString("user_name", "Guest") ?: "Guest"
//    val userPhoto = sharedPref.getString("user_photo", null)
//    val userPhotoUrl = if (!userPhoto.isNullOrEmpty()) {
//        "http://192.168.8.187/HND_FINAL/Uploads/profile_photos/$userPhoto"
//    } else null


    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                AppTopBar(
                    userName = "Hello $userName",
                    userPhoto = getLocalProfilePhoto(context, userId), // local path
                    onProfileClick = { navController.navigate("profile_screen") }
                )
            },
            bottomBar = { AppBottomBar(navController = navController, currentScreen = "Dashboard") }
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(BgColor),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                item {
                    Text("Quick Actions", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextColor)
                    Spacer(modifier = Modifier.height(12.dp))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        item {
                            QuickActionButton(
                                "New Auction",
                                Icons.Default.AddCircle,
                                ButtonBg,
                                ButtonText
                            ) { navController.navigate("list_new_auction_screen") }
                        }
                        item {
                            QuickActionButton("Manage Bids", Icons.Default.Gavel, HeadingColor, ButtonText) {}
                        }
                        item {
                            QuickActionButton("Ship Orders", Icons.Default.LocalShipping, ButtonHoverBg, ButtonText) {}
                        }
                    }
                }

                item {
                    Text("Your Performance", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextColor)
                    Spacer(modifier = Modifier.height(12.dp))
                    DashboardStatsCard(stats)
                }

                item {
                    Text("My Live Auctions", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextColor)
                    Spacer(modifier = Modifier.height(12.dp))
                    when {
                        isLoading -> {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 24.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                        !isConnected -> {
                            AnimatedStatusMessage_ArtistDashBoard(
                                message = "No Internet Connection",
                                icon = { Icon(Icons.Default.WifiOff, contentDescription = null, tint = Color.White) },
                                backgroundColor = Color(0xFFE53935),
                                position = Alignment.TopCenter
                            )
                        }
                        liveAuctions.isEmpty() -> {
                            AnimatedStatusMessage_ArtistDashBoard(
                                message = "You have no live artworks.",
                                icon = { Icon(Icons.Default.ArtTrack, contentDescription = null, tint = Color.White) },
                                backgroundColor = Color(0xFF42A5F5),
                                position = Alignment.TopCenter
                            )
                        }
                        else -> {
                            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                liveAuctions.forEach { artwork ->
                                    LiveAuctionCardFromArtwork(artwork)
                                }
                            }
                        }
                    }

                }
            }
        }
    }
    // 🧾 Toast display
    toastMessage?.let { message ->
        SwipeDismissToast(
            message = message,
            type = toastType,
            onDismiss = { toastMessage = null }
        )
    }

}

private fun fetchLiveArtworks(
    userId: Int,
    onStart: () -> Unit,
    onComplete: (List<Artwork>) -> Unit,
    onError: (String) -> Unit
) {
    if (userId == -1) {
        onError("Invalid user ID")
        return
    }

    onStart()

    RetrofitInstance.api.getLiveArtworks(userId).enqueue(object : Callback<Live_ArtworkResponse> {
        override fun onResponse(
            call: Call<Live_ArtworkResponse>,
            response: Response<Live_ArtworkResponse>
        ) {
            if (response.isSuccessful && response.body()?.status == "success") {
                val artworks = response.body()?.data ?: emptyList()
                onComplete(artworks)
            } else {
                onError("Failed to load artworks (Server Error)")
            }
        }

        override fun onFailure(call: Call<Live_ArtworkResponse>, t: Throwable) {
            onError("Network Error: ${t.localizedMessage ?: "Unable to reach server"}")
        }
    })
}



@Composable
fun LiveAuctionCardFromArtwork(artwork: Artwork) {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    val endTime = try {
        LocalDateTime.parse(artwork.auction_end, formatter)
    } catch (e: Exception) { null }

    // Remaining time state, updates every minute
    var remainingTime by remember { mutableStateOf("") }
    LaunchedEffect(endTime) {
        if (endTime != null) {
            while (true) {
                val now = LocalDateTime.now()
                val duration = Duration.between(now, endTime)
                remainingTime = if (!duration.isNegative) {
                    val hours = duration.toHours()
                    val minutes = duration.toMinutes() % 60
                    "${hours}h ${minutes}m left"
                } else {
                    "Auction ended"
                }
                delay(60_000) // update every minute
            }
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
            .padding(vertical = 8.dp)
            .clip(RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = SecondaryBg),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Row(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            // Artwork Image
            AsyncImage(
                model = "http://192.168.8.187/HND_FINAL/Uploads/artworks/${artwork.image_path}",
                contentDescription = artwork.title,
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(12.dp))
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Title
                Text(
                    text = artwork.title,
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold,
                    color = HeadingColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                // Description
//                Text(
//                    text = artwork.description ?: "",
//                    fontSize = 12.sp,
//                    color = TextColor.copy(alpha = 0.7f),
//                    maxLines = 2,
//                    overflow = TextOverflow.Ellipsis
//                )

                Spacer(modifier = Modifier.height(4.dp))

                // Prices
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = "Start: Rs. ${String.format("%.2f", artwork.starting_price)}",
                        fontSize = 13.sp,
                        color = TextColor.copy(alpha = 0.7f)
                    )
                    artwork.buy_now_price?.let { buyNow ->
                        Text(
                            text = "Buy Now: Rs. ${String.format("%.2f", artwork.buy_now_price)}",
                            fontSize = 13.sp,
                            color = ButtonBg
                        )
                    }
                }

                // Remaining Time
                Text(
                    text = remainingTime,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFFFFC107)
                )
            }
        }
    }
}


@Composable
fun QuickActionButton(label: String, icon: ImageVector, bgColor: Color, iconColor: Color, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .width(100.dp)
            .height(100.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(bgColor)
            .clickable(onClick = onClick)
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(imageVector = icon, contentDescription = label, tint = iconColor, modifier = Modifier.size(32.dp))
        Spacer(Modifier.height(8.dp))
        Text(text = label, fontSize = 12.sp, fontWeight = FontWeight.Medium, color = iconColor, maxLines = 1, overflow = TextOverflow.Ellipsis)
    }
}

@Composable
fun DashboardStatsCard(stats: ArtistStats) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = SecondaryBg),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Overview", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextColor)
            Spacer(modifier = Modifier.height(12.dp))
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                StatRow(label = "Total Sales", value = stats.totalSales, icon = Icons.Default.TrendingUp)
                Divider(color = BgColor, thickness = 1.dp)
                StatRow(label = "Live Auctions", value = stats.liveAuctions, icon = Icons.Default.Gavel)
                Divider(color = BgColor, thickness = 1.dp)
                StatRow(label = "Total Bids", value = stats.totalBids, icon = Icons.Default.BarChart)
                Divider(color = BgColor, thickness = 1.dp)
                StatRow(label = "Total Artworks", value = stats.totalArtworks, icon = Icons.Default.Palette)
            }
        }
    }
}

@Composable
fun StatRow(label: String, value: String, icon: ImageVector) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Icon(imageVector = icon, contentDescription = label, tint = HeadingColor, modifier = Modifier.size(20.dp))
            Text(label, fontSize = 14.sp, color = TextColor.copy(alpha = 0.7f))
        }
        Text(value, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextColor)
    }
}

@Composable
fun AnimatedStatusMessage_ArtistDashBoard(
    message: String,
    icon: @Composable (() -> Unit)? = null,
    backgroundColor: Color = Color(0xFFE57373),
    position: Alignment = Alignment.TopCenter
) {
    var visible by remember { mutableStateOf(true) }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(animationSpec = tween(400)) + slideInVertically(
            initialOffsetY = { -it / 2 },
            animationSpec = tween(400)
        ),
        exit = fadeOut(animationSpec = tween(400)) + slideOutVertically(
            targetOffsetY = { -it / 2 },
            animationSpec = tween(400)
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            contentAlignment = position
        ) {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = backgroundColor),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                modifier = Modifier.wrapContentWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .wrapContentWidth()
                        .padding(horizontal = 24.dp, vertical = 16.dp)
                ) {
                    icon?.let {
                        it()
                        Spacer(modifier = Modifier.width(12.dp))
                    }
                    Text(
                        text = message,
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
