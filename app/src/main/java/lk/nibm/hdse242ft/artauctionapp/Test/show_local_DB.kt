package lk.nibm.hdse242ft.artauctionapp.Test

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.launch
import lk.nibm.hdse242ft.artauctionapp.Local_DB.AppDatabase
import lk.nibm.hdse242ft.artauctionapp.Local_DB.User
import lk.nibm.hdse242ft.artauctionapp.Reusable_Components.getLocalProfilePhoto
import lk.nibm.hdse242ft.artauctionapp.ui.theme.BgColor
import lk.nibm.hdse242ft.artauctionapp.ui.theme.HeadingColor
import lk.nibm.hdse242ft.artauctionapp.ui.theme.TextColor
@Composable
fun ModernUserListScreen() {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var users by remember { mutableStateOf(listOf<User>()) }
    val baseUrl = "http://192.168.8.187/HND_FINAL/Uploads/profile_photos/"

    // Load users from Room DB
    LaunchedEffect(Unit) {
        coroutineScope.launch {
            val db = AppDatabase.getDatabase(context)
            users = db.userDao().getAllUsers()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BgColor)
            .padding(horizontal = 16.dp, vertical = 56.dp)
    ) {
        Text(
            text = "Users in Local DB",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = HeadingColor,
            modifier = Modifier
                .align(Alignment.CenterHorizontally) // centers horizontally
                .padding(bottom = 12.dp)
        )

        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {

            items(users) { user ->
                var expanded by remember { mutableStateOf(false) }
                val photoUrl = user.photo?.let { baseUrl + it }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Box(modifier = Modifier.padding(16.dp)) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { expanded = !expanded }
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                // Profile Image
                                // Profile Image
                                val localPhotoPath = getLocalProfilePhoto(context, user.id)
                                if (localPhotoPath != null) {
                                    // Load from local file
                                    Image(
                                        painter = rememberAsyncImagePainter("file://$localPhotoPath"),
                                        contentDescription = "Profile Image",
                                        modifier = Modifier
                                            .size(60.dp)
                                            .clip(CircleShape)
                                            .border(2.dp, HeadingColor, CircleShape)
                                    )
                                } else if (!user.photo.isNullOrEmpty()) {
                                    // Fallback: load from remote URL
                                    val photoUrl = baseUrl + user.photo
                                    Image(
                                        painter = rememberAsyncImagePainter(photoUrl),
                                        contentDescription = "Profile Image",
                                        modifier = Modifier
                                            .size(60.dp)
                                            .clip(CircleShape)
                                            .border(2.dp, HeadingColor, CircleShape)
                                    )
                                } else {
                                    // No image: show initials
                                    Box(
                                        modifier = Modifier
                                            .size(60.dp)
                                            .clip(CircleShape)
                                            .background(HeadingColor),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = user.name.firstOrNull()?.uppercase() ?: "?",
                                            color = BgColor,
                                            fontSize = 24.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = user.name.ifEmpty { "Unknown" },
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = HeadingColor
                                    )
                                    Text(
                                        text = user.role,
                                        color = TextColor,
                                        fontSize = 14.sp
                                    )
                                    Text(
                                        text = user.email,
                                        color = TextColor,
                                        fontSize = 14.sp
                                    )
                                }
                            }


                            // Expanded details
                            if (expanded) {
                                Spacer(modifier = Modifier.height(12.dp))
                                Column(modifier = Modifier.padding(start = 72.dp)) {
                                    Text("ID: ${user.user_unique_id ?: "N/A"}", color = TextColor)
                                    Text("Address: ${user.address ?: "N/A"}", color = TextColor)
                                    Text(
                                        "Lat: ${user.latitude?.toString() ?: "N/A"}, " +
                                                "Long: ${user.longitude?.toString() ?: "N/A"}",
                                        color = TextColor
                                    )
                                }
                            }
                        }

                        // Delete icon at bottom-right of card
                        IconButton(
                            onClick = {
                                coroutineScope.launch {
                                    val db = AppDatabase.getDatabase(context)
                                    db.userDao().deleteUserById(user.id)
                                    users = db.userDao().getAllUsers()
                                }
                            },
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .size(32.dp)
                                .background(MaterialTheme.colorScheme.errorContainer, CircleShape)
                        ) {
                            Icon(
                                painter = rememberAsyncImagePainter("https://img.icons8.com/ios-glyphs/30/ff0000/trash.png"),
                                contentDescription = "Delete User",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            }
        }
    }
}

