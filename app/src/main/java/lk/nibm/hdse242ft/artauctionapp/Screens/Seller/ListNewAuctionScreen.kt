package lk.nibm.hdse242ft.artauctionapp.Screens.Seller


import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import lk.nibm.hdse242ft.artauctionapp.Api.ListArtworkRequest
import lk.nibm.hdse242ft.artauctionapp.Api.ListArtworkResponse
import lk.nibm.hdse242ft.artauctionapp.Api.RetrofitInstance
import lk.nibm.hdse242ft.artauctionapp.Local_DB.AppDatabase
import lk.nibm.hdse242ft.artauctionapp.Reusable_Components.*
import lk.nibm.hdse242ft.artauctionapp.ui.theme.HeadingColor
import lk.nibm.hdse242ft.artauctionapp.ui.theme.SecondaryBg

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListNewArtworkScreen(navController: NavController) {
    // --- State ---
    val artworkTitle = remember { mutableStateOf("") }
    val description = remember { mutableStateOf("") }
    val artistName = remember { mutableStateOf("") }
    val width = remember { mutableStateOf("") }
    val height = remember { mutableStateOf("") }
    val yearCreated = remember { mutableStateOf("") }
    val startingBid = remember { mutableStateOf("") }
    val buyNowPrice = remember { mutableStateOf("") }
    val predictedPrice = remember { mutableStateOf("") }

    val categoryOptions = listOf("Oil Painting", "Watercolor", "Acrylic", "Sketch")
    var selectedCategory by remember { mutableStateOf(categoryOptions[0]) }
    val mediumOptions = listOf("Canvas", "Paper", "Wood", "Digital")
    var selectedMedium by remember { mutableStateOf(mediumOptions[0]) }
    val styleOptions = listOf("Abstract", "Realism", "Impressionism")
    var selectedStyle by remember { mutableStateOf(styleOptions[0]) }

    var isCategoryExpanded by remember { mutableStateOf(false) }
    var isMediumExpanded by remember { mutableStateOf(false) }
    var isStyleExpanded by remember { mutableStateOf(false) }

    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var showImageDialog by remember { mutableStateOf(false) }

    val location = remember { mutableStateOf("") }
    val context = LocalContext.current
    val startDateTime = remember { mutableStateOf("") }
    val endDateTime = remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()
    // Toast state
    var toastMessage by remember { mutableStateOf("") }
    var toastType by remember { mutableStateOf(ToastType.INFO) }
    var showToastState by remember { mutableStateOf(false) }

    val sharedPref = LocalContext.current.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    //val userName = sharedPref.getString("user_name", "Guest") ?: "Guest"
    val userPhoto = sharedPref.getString("user_photo", null)

// Build full URL using the value from SharedPreferences
//    val userPhotoUrl = if (!userPhoto.isNullOrEmpty()) {
//        "http://192.168.8.187/HND_FINAL/Uploads/profile_photos/$userPhoto"
//    } else null

    val db = AppDatabase.getDatabase(context)
    val userDao = db.userDao()

    // Observe user from DB
    val user by userDao.getUserFlow().collectAsState(initial = null)

    val userName = user?.name ?: "Guest"
    val userId = user?.id ?: 0
    Scaffold(
        topBar = {
            AppTopBar(
                userName = userName,
                userPhoto = getLocalProfilePhoto(context, userId), // local path
                onProfileClick = { // Navigate to ProfileUpdateScreen
                    navController.navigate("profile_screen")}
            )
        },
        bottomBar = { AppBottomBar(navController = navController, currentScreen = "") }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(16.dp)
        ) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "List New Artwork",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = HeadingColor
                    )
                }
            }
            item {
                SectionCard("Artwork Information") {
                    AuctionTextField(artworkTitle.value, { artworkTitle.value = it }, "Artwork Title")
                    AuctionTextField(description.value, { description.value = it }, "Description", height = 120.dp)
                    AuctionDropdown(categoryOptions, selectedCategory, { selectedCategory = it }, isCategoryExpanded, { isCategoryExpanded = it }, "Category")
                    AuctionTextField(artistName.value, { artistName.value = it }, "Artist Name")
                }
            }

            item {
                SectionCard("Dimensions & Style") {
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        AuctionTextField(width.value, { width.value = it }, "Width (cm)", Modifier.weight(1f))
                        AuctionTextField(height.value, { height.value = it }, "Height (cm)", Modifier.weight(1f))
                    }
                    AuctionTextField(yearCreated.value, { yearCreated.value = it }, "Year Created")
                    AuctionDropdown(mediumOptions, selectedMedium, { selectedMedium = it }, isMediumExpanded, { isMediumExpanded = it }, "Medium")
                    AuctionDropdown(styleOptions, selectedStyle, { selectedStyle = it }, isStyleExpanded, { isStyleExpanded = it }, "Style")
                }
            }

            item {
                SectionCard("Auction Details") {
                    ImagePickerWithDialog(
                        selectedImageUri = selectedImageUri,
                        onImageSelected = { selectedImageUri = it }
                    )



                    LocationPermissionButton(
                        context = context,
                        onLocationReceived = { address, lat, lon ->
                            location.value = address
                            Toast.makeText(context, "Lat: $lat, Lng: $lon", Toast.LENGTH_SHORT).show()
                        }
                    ) { onClick ->
                        AuctionTextField(
                            value = location.value,
                            onValueChange = { location.value = it },
                            label = "Location",
                            trailingIcon = {
                                IconButton(onClick = { onClick() }) {
                                    Icon(
                                        imageVector = Icons.Default.LocationOn,
                                        contentDescription = "Get Location",
                                        tint = HeadingColor
                                    )
                                }
                            }
                        )
                    }

                    AuctionTextField(startingBid.value, { startingBid.value = it }, "Starting Bid Price")
                    AuctionTextField(buyNowPrice.value, { buyNowPrice.value = it }, "Buy Now Price")


                    // ✅ Auction Start & End Time
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.fillMaxWidth()) {
                        DateTimePickerField(
                            label = "Start Time",
                            value = startDateTime.value,
                            onValueChange = { startDateTime.value = it },
                            modifier = Modifier.weight(1f) // 👈 share row space
                        )

                        DateTimePickerField(
                            label = "End Time",
                            value = endDateTime.value,
                            onValueChange = { endDateTime.value = it },
                            modifier = Modifier.weight(1f) // 👈 share row space
                        )
                    }



                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        AuctionTextField(predictedPrice.value, { predictedPrice.value = it }, "Predicted Price", Modifier.weight(1f))
                        FilledTonalButton(onClick = { /* TODO: Predict */ }) { Text("Predict") }
                    }
                }
            }



            item {
                var isLoading by remember { mutableStateOf(false) }
                Button(
                    onClick = {
                        if (isLoading) return@Button // ⛔ Prevent multiple clicks
                        isLoading = true

                        val sharedPref = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
                        val publishBy = sharedPref.getString("user_email", "") ?: "Unknown"

                        val photoBase64 = selectedImageUri?.let { uriToBase64(context, it) } ?: ""
                        // --- Prepare Data ---
                        val request = ListArtworkRequest(
                            title = artworkTitle.value,
                            description = description.value,
                            publish_by = publishBy, // dynamically set from login
                            category = selectedCategory,
                            artist = artistName.value,
                            width_cm = width.value.toIntOrNull() ?: 0,
                            height_cm = height.value.toIntOrNull() ?: 0,
                            year_created = yearCreated.value.toIntOrNull() ?: 0,
                            medium = selectedMedium,
                            style = selectedStyle,
                            location = location.value,
                            image_path = photoBase64,
                            auction_start = startDateTime.value,
                            auction_end = endDateTime.value,
                            starting_price = startingBid.value.toDoubleOrNull() ?: 0.0,
                            buy_now_price = buyNowPrice.value.toDoubleOrNull(),
                            predicted_price = predictedPrice.value.toDoubleOrNull()
                        )

                        // --- Call API using RetrofitInstance ---
                        RetrofitInstance.api.listArtwork(request).enqueue(object : retrofit2.Callback<ListArtworkResponse> {
                            override fun onResponse(
                                call: retrofit2.Call<ListArtworkResponse>,
                                response: retrofit2.Response<ListArtworkResponse>
                            ) {
                                scope.launch {
                                    delay(3000) // wait 1.5 seconds before resetting
                                    isLoading = false // ✅ Enable button again
                                }

                                if (response.isSuccessful && response.body()?.status == "success") {
                                    //Toast.makeText(context, "Artwork Listed Successfully!", Toast.LENGTH_SHORT).show()


                                    // Show success toast
                                    toastMessage = response.body()?.message ?: "success"
                                    toastType = ToastType.SUCCESS
                                    showToastState = true

                                    navController.popBackStack()

                                } else {
                                    //Toast.makeText(context, response.body()?.message ?: "Error", Toast.LENGTH_SHORT).show()

                                    // Show error toast
                                    toastMessage = response.body()?.message ?: "Error"
                                    toastType = ToastType.ERROR
                                    showToastState = true
                                }
                            }

                            override fun onFailure(call: retrofit2.Call<ListArtworkResponse>, t: Throwable) {
                            //  Toast.makeText(context, "Network Error: ${t.message}", Toast.LENGTH_SHORT).show()
                                scope.launch {
                                    delay(3000) // wait 1.5 seconds before resetting
                                    isLoading = false // ✅ Enable button again
                                }

                                // Show error toast
                                toastMessage = "Network Error : ${t.message}"
                                toastType = ToastType.ERROR
                                showToastState = true

                            }
                        })
                    },
                    enabled = !isLoading, // 🚫 Disable button while loading
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = if (isLoading) "Listing..." else "List Artwork",
                        fontSize = 18.sp, // 👈 Set font size here
                        fontWeight = FontWeight.Bold // (optional) make it bold
                    )
                }

            }

        }
    }

// Display toast overlay
    if (showToastState) {
        SwipeDismissToast(
            message = toastMessage,
            type = toastType,
            onDismiss = { showToastState = false }
        ) }


}
