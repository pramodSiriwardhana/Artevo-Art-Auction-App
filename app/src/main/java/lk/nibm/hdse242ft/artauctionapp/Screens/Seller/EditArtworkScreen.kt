package lk.nibm.hdse242ft.artauctionapp.Screens.Seller

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.Looper
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import lk.nibm.hdse242ft.artauctionapp.ui.theme.*
import java.io.File
import java.io.IOException
import java.util.Locale
import kotlin.coroutines.resume

// Data class to represent an artwork with ALL details for editing
data class ArtworkDetails(
    val id: String,
    val title: String,
    val description: String,
    val category: String,
    val artistName: String,
    val widthCm: String,
    val heightCm: String,
    val yearCreated: String,
    val medium: String,
    val style: String,
    val latitude: Double,
    val longitude: Double,
    val imageUrl: String,
    val auctionStartTime: String,
    val auctionEndTime: String,
    val startingBidPrice: String,
    val buyNowPrice: String,
    val predictedPrice: String,
)

// A placeholder function to simulate fetching artwork data by ID
fun getArtworkById(id: String): ArtworkDetails? {
    return when (id) {
        "1" -> ArtworkDetails(
            id = "1",
            title = "Whispers of the Forest",
            description = "An acrylic painting of a mystical forest.",
            category = "Oil Painting",
            artistName = "Kasun Perera",
            widthCm = "80",
            heightCm = "120",
            yearCreated = "2023",
            medium = "Canvas",
            style = "Impressionism",
            latitude = 6.9271,
            longitude = 79.8612,
            imageUrl = "https://placehold.co/600x400/183045/D9D9D9?text=Artwork+1",
            auctionStartTime = "2025-08-01 10:00 AM",
            auctionEndTime = "2025-08-08 10:00 AM",
            startingBidPrice = "50000",
            buyNowPrice = "100000",
            predictedPrice = "75000"
        )
        "2" -> ArtworkDetails(
            id = "2",
            title = "Digital Sunset",
            description = "A vibrant digital painting of a sunset.",
            category = "Digital",
            artistName = "Kasun Perera",
            widthCm = "100",
            heightCm = "60",
            yearCreated = "2024",
            medium = "Digital",
            style = "Realism",
            latitude = 7.2906,
            longitude = 80.6337,
            imageUrl = "https://placehold.co/600x400/183045/D9D9D9?text=Artwork+2",
            auctionStartTime = "2025-08-05 09:00 AM",
            auctionEndTime = "2025-08-12 09:00 AM",
            startingBidPrice = "75000",
            buyNowPrice = "150000",
            predictedPrice = "110000"
        )
        else -> null
    }
}

// A top-level function to get the current location using coroutines.
@SuppressLint("MissingPermission")
private suspend fun getLocation(fusedLocationClient: FusedLocationProviderClient): Location? {
    return suspendCancellableCoroutine { continuation ->
        fusedLocationClient.lastLocation
            .addOnSuccessListener { lastLocation ->
                if (lastLocation != null) {
                    continuation.resume(lastLocation)
                } else {
                    // Last location is null, request a new one
                    val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000)
                        .setWaitForAccurateLocation(false)
                        .setMaxUpdates(1)
                        .build()

                    val locationCallback = object : LocationCallback() {
                        override fun onLocationResult(locationResult: LocationResult) {
                            locationResult.lastLocation?.let { newLocation ->
                                continuation.resume(newLocation)
                            } ?: run {
                                continuation.resume(null)
                            }
                            fusedLocationClient.removeLocationUpdates(this)
                        }
                    }

                    fusedLocationClient.requestLocationUpdates(
                        locationRequest,
                        locationCallback,
                        Looper.getMainLooper()
                    )
                }
            }
            .addOnFailureListener { e ->
                Log.e("EditArtworkScreen", "Failed to get location: ${e.message}")
                continuation.resume(null)
            }
    }
}

