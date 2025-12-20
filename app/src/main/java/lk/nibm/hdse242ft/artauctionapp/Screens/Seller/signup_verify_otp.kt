package lk.nibm.hdse242ft.artauctionapp.Screens

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import lk.nibm.hdse242ft.artauctionapp.Api.RetrofitInstance
import lk.nibm.hdse242ft.artauctionapp.Reusable_Components.AppButton
import lk.nibm.hdse242ft.artauctionapp.Reusable_Components.AppCard
import lk.nibm.hdse242ft.artauctionapp.Reusable_Components.AppTextField
import lk.nibm.hdse242ft.artauctionapp.Reusable_Components.SwipeDismissToast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import androidx.compose.ui.text.style.TextAlign
import lk.nibm.hdse242ft.artauctionapp.Api.VerifyOtpRequest_signup
import lk.nibm.hdse242ft.artauctionapp.Api.VerifyOtpResponse_signup
import lk.nibm.hdse242ft.artauctionapp.Reusable_Components.ToastType
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.delay
@Composable
fun OTPVerificationScreen_signup(
    navController: NavController,
    id: Int,
    email: String,
    modifier: Modifier = Modifier
) {


    var otp by remember { mutableStateOf("") }
    var toastMessage by remember { mutableStateOf("") }
    var showToastState by remember { mutableStateOf(false) }
    var toastType by remember { mutableStateOf(ToastType.INFO) }
// Add these states at the top
    var navigateToScreen by remember { mutableStateOf<String?>(null) } // holds route to navigate



    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 24.dp, vertical = 32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AppCard {
                // Back button
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.align(Alignment.Start)
                ) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text("Verify OTP", fontSize = 28.sp, color = MaterialTheme.colorScheme.secondary)

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Enter the 6-digit code sent to $email",
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(32.dp))

                AppTextField(value = otp, onValueChange = { otp = it }, label = "OTP Code")
                Spacer(modifier = Modifier.height(16.dp))

                AppButton(
                    text = "Verify OTP",
                    onClick = {


                        val request = VerifyOtpRequest_signup(id = id ,email = email, otp = otp)

                        RetrofitInstance.api.verifyOtpSignup(request)
                            .enqueue(object : Callback<VerifyOtpResponse_signup> {
                                override fun onResponse(
                                    call: Call<VerifyOtpResponse_signup>,
                                    response: Response<VerifyOtpResponse_signup>
                                ) {
                                    if (response.isSuccessful && response.body()?.status == "success") {
                                        toastMessage = response.body()?.message ?: "Signup verified successfully!"
                                        toastType = ToastType.SUCCESS
                                        showToastState = true

                                        // Trigger navigation after toast
                                        navigateToScreen = "login"

                                    }
                                    else if(response.isSuccessful && response.body()?.status == "error_db")
                                    {
                                        toastMessage = response.body()?.message ?: "Signup verified Failed!"
                                        toastType = ToastType.ERROR
                                        showToastState = true

                                        navigateToScreen = "signUp"



                                    }else {
                                        toastMessage = response.body()?.message ?: "OTP verification failed!"
                                        toastType = ToastType.ERROR
                                        showToastState = true
                                    }
                                }

                                override fun onFailure(call: Call<VerifyOtpResponse_signup>, t: Throwable) {
                                    toastMessage = "API call failed: ${t.message}"
                                    toastType = ToastType.ERROR
                                    showToastState = true
                                }
                            })
                    }
                )
            }
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
    // Outside Column, after toast
    if (navigateToScreen != null) {
        LaunchedEffect(navigateToScreen) {
            delay(3000) // wait for toast to show
            navController.navigate(navigateToScreen!!) {
                popUpTo(navigateToScreen!!) { inclusive = true }
            }
            navigateToScreen = null
        }
    }
}
