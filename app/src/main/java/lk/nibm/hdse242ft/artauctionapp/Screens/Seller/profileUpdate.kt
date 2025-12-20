package lk.nibm.hdse242ft.artauctionapp.Screens.Seller
import android.content.Context
import android.net.Uri
import android.util.Base64
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import lk.nibm.hdse242ft.artauctionapp.R
import lk.nibm.hdse242ft.artauctionapp.Reusable_Components.*
import lk.nibm.hdse242ft.artauctionapp.ui.theme.HeadingColor
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import lk.nibm.hdse242ft.artauctionapp.Api.ChangePasswordRequest
import lk.nibm.hdse242ft.artauctionapp.Api.ChangePasswordResponse
import lk.nibm.hdse242ft.artauctionapp.Api.RetrofitInstance
import lk.nibm.hdse242ft.artauctionapp.Api.UpdateProfileImageRequest
import lk.nibm.hdse242ft.artauctionapp.Api.UpdateProfileImageResponse
import lk.nibm.hdse242ft.artauctionapp.Api.UpdateProfileRequest
import lk.nibm.hdse242ft.artauctionapp.Api.UpdateProfileResponse
import lk.nibm.hdse242ft.artauctionapp.Local_DB.AppDatabase
import java.io.File
import java.io.FileOutputStream