// A top-level function to get a human-readable, detailed address from coordinates
private fun getAddressFromLocation(context: Context, latitude: Double, longitude: Double): String {
    val geocoder = Geocoder(context, Locale.getDefault())
    return try {
        val addresses = geocoder.getFromLocation(latitude, longitude, 1)
        if (addresses != null && addresses.isNotEmpty()) {
            val address = addresses[0]
            val addressLines = (0..address.maxAddressLineIndex).mapNotNull {
                address.getAddressLine(it)
            }
            if (addressLines.isNotEmpty()) {
                addressLines.joinToString(separator = ", ")
            } else {
                // Fallback to individual components if address lines are not available
                val street = address.thoroughfare ?: ""
                val city = address.locality ?: ""
                val country = address.countryName ?: ""
                val fullAddress = listOf(street, city, country).filter { it.isNotEmpty() }
                if (fullAddress.isNotEmpty()) {
                    fullAddress.joinToString(separator = ", ")
                } else {
                    "Address not found"
                }
            }
        } else {
            "Address not found"
        }
    } catch (e: IOException) {
        "Geocoder service not available"
    } catch (e: Exception) {
        "Address not found"
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditArtworkScreen(navController: NavController, artworkId: String) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    // Location services setup
    val fusedLocationClient: FusedLocationProviderClient =
        remember { LocationServices.getFusedLocationProviderClient(context) }
    var locationFetching by remember { mutableStateOf(false) }

    // State to hold the current artwork data
    var artwork by remember { mutableStateOf<ArtworkDetails?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    // State for all the editable fields
    var artworkTitle by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var artistName by remember { mutableStateOf("") }
    var widthCm by remember { mutableStateOf("") }
    var heightCm by remember { mutableStateOf("") }
    var yearCreated by remember { mutableStateOf("") }
    var medium by remember { mutableStateOf("") }
    var style by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var auctionStartTime by remember { mutableStateOf("") }
    var auctionEndTime by remember { mutableStateOf("") }
    var startingBidPrice by remember { mutableStateOf("") }
    var buyNowPrice by remember { mutableStateOf("") }
    var predictedPrice by remember { mutableStateOf("") }

    // Image picker states
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var showImageSourceDialog by remember { mutableStateOf(false) }

    // The URI for the captured photo. It's nullable and will be updated just before
    // the camera is launched.
    var tempPhotoUri by remember { mutableStateOf<Uri?>(null) }

    // Dropdown menu state
    val categories = listOf("Oil Painting", "Watercolor", "Acrylic", "Sketch", "Mixed Media")
    val mediums = listOf("Canvas", "Paper", "Wood", "Digital", "Glass")
    val styles = listOf("Abstract", "Realism", "Impressionism", "Minimalism", "Surrealism")

    // Launchers for Activity Results
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { imageUri = it }
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            // The photo was successfully taken, so update the main imageUri
            tempPhotoUri?.let { imageUri = it }
        }
        // Whether the photo was taken or not, we should clear the temporary URI.
        tempPhotoUri = null
    }

    // New launcher to request camera permission
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            // Permission granted, now we can safely create the file and launch the camera.
            try {
                val photoFile = File.createTempFile(
                    "IMG_${System.currentTimeMillis()}_",
                    ".jpg",
                    context.cacheDir
                )
                tempPhotoUri = FileProvider.getUriForFile(
                    context,
                    "${context.packageName}.provider",
                    photoFile
                )
                tempPhotoUri?.let { cameraLauncher.launch(it) }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        } else {
            // Permission denied, show a message
            Log.e("EditArtworkScreen", "Camera permission denied")
        }
    }

    // New launcher to request location permissions
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                // Fine location permission granted, now get the location
                locationFetching = true
                coroutineScope.launch {
                    val locationResult = getLocation(fusedLocationClient)
                    locationResult?.let {
                        location = getAddressFromLocation(context, it.latitude, it.longitude)
                    } ?: run {
                        location = "Could not get location"
                    }
                    locationFetching = false
                }
            }
            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                // Only coarse location permission granted
                locationFetching = true
                coroutineScope.launch {
                    val locationResult = getLocation(fusedLocationClient)
                    locationResult?.let {
                        location = getAddressFromLocation(context, it.latitude, it.longitude)
                    } ?: run {
                        location = "Could not get location"
                    }
                    locationFetching = false
                }
            }
            else -> {
                // No location permission granted
                location = "Location permission denied"
                Log.e("EditArtworkScreen", "Location permission denied")
            }
        }
    }

    // LaunchedEffect to fetch the artwork data when the screen loads
    LaunchedEffect(key1 = artworkId) {
        artwork = getArtworkById(artworkId)
        isLoading = false
        artwork?.let {
            artworkTitle = it.title
            description = it.description
            category = it.category
            artistName = it.artistName
            widthCm = it.widthCm
            heightCm = it.heightCm
            yearCreated = it.yearCreated
            medium = it.medium
            style = it.style
            location = getAddressFromLocation(context, it.latitude, it.longitude)
            imageUri = Uri.parse(it.imageUrl)
            auctionStartTime = it.auctionStartTime
            auctionEndTime = it.auctionEndTime
            startingBidPrice = it.startingBidPrice
            buyNowPrice = it.buyNowPrice
            predictedPrice = it.predictedPrice
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = BgColor,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Edit Artwork",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = HeadingColor,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = HeadingColor
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = SecondaryBg
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    // TODO: Implement logic to save the updated artwork details
                    navController.popBackStack()
                },
                containerColor = HeadingColor,
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Save,
                    contentDescription = "Save Changes",
                    tint = ButtonText
                )
            }
        }
    ) { innerPadding ->
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = HeadingColor)
            }
        } else if (artwork == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "Artwork not found!",
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Artwork Information
                TextSectionHeader("Artwork Information")

                CustomOutlinedTextField(
                    value = artworkTitle,
                    onValueChange = { artworkTitle = it },
                    label = "Artwork Title"
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Image and Description
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Artwork Image with placeholder for image change
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(SecondaryBg),
                        contentAlignment = Alignment.Center
                    ) {
                        if (imageUri != null) {
                            AsyncImage(
                                model = imageUri,
                                contentDescription = artwork?.title,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    imageVector = Icons.Default.Camera,
                                    contentDescription = "No Image", // FIX: Changed `description` to `contentDescription`
                                    modifier = Modifier.size(48.dp),
                                    tint = TextColor.copy(alpha = 0.5f)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text("No Image", color = TextColor.copy(alpha = 0.5f))
                            }
                        }
                        // "Change Image" button overlay
                        Button(
                            onClick = { showImageSourceDialog = true },
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = HeadingColor)
                        ) {
                            Icon(imageVector = Icons.Default.Camera, contentDescription = "Change Image")
                        }
                    }

                    if (showImageSourceDialog) {
                        ImageSourceDialog(
                            onDismiss = { showImageSourceDialog = false },
                            onGallerySelected = {
                                galleryLauncher.launch("image/*")
                                showImageSourceDialog = false
                            },
                            onCameraSelected = {
                                showImageSourceDialog = false
                                // Check for camera permission before launching the camera
                                if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                                    try {
                                        val photoFile = File.createTempFile(
                                            "IMG_${System.currentTimeMillis()}_",
                                            ".jpg",
                                            context.cacheDir
                                        )
                                        tempPhotoUri = FileProvider.getUriForFile(
                                            context,
                                            "${context.packageName}.provider",
                                            photoFile
                                        )
                                        tempPhotoUri?.let { cameraLauncher.launch(it) }
                                    } catch (e: IOException) {
                                        e.printStackTrace()
                                    }
                                } else {
                                    // Permission not granted, request it
                                    cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                                }
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Editable Description field
                    CustomOutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = "Description",
                        maxLines = 10
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
                CustomDropdownMenu(
                    options = categories,
                    selectedOption = category,
                    onOptionSelected = { category = it },
                    label = "Category"
                )
                Spacer(modifier = Modifier.height(16.dp))
                CustomOutlinedTextField(
                    value = artistName,
                    onValueChange = { artistName = it },
                    label = "Artist Name",
                    readOnly = true
                )

                // Dimensions & Style
                Spacer(modifier = Modifier.height(24.dp))
                TextSectionHeader("Dimensions & Style")
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    CustomOutlinedTextField(
                        value = widthCm,
                        onValueChange = { widthCm = it },
                        label = "Width (cm)",
                        keyboardType = KeyboardType.Number,
                        modifier = Modifier.weight(1f)
                    )
                    CustomOutlinedTextField(
                        value = heightCm,
                        onValueChange = { heightCm = it },
                        label = "Height (cm)",
                        keyboardType = KeyboardType.Number,
                        modifier = Modifier.weight(1f)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                CustomOutlinedTextField(
                    value = yearCreated,
                    onValueChange = { yearCreated = it },
                    label = "Year Created",
                    keyboardType = KeyboardType.Number
                )
                Spacer(modifier = Modifier.height(16.dp))
                CustomDropdownMenu(
                    options = mediums,
                    selectedOption = medium,
                    onOptionSelected = { medium = it },
                    label = "Medium"
                )
                Spacer(modifier = Modifier.height(16.dp))
                CustomDropdownMenu(
                    options = styles,
                    selectedOption = style,
                    onOptionSelected = { style = it },
                    label = "Style"
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Location field with the new icon and click functionality
                CustomOutlinedTextField(
                    value = location,
                    onValueChange = { location = it },
                    label = "Location",
                    readOnly = locationFetching,
                    trailingIcon = {
                        if (locationFetching) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = HeadingColor,
                                strokeWidth = 2.dp
                            )
                        } else {
                            IconButton(onClick = {
                                // Request permissions on button click
                                locationPermissionLauncher.launch(
                                    arrayOf(
                                        Manifest.permission.ACCESS_FINE_LOCATION,
                                        Manifest.permission.ACCESS_COARSE_LOCATION
                                    )
                                )
                            }) {
                                Icon(
                                    imageVector = Icons.Default.LocationOn,
                                    contentDescription = "Get Current Location",
                                    tint = HeadingColor
                                )
                            }
                        }
                    }
                )

                // Auction Details
                Spacer(modifier = Modifier.height(24.dp))
                TextSectionHeader("Auction Details")

                // Time fields
                CustomOutlinedTextField(
                    value = auctionStartTime,
                    onValueChange = { auctionStartTime = it },
                    label = "Auction Start Time",
                    readOnly = true,
                    trailingIcon = {
                        IconButton(onClick = { /* TODO: Show date/time picker */ }) {
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowDown,
                                contentDescription = "Select Date/Time",
                                tint = HeadingColor
                            )
                        }
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
                CustomOutlinedTextField(
                    value = auctionEndTime,
                    onValueChange = { auctionEndTime = it },
                    label = "Auction End Time",
                    readOnly = true,
                    trailingIcon = {
                        IconButton(onClick = { /* TODO: Show date/time picker */ }) {
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowDown,
                                contentDescription = "Select Date/Time",
                                tint = HeadingColor
                            )
                        }
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
                CustomOutlinedTextField(
                    value = startingBidPrice,
                    onValueChange = { startingBidPrice = it },
                    label = "Starting Bid Price (LKR)",
                    keyboardType = KeyboardType.Number
                )
                Spacer(modifier = Modifier.height(16.dp))
                CustomOutlinedTextField(
                    value = buyNowPrice,
                    onValueChange = { buyNowPrice = it },
                    label = "Buy Now Price (LKR)",
                    keyboardType = KeyboardType.Number
                )

                // Predicted Price Section
                Spacer(modifier = Modifier.height(24.dp))
                TextSectionHeader("Predicted Price (LKR)")
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    CustomOutlinedTextField(
                        value = predictedPrice,
                        onValueChange = { predictedPrice = it },
                        label = "Predicted Price",
                        readOnly = true,
                        modifier = Modifier.weight(1f)
                    )
                    Button(
                        onClick = { /* TODO: Call prediction model */ },
                        modifier = Modifier.height(56.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = ButtonBg)
                    ) {
                        Text(text = "Predict", color = ButtonText)
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

// Utility Composables
@Composable
private fun TextSectionHeader(text: String) {
    Text(
        text = text,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        color = HeadingColor,
        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CustomOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier.fillMaxWidth(),
    keyboardType: KeyboardType = KeyboardType.Text,
    readOnly: Boolean = false,
    trailingIcon: @Composable (() -> Unit)? = null,
    maxLines: Int = 1
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = modifier,
        readOnly = readOnly,
        trailingIcon = trailingIcon,
        shape = RoundedCornerShape(8.dp),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = SecondaryBg,
            unfocusedContainerColor = SecondaryBg,
            focusedBorderColor = HeadingColor,
            unfocusedBorderColor = SecondaryBg,
            focusedLabelColor = HeadingColor,
            unfocusedLabelColor = TextColor.copy(alpha = 0.7f)
        ),
        maxLines = maxLines
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CustomDropdownMenu(
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier.fillMaxWidth()
) {
    var expanded by remember { mutableStateOf(false) }
    Box(modifier = modifier) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = Modifier.fillMaxWidth()
        ) {
            CustomOutlinedTextField(
                value = selectedOption,
                onValueChange = {},
                label = label,
                readOnly = true,
                modifier = Modifier.menuAnchor(),
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) }
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                options.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(item) },
                        onClick = {
                            onOptionSelected(item)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ImageSourceDialog(
    onDismiss: () -> Unit,
    onCameraSelected: () -> Unit,
    onGallerySelected: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Choose Image Source") },
        text = { Text("How would you like to add an image for this artwork?") },
        confirmButton = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                TextButton(onClick = onCameraSelected) {
                    Text("Take a Photo")
                }
                TextButton(onClick = onGallerySelected) {
                    Text("Choose from Gallery")
                }
            }
        }
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun EditArtworkScreenPreview() {
    ArtAuctionAppTheme {
        EditArtworkScreen(navController = rememberNavController(), artworkId = "1")
    }
}
