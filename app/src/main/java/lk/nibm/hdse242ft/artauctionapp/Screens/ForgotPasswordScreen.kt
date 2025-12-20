package lk.nibm.hdse242ft.artauctionapp.Screens

import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import lk.nibm.hdse242ft.artauctionapp.Api.ForgotPasswordRequest
import lk.nibm.hdse242ft.artauctionapp.Api.ForgotPasswordResponse
import lk.nibm.hdse242ft.artauctionapp.Api.RetrofitInstance
import lk.nibm.hdse242ft.artauctionapp.Reusable_Components.FPButton
import lk.nibm.hdse242ft.artauctionapp.Reusable_Components.FPCard
import lk.nibm.hdse242ft.artauctionapp.Reusable_Components.SwipeDismissToast
import lk.nibm.hdse242ft.artauctionapp.Reusable_Components.ToastType
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import lk.nibm.hdse242ft.artauctionapp.Reusable_Components.FPTextField
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordScreen(navController: NavController, modifier: Modifier = Modifier) {

    val context = LocalContext.current

    // Load user info from SharedPreferences
    val sharedPref = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    val userEmailPref = sharedPref.getString("user_email", "") ?: ""
    val userid = sharedPref.getInt("user_id", 0)

    var email by remember { mutableStateOf(userEmailPref) }
    var isLoading by remember { mutableStateOf(false) }
    // Toast state
    var toastMessage by remember { mutableStateOf("") }
    var toastType by remember { mutableStateOf(ToastType.INFO) }
    var showToastState by remember { mutableStateOf(false) }
    var navigateToOtp by remember { mutableStateOf(false) }


    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 24.dp, vertical = 32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        FPCard {
            // Back button
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.align(Alignment.Start)
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Forgot Password?",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Enter your email to reset your password.",
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                fontSize = 16.sp,
                modifier = Modifier.fillMaxWidth(),   // Make text take full width
                textAlign = TextAlign.Center          // Center the text horizontally
            )

            Spacer(modifier = Modifier.height(32.dp))

            FPTextField(
                value = email,
                onValueChange = { email = it },
                label = "Email",
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = "Email Icon") },
                enabled = !isLoading
            )

            Spacer(modifier = Modifier.height(32.dp))

            FPButton(
                text = if (isLoading) "Sending..." else "Send OTP",
                onClick = {
                    if (email.isNotEmpty()) {
                        isLoading = true

                        // ✅ Debug logs
                        Log.d("DEBUG_OTP", "Email: $email")//, UserId: $userid

//                        if (userid == 0) {
//                            toastMessage = "User not logged in"
//                            toastType = ToastType.ERROR
//                            showToastState = true
//                            Log.d("DEBUG_OTP", "UserId is 0, cannot send OTP")
//                            return@FPButton
//                        }

                        val request = ForgotPasswordRequest(email)//, userid

                        RetrofitInstance.api.sendOtp(request).enqueue(object :
                            Callback<ForgotPasswordResponse> {
                            override fun onResponse(
                                call: Call<ForgotPasswordResponse>,
                                response: Response<ForgotPasswordResponse>
                            ) {
                                isLoading = false
                                if (response.isSuccessful && response.body()?.status == "success") {
                                    toastMessage = response.body()?.message ?: "OTP sent successfully!"
                                    toastType = ToastType.SUCCESS
                                    showToastState = true
//                                    navController.navigate("otp_screen/$email")
                                    // ✅ Only trigger LaunchedEffect, don't navigate immediately
                                    navigateToOtp = true

                                } else {
                                    toastMessage = response.body()?.message ?: "Failed to send OTP!"
                                    toastType = ToastType.ERROR
                                    showToastState = true
                                }
                            }

                            override fun onFailure(call: Call<ForgotPasswordResponse>, t: Throwable) {
                                isLoading = false
                                toastMessage = "Failed to send OTP: ${t.message}"
                                toastType = ToastType.ERROR
                                showToastState = true
                            }
                        })
                    } else {
                        toastMessage = "Please enter your email"
                        toastType = ToastType.ERROR
                        showToastState = true
                    }
                },
                enabled = !isLoading
            )
        }
    }

    // Show toast
    if (showToastState) {
        SwipeDismissToast(
            message = toastMessage,
            type = toastType,
            onDismiss = { showToastState = false }
        )
    }

    // Delay navigation until toast is visible
    if (navigateToOtp) {
        LaunchedEffect(navigateToOtp) {
            delay(3000) // wait 3 seconds for toast
            navController.navigate("otp_screen/$email") {
                popUpTo("otp_screen/{email}") { inclusive = true }
            }
            navigateToOtp = false
        }
    }
}