@Composable
fun ProfileUpdateScreen(
    navController: NavController,
    onUpdateProfile: (fullName: String, email: String, location: String, imageUri: Uri?) -> Unit,
    onChangePassword: (password: String, confirmPassword: String) -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // --- Image from SharedPreferences (keep existing logic) ---
    val sharedPref = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
//    val userPhotoPref = sharedPref.getString("user_photo", null)
//    val userPhotoUrl = userPhotoPref?.let {
//        "http://192.168.8.187/HND_FINAL/Uploads/profile_photos/$it"
//    }

    // Get userId from SharedPreferences or local DB
    val userId = sharedPref.getInt("user_id", 0)

// Get local profile photo path
    val localPhotoPath = getLocalProfilePhoto(context, userId)

// Convert local path to Uri
// --- State lifted ---
    var selectedImageUri by remember { mutableStateOf(localPhotoPath?.let { Uri.fromFile(File(it)) }) }

   // var selectedImageUri by remember { mutableStateOf(userPhotoUrl?.let { Uri.parse(it) }) }

    // --- Local DB for name, email, location ---
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }

    //--Location Fields--//
    var latitude by remember { mutableStateOf<Double?>(null) }
    var longitude by remember { mutableStateOf<Double?>(null) }
    // Load from local DB on composition
    LaunchedEffect(Unit) {
        val db = AppDatabase.getDatabase(context)
        val userDao = db.userDao()
        val user = userDao.getUser()
        user?.let {
            fullName = it.name
            email = it.email
            location = it.address ?: ""
            latitude = it.latitude ?: 0.0   // ← initialize here
            longitude = it.longitude ?: 0.0 // ← initialize here
        }
    }

    // --- Password fields ---
    var currentPassword by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    //--Toasts Fields--//
    var toastMessage by remember { mutableStateOf("") }
    var toastType by remember { mutableStateOf(ToastType.INFO) }
    var showToast by remember { mutableStateOf(false) }

    // Place these at the top level inside ProfileUpdateScreen, not inside item{}
    var isProfileUpdating by remember { mutableStateOf(false) }
    var isPasswordUpdating by remember { mutableStateOf(false) }



    // 🔌 Listen for network changes
    var isConnected by remember { mutableStateOf(true) }
    NetworkStatusChecker(
        context = context,
        onConnected = {
            isConnected = true
        },
        onDisconnected = {
            isConnected = false
            toastMessage = "No Internet Connection"
            toastType = ToastType.ERROR
            showToast = true
        }
    )



    Scaffold(
        topBar = {
            key(selectedImageUri) {  // <-- force recomposition on image change
                AppTopBar(
                    userName = fullName,
                    userPhoto = selectedImageUri?.path,  // path with dummy query
                    onProfileClick = { navController.navigate("profile_screen") }
                )
            }
        },
        bottomBar = {
            AppBottomBar(navController = navController, currentScreen = "Profile")
        }
    ) { innerPadding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // -------------------- Profile Section --------------------
            item {
                AppCard {
                    Text(
                        "Profile Information",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = HeadingColor
                    )
                    Spacer(Modifier.height(12.dp))

                    // Profile photo picker
                    ProfilePhotoPicker(
                        context = context,
                        imageUri = selectedImageUri,
                        placeholder = painterResource(id = R.drawable.default_profile),
                        onImageSelected = { uri ->
                            uri?.let {
                                // Save to internal storage
                                val fileName = "profile_$userId.jpg"
                                val file = File(context.filesDir, fileName)
                                context.contentResolver.openInputStream(it)?.use { input ->
                                    FileOutputStream(file, false).use { output ->
                                        input.copyTo(output)
                                    }
                                }

                                // Update image URI to reload Coil
                                selectedImageUri = Uri.parse("${file.path}?t=${System.currentTimeMillis()}")

                                // --- Call API to update MySQL ---
//                                val request = UpdateProfileImageRequest(
//                                    user_id = userId,
//                                    photo = fileName
//                                )

                                val bytes = file.readBytes()
                                val base64String = Base64.encodeToString(bytes, Base64.NO_WRAP)

                                val request = UpdateProfileImageRequest(
                                    user_id = userId,
                                    photo = base64String
                                )
                                Log.d("API_PROFILE_IMAGE_UPDATE", "Base64 (first 100 chars): ${base64String.take(100)}")
                                RetrofitInstance.api.updateProfileImage(request)
                                    .enqueue(object : retrofit2.Callback<UpdateProfileImageResponse> {
                                        override fun onResponse(
                                            call: retrofit2.Call<UpdateProfileImageResponse>,
                                            response: retrofit2.Response<UpdateProfileImageResponse>
                                        ) {
                                            val body = response.body()
                                            if(body?.status == "success"){
                                                toastMessage = "Profile image updated successfully"
                                                toastType = ToastType.SUCCESS
                                            } else {
                                                toastMessage = body?.message ?: "Failed to update profile image"
                                                toastType = ToastType.ERROR
                                            }
                                            showToast = true
                                        }

                                        override fun onFailure(
                                            call: retrofit2.Call<UpdateProfileImageResponse>,
                                            t: Throwable
                                        ) {
                                            toastMessage = "Network error: ${t.message}"
                                            toastType = ToastType.ERROR
                                            showToast = true
                                        }
                                    })
                            }
                        }
                    )

                    // Full name and email (from local DB)
                    AppTextField(
                        value = fullName,
                        onValueChange = { fullName = it },
                        label = "Full Name"
                    )
                    AppTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = "Email"
                    )

                    // Location picker (from local DB)
                    LocationPermissionButton(
                        context = context,
                        onLocationReceived = { addr, lat, lon ->
                            location = addr
                            latitude = lat
                            longitude = lon
                        }
                    ) { onClick ->
                        AppTextField(
                            value = location,
                            onValueChange = { location = it },
                            label = "Location",
                            trailingIcon = {
                                IconButton(onClick = { onClick() }) {
                                    Icon(Icons.Default.LocationOn, contentDescription = "Get Location", tint = HeadingColor)
                                }
                            }
                        )
                    }


                    Spacer(Modifier.height(32.dp))

                    AppButton(
                        text = if (isProfileUpdating) "Updating Profile..." else "Update Profile",
                        onClick = {
                            if (isProfileUpdating) return@AppButton
                            isProfileUpdating = true
                            scope.launch {
                                val db = AppDatabase.getDatabase(context)
                                val userDao = db.userDao()
                                val oldUser = userDao.getUser()
                                if (oldUser != null) {
                                    val userId = oldUser.id

                                    // --- Check if any field changed ---
                                    val hasChanges = (oldUser.name != fullName ||
                                            oldUser.email != email ||
                                            oldUser.address != location ||
                                            oldUser.latitude != latitude ||
                                            oldUser.longitude != longitude)

                                    if (!hasChanges) {
                                        toastMessage = "No changes to update."
                                        toastType = ToastType.INFO
                                        showToast = true
                                        isProfileUpdating = false // ✅ Reset here
                                        return@launch

                                    }


                                    // --- Prepare API request ---
                                    val request = UpdateProfileRequest(
                                        user_id = userId,
                                        name = fullName,
                                        email = email,
                                        address = location,
                                        latitude = latitude,
                                        longitude = longitude
                                    )
                                    // --- LOG API VALUES ---
                                    Log.d("API_PROFILE_UPDATE", "Request Values: user_id=$userId, name=$fullName, email=$email, address=$location, latitude=$latitude, longitude=$longitude")

                                    // --- Call API ---
                                    RetrofitInstance.api.updateProfile(request)
                                        .enqueue(object : retrofit2.Callback<UpdateProfileResponse> {
                                            override fun onResponse(
                                                call: retrofit2.Call<UpdateProfileResponse>,
                                                response: retrofit2.Response<UpdateProfileResponse>
                                            ) {
                                                scope.launch {
                                                    delay(2500) // wait 1.5 seconds before resetting
                                                    isProfileUpdating = false
                                                }
                                                val body = response.body()
                                                if (response.isSuccessful && body != null && body.status == "success") {

                                                    // --- Track changes ---
                                                    val changes = mutableListOf<String>()
                                                    if (oldUser.name != fullName) changes.add("Name: '${oldUser.name}' -> '$fullName'")
                                                    if (oldUser.email != email) changes.add("Email: '${oldUser.email}' -> '$email'")
                                                    if (oldUser.address != location) changes.add("Address: '${oldUser.address}' -> '$location'")
                                                    if (oldUser.latitude != latitude) changes.add("Latitude: '${oldUser.latitude}' -> '$latitude'")
                                                    if (oldUser.longitude != longitude) changes.add("Longitude: '${oldUser.longitude}' -> '$longitude'")

                                                    // --- Update only changed fields in local DB ---
                                                    scope.launch {
                                                        var updatedUser = oldUser
                                                        if (oldUser.name != fullName) updatedUser = updatedUser.copy(name = fullName)
                                                        if (oldUser.email != email) updatedUser = updatedUser.copy(email = email)
                                                        if (oldUser.address != location) updatedUser = updatedUser.copy(address = location)
                                                        if (oldUser.latitude != latitude) updatedUser = updatedUser.copy(latitude = latitude)
                                                        if (oldUser.longitude != longitude) updatedUser = updatedUser.copy(longitude = longitude)

                                                        userDao.insertUser(updatedUser)

                                                        // ✅ Confirm update
                                                        val confirmUser = userDao.getUser()
                                                        Log.d("ProfileUpdate", "Old User: $oldUser")
                                                        Log.d("ProfileUpdate", "Updated User: $confirmUser")
                                                        Log.d("ProfileUpdate", "Fields Updated: ${changes.joinToString(", ")}")
                                                    }

                                                    // Toast
                                                    toastMessage = body.message
                                                    toastType = ToastType.SUCCESS

                                                } else {
                                                    toastMessage = body?.message ?: "Profile update failed."
                                                    toastType = ToastType.ERROR
                                                }
                                                showToast = true

                                            }

                                            override fun onFailure(call: retrofit2.Call<UpdateProfileResponse>, t: Throwable) {
                                                toastMessage = "Network error: ${t.message}"
                                                toastType = ToastType.ERROR
                                                showToast = true
                                                scope.launch {
                                                    delay(2500) // wait 1.5 seconds before resetting
                                                    isProfileUpdating = false
                                                }
                                            }
                                        })
                                }
                            }
                        }
                    )




                }


            }

            // -------------------- Password Section --------------------
            item {
                AppCard {
                    Text(
                        "Change Password",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = HeadingColor
                    )
                    Spacer(Modifier.height(12.dp))

                    AppTextField(
                        value = currentPassword,
                        onValueChange = { currentPassword = it },
                        label = "Current Password",
                        isPassword = true
                    )
                    AppTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = "New Password",
                        isPassword = true
                    )
                    AppTextField(
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it },
                        label = "Confirm Password",
                        isPassword = true
                    )

                    Spacer(Modifier.height(32.dp))
                    AppButton(
                        text = if (isPasswordUpdating) "Updating Password..." else "Update Password",
                        onClick = {
                            if (isPasswordUpdating) return@AppButton
                            isPasswordUpdating = true
                            scope.launch {
                                // --- Validation ---
                                if (password.isBlank() || confirmPassword.isBlank() || currentPassword.isBlank()) {
                                    toastMessage = "All fields are required"
                                    toastType = ToastType.ERROR
                                    showToast = true
                                    return@launch
                                }

                                if (password != confirmPassword) {
                                    toastMessage = "New password and confirm password do not match"
                                    toastType = ToastType.ERROR
                                    showToast = true
                                    return@launch
                                }

                                // Get userId from SharedPreferences or Room
                                val db = AppDatabase.getDatabase(context)
                                val userDao = db.userDao()
                                val user = userDao.getUser() // suspend function
                                if (user == null) {
                                    // No user in local DB → show error
                                    toastMessage = "User not found in local database"
                                    toastType = ToastType.ERROR
                                    showToast = true
                                    return@launch
                                }

                                val userId = sharedPref.getInt("user_id", 0)
                                if (userId == 0) {
                                    toastMessage = "User not found in local storage"
                                    toastType = ToastType.ERROR
                                    showToast = true
                                    return@launch
                                }


                                // --- Call API ---
                                val request = ChangePasswordRequest(
                                    user_id = user.id,
                                    current_password = currentPassword,
                                    new_password = password,
                                    confirm_password = confirmPassword
                                )
                                // --- LOG API VALUES ---
                                Log.d("API_PASSWORD_UPDATE", "Request Values: user_id=${user.id}, current_password=$currentPassword, new_password=$password, confirm_password=$confirmPassword")




                                RetrofitInstance.api.changePassword(request)
                                    .enqueue(object : retrofit2.Callback<ChangePasswordResponse> {
                                        override fun onResponse(
                                            call: retrofit2.Call<ChangePasswordResponse>,
                                            response: retrofit2.Response<ChangePasswordResponse>
                                        ) {
                                            scope.launch {
                                                delay(2500) // wait 1.5 seconds before resetting
                                                isPasswordUpdating = false // ✅ Reset here
                                            }

                                            val body = response.body()
                                            if (response.isSuccessful && body != null && body.status == "success") {
                                                toastMessage = body.message
                                                toastType = ToastType.SUCCESS



                                            } else {
                                                toastMessage = body?.message ?: "Password update failed"
                                                toastType = ToastType.ERROR
                                            }
                                            showToast = true
                                        }

                                        override fun onFailure(call: retrofit2.Call<ChangePasswordResponse>, t: Throwable) {
                                            toastMessage = "Network error: ${t.message}"
                                            toastType = ToastType.ERROR
                                            showToast = true
                                            scope.launch {
                                                delay(2500) // wait 1.5 seconds before resetting
                                                isPasswordUpdating = false // ✅ Reset here
                                            }
                                        }
                                    })
                            }
                        }
                    )

                }
            }

            // -------------------- Logout --------------------
            item {
                AppCard {
                    AppButton(
                        text = "Log Out",
                        onClick = {
                            val sharedPref = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

                            // 1️⃣ Delete local photo
                            val photoPath = sharedPref.getString("user_photo_local", null)
                            if (!photoPath.isNullOrEmpty()) {
                                val photoFile = File(photoPath)
                                if (photoFile.exists()) {
                                    val deleted = photoFile.delete()
                                    Log.d("LOGOUT", "Photo deleted: $deleted, path: ${photoFile.absolutePath}")
                                }
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
                    )


                }
            }
        }

    }
    if (showToast) {
        SwipeDismissToast(
            message = toastMessage,
            type = toastType,
            onDismiss = { showToast = false }
        )
    }
}

