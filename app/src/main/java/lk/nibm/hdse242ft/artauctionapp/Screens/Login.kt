
package lk.nibm.hdse242ft.artauctionapp.Screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import lk.nibm.hdse242ft.artauctionapp.Api.LoginRequest
import lk.nibm.hdse242ft.artauctionapp.Api.LoginResponse
import lk.nibm.hdse242ft.artauctionapp.Api.RetrofitInstance
import lk.nibm.hdse242ft.artauctionapp.Reusable_Components.AppButton
import lk.nibm.hdse242ft.artauctionapp.Reusable_Components.AppCard
import lk.nibm.hdse242ft.artauctionapp.Reusable_Components.AppClickableText
import lk.nibm.hdse242ft.artauctionapp.Reusable_Components.AppTextField
import lk.nibm.hdse242ft.artauctionapp.ui.theme.BgColor
import lk.nibm.hdse242ft.artauctionapp.ui.theme.ButtonBg
import lk.nibm.hdse242ft.artauctionapp.ui.theme.HeadingColor
import lk.nibm.hdse242ft.artauctionapp.ui.theme.TextColor
import retrofit2.Call
import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import lk.nibm.hdse242ft.artauctionapp.Reusable_Components.SwipeDismissToast
import lk.nibm.hdse242ft.artauctionapp.Reusable_Components.ToastType

