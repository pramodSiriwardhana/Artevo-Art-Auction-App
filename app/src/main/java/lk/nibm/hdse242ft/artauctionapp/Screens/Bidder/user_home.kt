package lk.nibm.hdse242ft.artauctionapp.Screens.Bidder

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.launch
import lk.nibm.hdse242ft.artauctionapp.Local_DB.AppDatabase
import lk.nibm.hdse242ft.artauctionapp.R
import lk.nibm.hdse242ft.artauctionapp.Reusable_Components.AppButton
import lk.nibm.hdse242ft.artauctionapp.ui.theme.BgColor
import lk.nibm.hdse242ft.artauctionapp.ui.theme.ButtonBg
import lk.nibm.hdse242ft.artauctionapp.ui.theme.ButtonText
import lk.nibm.hdse242ft.artauctionapp.ui.theme.HeadingColor
import lk.nibm.hdse242ft.artauctionapp.ui.theme.TextColor
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserHomeScreen(navController: NavController) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    // Load user data from SharedPreferences
    val sharedPref = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    val userName = sharedPref.getString("user_name", "Guest") ?: "Guest"
    val userEmail = sharedPref.getString("user_email", "") ?: ""
    val userAddress = sharedPref.getString("user_address", "") ?: ""
    val userPhoto = sharedPref.getString("user_photo", null)
    val scope = rememberCoroutineScope()
    val userPhotoUrl = userPhoto?.let {
        "http://192.168.8.187/HND_FINAL/Uploads/profile_photos/$it"
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("User Home", fontWeight = FontWeight.Bold) },
                actions = {
                    IconButton(
                        onClick = {

                            val sharedPref = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

                            // 1️⃣ Delete local photo
                            val localPhotoPref = sharedPref.getString("user_photo_local", null)
                            if (!localPhotoPref.isNullOrEmpty()) {
                                val photoFile = File(localPhotoPref)
                                if (photoFile.exists()) {
                                    val deleted = photoFile.delete()
                                    Log.d("LOGOUT", "Local profile photo deleted: $deleted, path: ${photoFile.absolutePath}")
                                } else {
                                    Log.d("LOGOUT", "Photo file does not exist at path: ${photoFile.absolutePath}")
                                }
                            } else {
                                Log.d("LOGOUT", "No local photo path found in SharedPreferences")
                            }


                            // 2️⃣ Clear SharedPreferences
                            sharedPref.edit().clear().apply()

                            // 3️⃣ Clear local Room DB
                            scope.launch {
                                val db = AppDatabase.getDatabase(context)
                                db.userDao().clearUser()
                            }

                            // 4️⃣ Navigate to login
                            navController.navigate("login") {
                                popUpTo("profile_screen") { inclusive = true }
                            }
                    }
                    ) {
                        Icon(Icons.Default.Logout, contentDescription = "Logout")
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(BgColor)
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                // Profile photo
                if (userPhotoUrl != null) {
                    Image(
                        painter = rememberAsyncImagePainter(userPhotoUrl),
                        contentDescription = "Profile Photo",
                        modifier = Modifier
                            .size(120.dp)
                            .padding(8.dp)
                    )
                } else {
                    Image(
                        painter = painterResource(R.drawable.default_profile),
                        contentDescription = "Default Profile",
                        modifier = Modifier.size(120.dp)
                    )
                }
                Spacer(Modifier.height(16.dp))
                Text(userName, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = HeadingColor)
                Text(userEmail, fontSize = 16.sp, color = TextColor)
                Text(userAddress, fontSize = 16.sp, color = TextColor)
            }

            item {
                Spacer(Modifier.height(24.dp))
                AppButton(
                    text = "Browse Auctions",
                    onClick = { navController.navigate("auction_list_screen") }
                )
                Spacer(Modifier.height(16.dp))
                AppButton(
                    text = "My Bids",
                    onClick = { navController.navigate("my_bids_screen") }
                )
                Spacer(Modifier.height(16.dp))
                AppButton(
                    text = "Profile",
                    onClick = { navController.navigate("profile_screen") }
                )
                Spacer(Modifier.height(16.dp))

                Button(
                    onClick = { navController.navigate("user_list") },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ButtonBg,
                        contentColor = ButtonText
                    ),
                    shape = RoundedCornerShape(26.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text("View Users in Local DB", fontSize = 18.sp)
                }
            }
        }
    }
}
