package lk.nibm.hdse242ft.artauctionapp.Screens

import android.content.Context
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Group
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import lk.nibm.hdse242ft.artauctionapp.Api.Artwork
import lk.nibm.hdse242ft.artauctionapp.Api.ArtworkResponse
import lk.nibm.hdse242ft.artauctionapp.Api.RetrofitInstance
import lk.nibm.hdse242ft.artauctionapp.Reusable_Components.*
import lk.nibm.hdse242ft.artauctionapp.ui.theme.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import androidx.compose.animation.fadeOut
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.ArtTrack
import androidx.compose.material.icons.filled.WifiOff
import lk.nibm.hdse242ft.artauctionapp.Local_DB.AppDatabase


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyArtScreen(navController: NavController, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context)
    val userDao = db.userDao()

    // Observe user from DB
    val user by userDao.getUserFlow().collectAsState(initial = null)

    val userName = user?.name ?: "Guest"
    val sharedPref = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    val userId = sharedPref.getInt("user_id", 0)
//    val userName = sharedPref.getString("user_name", "Guest") ?: "Guest"
    val userPhoto = sharedPref.getString("user_photo", null)
    val userPhotoUrl = userPhoto?.let { "http://192.168.8.187/HND_FINAL/Uploads/profile_photos/$it" }

    var myArtworks by remember { mutableStateOf(listOf<Artwork>()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isConnected by remember { mutableStateOf(true) }

    // ---------------------------
    // Toast state
    // ---------------------------
    var toastMessage by remember { mutableStateOf<String?>(null) }
    var toastType by remember { mutableStateOf(ToastType.INFO) }

    // ---------------------------
    // Local function to fetch artworks
    // ---------------------------
    fun fetchArtworks() {
        isLoading = true
        RetrofitInstance.api.getUserArtworks(userId).enqueue(object : Callback<ArtworkResponse> {
            override fun onResponse(call: Call<ArtworkResponse>, response: Response<ArtworkResponse>) {
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body?.status == "success") {
                        myArtworks = body.data ?: emptyList()
                        errorMessage = body?.message
                        // --------- Add this logging ---------
                        myArtworks.forEach { artwork ->
                            Log.d("DEBUG_ARTWORK_DETAIL", artwork.toString())
                        }

                        Log.d("DEBUG_ARTWORK", "Fetched artworks: $myArtworks")
                        myArtworks.forEach { artwork ->
                            Log.d("DEBUG_ARTWORK", "Artwork ID: ${artwork.id}, Title: ${artwork.title}")
                        }

                        if (myArtworks.isNotEmpty()) {
                            toastMessage = "Artworks loaded successfully".replace("\n", " ").trim()
                            toastType = ToastType.SUCCESS
                        }

                    } else {
                        myArtworks = emptyList()
                        errorMessage = body?.message ?: "Failed to load artworks"
                        toastMessage = errorMessage
                        toastType = ToastType.ERROR
                        Log.d("DEBUG_ARTWORK", "API returned error: ${body?.message}")
                    }
                } else {
                    myArtworks = emptyList()
                    errorMessage = "Failed to load artworks: ${response.message()}"
                    toastMessage = errorMessage
                    toastType = ToastType.ERROR
                    Log.d("DEBUG_ARTWORK", "Response not successful: ${response.message()}")
                }
                isLoading = false
            }

            override fun onFailure(call: Call<ArtworkResponse>, t: Throwable) {
                myArtworks = emptyList()
                errorMessage = t.message ?: "Unknown error"
                toastMessage = errorMessage
                toastType = ToastType.ERROR
                isLoading = false

                Log.d("DEBUG_ARTWORK", "Failed to fetch artworks: ${t.message}")
            }

        })
    }

    // ---------------------------
    // Network status checker
    // ---------------------------
    NetworkStatusChecker(
        context = context,
        onConnected = {
            isConnected = true
            if (myArtworks.isEmpty()) fetchArtworks()
        },
        onDisconnected = {
            isConnected = false
            toastMessage = "No internet connection"
            toastType = ToastType.ERROR
            isLoading = false
        }
    )

    // ---------------------------
    // UI
    // ---------------------------
    Scaffold(
        topBar = {
            AppTopBar(
                userName = userName,
                userPhoto = getLocalProfilePhoto(context, userId), // local path
                onProfileClick = { navController.navigate("profile_screen") }
            )
        },
        bottomBar = { AppBottomBar(navController = navController, currentScreen = "My Art") }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(BgColor)
        ) {
            when {
                !isConnected -> {
                    AnimatedStatusMessage(
                        message = "No internet connection",
                        icon = { Icon(Icons.Default.WifiOff, contentDescription = null, tint = Color.White) },
                        backgroundColor = Color(0xFFE53935), // deep red
                        position = Alignment.TopCenter // Alignment.TopEnd → top-right / Alignment.TopCenter → top-center / Alignment.BottomEnd → bottom-right
                    )
                }
                isLoading -> {
//                    CircularProgressIndicator(
//                        modifier = Modifier.align(Alignment.Center),
//                        color = Color(0xFF4CAF50),
//                        strokeWidth = 6.dp
//                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                myArtworks.isEmpty() -> {
                    AnimatedStatusMessage(
                        message = "You have not uploaded any artworks yet.",
                        icon = { Icon(Icons.Default.ArtTrack, contentDescription = null, tint = Color.White) },
                        backgroundColor = Color(0xFF42A5F5), // nice blue
                        position = Alignment.TopCenter
                    )
                }


             else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "My Artwork",
                                    fontSize = 26.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = HeadingColor
                                )
                            }
                        }
                        items(myArtworks) { artwork ->
                            ArtworkCard(artwork, navController)
                        }
                    }
                }
            }

            // ---------------------------
            // Toast messages
            // ---------------------------
            toastMessage?.let { msg ->
                SwipeDismissToast(
                    message = msg,
                    type = toastType,
                    onDismiss = { toastMessage = null }
                )
            }
        }
    }
}