import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import lk.nibm.hdse242ft.artauctionapp.Local_DB.AppDatabase
import lk.nibm.hdse242ft.artauctionapp.Local_DB.User
import lk.nibm.hdse242ft.artauctionapp.Reusable_Components.downloadAndSaveImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun LoginScreen(navController: NavController, modifier: Modifier = Modifier) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current  // ✅ capture context here

    // Toast state
    var toastMessage by remember { mutableStateOf("") }
    var toastType by remember { mutableStateOf(ToastType.INFO) }
    var showToastState by remember { mutableStateOf(false) }
    var navigateTo by remember { mutableStateOf<String?>(null) }

    // ✅ Compose-friendly coroutine scope
    val coroutineScope = rememberCoroutineScope()
    val scope = rememberCoroutineScope()
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BgColor)
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        AppCard {
            Text(
                text = "Welcome Back!",
                fontSize = 32.sp,
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                color = HeadingColor
            )

            Spacer(modifier = Modifier.height(32.dp))

            AppTextField(
                value = email,
                onValueChange = { email = it },
                label = "Email",
                labelColor = ButtonBg,
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = "Email Icon") }
            )

            AppTextField(
                value = password,
                onValueChange = { password = it },
                label = "Password",
                labelColor = ButtonBg,
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "Lock Icon") },
                isPassword = true
            )

            Spacer(modifier = Modifier.height(32.dp))

            var isLoading by remember { mutableStateOf(false) }
            AppButton(
                text = if (isLoading) "Logging in..." else "Login",
                onClick = {
                    if (isLoading) return@AppButton // prevent re-clicks
                    isLoading = true
                    val request = LoginRequest(email = email, password = password)
                    RetrofitInstance.api.login(request)
                        .enqueue(object : retrofit2.Callback<LoginResponse> {
                            override fun onResponse(
                                call: Call<LoginResponse>,
                                response: retrofit2.Response<LoginResponse>
                            ) {


                                scope.launch {
                                    delay(3000) // wait 1.5 seconds before resetting
                                    isLoading  = false
                                }

                                val loginResponse = response.body()

                                if (response.isSuccessful && loginResponse?.status == "success") {
                                    //  Toast.makeText(context, loginResponse.message, Toast.LENGTH_SHORT).show()

                                    // Show success toast
                                    toastMessage = loginResponse.message ?: "Login successful!"
                                    toastType = ToastType.SUCCESS
                                    showToastState = true

                                    // ✅ Store user info in SharedPreferences
                                    val sharedPref = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
                                    with(sharedPref.edit()) {
                                        putInt("user_id", loginResponse.data?.id ?: 0)
                                        putString("user_unique_id", loginResponse.data?.user_unique_id ?: "")
                                        putString("user_name", loginResponse.data?.name ?: "")
                                        putString("user_email", loginResponse.data?.email ?: "") // ✅ store email
                                        putString("user_address", loginResponse.data?.address ?: "")
                                        putString("user_photo", loginResponse.data?.photo ?: "")
                                        putString("user_role", loginResponse.data?.role ?: "")
                                        putFloat("user_latitude", loginResponse.data?.latitude?.toFloat() ?: 0f)
                                        putFloat("user_longitude", loginResponse.data?.longitude?.toFloat() ?: 0f)
                                        apply()
                                    }
                                    // ✅ Debug logs
                                    Log.d("DEBUG_PREFS", "ID: ${loginResponse.data?.id}")
                                    Log.d("DEBUG_PREFS", "unique ID: ${loginResponse.data?.user_unique_id}")
                                    Log.d("DEBUG_PREFS", "Name: ${loginResponse.data?.name}")
                                    Log.d("DEBUG_PREFS", "Email: ${loginResponse.data?.email}")
                                    Log.d("DEBUG_PREFS", "Address: ${loginResponse.data?.address}")
                                    Log.d("DEBUG_PREFS", "Photo: ${loginResponse.data?.photo}")
                                    Log.d("DEBUG_PREFS", "Role: ${loginResponse.data?.role}")
                                    Log.d("DEBUG_PREFS", "Lat: ${loginResponse.data?.latitude}")
                                    Log.d("DEBUG_PREFS", "Long: ${loginResponse.data?.longitude}")


                                    // Store user info in Room DB
//                                    val user = User(
//                                        id = loginResponse.data?.id ?: 0,
//                                        user_unique_id = loginResponse.data?.user_unique_id ?: "",
//                                        name = loginResponse.data?.name ?: "",
//                                        email = loginResponse.data?.email ?: "",
//                                        role = loginResponse.data?.role ?: "",
//                                        photo = loginResponse.data?.photo,
//                                        address = loginResponse.data?.address,
//                                        latitude = loginResponse.data?.latitude,
//                                        longitude = loginResponse.data?.longitude
//                                    )
//
//                                   // Use coroutineScope from Compose
//                                    coroutineScope.launch {
//                                        val db = AppDatabase.getDatabase(context)
//                                        db.userDao().insertUser(user)
//                                    }

                                    coroutineScope.launch {
                                        // 1. Download and save image locally
                                        val photoUrl = loginResponse.data?.photo
                                        val localPhotoPath = if (!photoUrl.isNullOrEmpty()) {
                                            try {
                                                withContext(Dispatchers.IO) {
                                                    downloadAndSaveImage(
                                                        context,
                                                        "http://192.168.8.187/HND_FINAL/Uploads/profile_photos/$photoUrl",
                                                        "profile_${loginResponse.data?.id}.jpg"
                                                    )
                                                }
                                            } catch (e: Exception) {
                                                e.printStackTrace()
                                                null
                                            }
                                        } else null

                                        // 2. Save user info in Room DB
                                        val user = User(
                                            id = loginResponse.data?.id ?: 0,
                                            user_unique_id = loginResponse.data?.user_unique_id ?: "",
                                            name = loginResponse.data?.name ?: "",
                                            email = loginResponse.data?.email ?: "",
                                            role = loginResponse.data?.role ?: "",
                                            photo = localPhotoPath, // local path
                                            address = loginResponse.data?.address,
                                            latitude = loginResponse.data?.latitude,
                                            longitude = loginResponse.data?.longitude
                                        )
                                        val db = AppDatabase.getDatabase(context)
                                        db.userDao().insertUser(user)

                                        // 3. Save local photo path in SharedPreferences
                                        localPhotoPath?.let {
                                            context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
                                                .edit().putString("user_photo_local", it).apply()
                                        }


                                        Log.d("LOGIN", "Image download URL: http://192.168.8.187/HND_FINAL/Uploads/profile_photos/$photoUrl")

                                        Log.d("LOGIN", "Local photo saved: $localPhotoPath")
                                    }
                                    // Navigate based on role
                                    navigateTo = when (loginResponse.data?.role?.lowercase()) {
                                        "seller" -> "artist_dashboard_screen"
                                        "bidder" -> "user_home"
                                        else -> null
                                    }
                                } else {
                                    // Toast.makeText(context, "Error: ${loginResponse?.message}", Toast.LENGTH_SHORT).show()

                                    // Show error toast
                                    toastMessage = loginResponse?.message ?: "Login failed!"
                                    toastType = ToastType.ERROR
                                    showToastState = true
                                }
                            }

                            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                                //Toast.makeText(context, "API call failed: ${t.message}", Toast.LENGTH_SHORT).show()


                                scope.launch {
                                    delay(3000) // wait 1.5 seconds before resetting
                                    isLoading  = false
                                }
                                // Show error toast
                                toastMessage = "API call failed: ${t.message}"
                                toastType = ToastType.ERROR
                                showToastState = true
                            }
                        })
                }
            )


            Spacer(modifier = Modifier.height(16.dp))

            AppClickableText(
                text = "Forgot Password?",
                color = TextColor,
                onClick = { navController.navigate("forgotPassword") }
            )

            Spacer(modifier = Modifier.height(16.dp))

            AppClickableText(
                text = "Don't have an account? Sign Up",
                color = TextColor,
                onClick = { navController.navigate("signUp") }
            )

            //TEST PAGE CODE
//            Button(onClick = {
//                val testEmail = "testuser@example.com"
//                navController.navigate("otp_screen_signUp/$testEmail")
//            }) {
//                Text("Test SignUp OTP Screen")
//            }
        }
    }
    // Display toast overlay
    if (showToastState) {
        SwipeDismissToast(
            message = toastMessage,
            type = toastType,
            //duration = 6000L,
            onDismiss = { showToastState = false }
        )

    }
    // Navigate after toast disappears
    if (navigateTo != null && !showToastState) {
        LaunchedEffect(navigateTo) {
            delay(1000) // wait 1s for toast
            navController.navigate(navigateTo!!) { popUpTo("login") { inclusive = true } }
            navigateTo = null
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun LoginScreenPreview() {
//    ArtAuctionAppTheme {
//        // For preview, provide a dummy NavController
//        LoginScreen(navController = rememberNavController())
//    }
//}