// ---------------------------
// Artwork Card
// ---------------------------

@Composable
fun ArtworkCard(artwork: Artwork, navController: NavController) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = SecondaryBg),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {

            // Artwork image with overlay tag
            Box(modifier = Modifier.fillMaxWidth()) {
                val imageUrl = "http://192.168.8.187/HND_FINAL/Uploads/artworks/${artwork.image_path}"
                AsyncImage(
                    model = imageUrl,
                    contentDescription = artwork.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .height(220.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                )


                // Status tag
                if (artwork.status != null) {
                    Text(
                        text = artwork.status,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(12.dp)
                            .background(
                                color = when (artwork.status) {
                                    "Approved" -> Color(0xFF4CAF50)
                                    "Pending" -> Color(0xFFFFC107)
                                    else -> Color(0xFFE57373)
                                },
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            // Title & description
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = artwork.title,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = HeadingColor
                )
                Text(
                    text = artwork.description ?: "",
                    fontSize = 14.sp,
                    color = TextColor.copy(alpha = 0.7f),
                    maxLines = if (expanded) Int.MAX_VALUE else 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(top = 4.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Expand/Collapse
                ExpandCollapseButton(expanded = expanded) {
                    expanded = !expanded
                }

                if (expanded) {
                    Spacer(modifier = Modifier.height(12.dp))

                    // Info Grid (2 columns)
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            InfoItem(label = "Artist", value = artwork.artist ?: "-",modifier = Modifier.weight(1f))
                            InfoItem(label = "Category", value = artwork.category ?: "-",modifier = Modifier.weight(1f))
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            InfoItem(label = "Medium", value = artwork.medium ?: "-",modifier = Modifier.weight(1f))
                            InfoItem(label = "Style", value = artwork.style ?: "-",modifier = Modifier.weight(1f))
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            InfoItem(label = "Size", value = "${artwork.width_cm} x ${artwork.height_cm} cm",modifier = Modifier.weight(1f))
                            InfoItem(label = "Year", value = artwork.year_created.toString(),modifier = Modifier.weight(1f))
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            InfoItem(label = "Starting", value = "LKR ${artwork.starting_price}",modifier = Modifier.weight(1f))
                            InfoItem(label = "Buy Now", value = "LKR ${artwork.buy_now_price}",modifier = Modifier.weight(1f))
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            InfoItem(label = "Auction Start", value = artwork.auction_start ?: "-",modifier = Modifier.weight(1f))
                            InfoItem(label = "Auction End", value = artwork.auction_end ?: "-",modifier = Modifier.weight(1f))
                        }
                        InfoItem(label = "Location", value = artwork.location ?: "-",modifier = Modifier.weight(1f))
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Action buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        ModernButton(
                            text = "Edit",
                            icon = Icons.Default.Edit,
                            color = Color(0xFFFEA116),
                            onClick = { navController.navigate("edit_artwork_screen/${artwork.id}") }
                        )
                        ModernButton(
                            text = "Delete",
                            icon = Icons.Default.Delete,
                            color = Color(0xFFE57373),
                            onClick = { /* delete logic */ }
                        )
                        ModernButton(
                            text = "Bidders",
                            icon = Icons.Default.Group,
                            color = Color(0xFF4CAF50),
                            onClick = { /* navigate to bidders */ }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun InfoItem(label: String, value: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(vertical = 4.dp)) {
        Text(text = label, fontSize = 12.sp, color = Color(0xFF8E8E8E))
        Text(
            text = value,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = TextColor
        )
    }
}




@Composable
fun ModernButton(text: String, icon: androidx.compose.ui.graphics.vector.ImageVector, color: Color, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = color),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.height(44.dp)
    ) {
        Icon(icon, contentDescription = null, tint = Color.White)
        Spacer(modifier = Modifier.width(8.dp))
        Text(text, color = Color.White)
    }
}

// Expand/Collapse Button
@Composable
fun ExpandCollapseButton(expanded: Boolean, onClick: () -> Unit) {
    Box(modifier = Modifier.fillMaxWidth()) {  // BoxScope allows align()
        Button(
            onClick = onClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = if (expanded) HeadingColor.copy(alpha = 0.2f)
                else HeadingColor.copy(alpha = 0.1f)
            ),
            shape = RoundedCornerShape(50), // pill shape
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp, pressedElevation = 8.dp),
            modifier = Modifier.align(Alignment.CenterEnd) // now works
        ) {
            Icon(
                imageVector = if (expanded) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                contentDescription = null,
                tint = HeadingColor
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = if (expanded) "Hide Details" else "View Details",
                color = HeadingColor,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp
            )
        }
    }
}



// show output style div like (no atrwork)
@Composable
fun AnimatedStatusMessage(
    message: String,
    icon: @Composable (() -> Unit)? = null,
    backgroundColor: Color = Color(0xFFE57373), // default red
    position: Alignment = Alignment.TopEnd // default top-right
) {
    var visible by remember { mutableStateOf(true) }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(animationSpec = tween(400)) + slideInVertically(
            initialOffsetY = { -it / 2 }, // slide from top
            animationSpec = tween(400)
        ),
        exit = fadeOut(animationSpec = tween(400)) + slideOutVertically(
            targetOffsetY = { -it / 2 },
            animationSpec = tween(400)
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
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
