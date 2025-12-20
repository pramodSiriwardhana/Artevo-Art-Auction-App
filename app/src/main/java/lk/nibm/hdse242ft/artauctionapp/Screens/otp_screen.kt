package lk.nibm.hdse242ft.artauctionapp.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import lk.nibm.hdse242ft.artauctionapp.Api.ForgotPasswordRequest
import lk.nibm.hdse242ft.artauctionapp.Api.RetrofitInstance
import lk.nibm.hdse242ft.artauctionapp.Api.VerifyOtpRequest
import lk.nibm.hdse242ft.artauctionapp.Api.VerifyOtpResponse
import lk.nibm.hdse242ft.artauctionapp.Reusable_Components.AppButton
import lk.nibm.hdse242ft.artauctionapp.Reusable_Components.AppCard
import lk.nibm.hdse242ft.artauctionapp.Reusable_Components.AppTextField
import lk.nibm.hdse242ft.artauctionapp.Reusable_Components.SwipeDismissToast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import kotlinx.coroutines.delay
import lk.nibm.hdse242ft.artauctionapp.Reusable_Components.FPPasswordTextField

@Composable
fun OTPVerificationScreen(navController: NavController, email: String, modifier: Modifier = Modifier) {
    var otp by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    // Toast state
    var toastMessage by remember { mutableStateOf("") }
    var showToastState by remember { mutableStateOf(false) }
    var toastType by remember { mutableStateOf(lk.nibm.hdse242ft.artauctionapp.Reusable_Components.ToastType.INFO) }
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
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.align(Alignment.Start)
                ) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Verify OTP",
                    fontSize = 28.sp,
                    color = MaterialTheme.colorScheme.secondary
                )

                Spacer(modifier = Modifier.height(16.dp))


                Text(
                    text = buildAnnotatedString {
                        append("Enter the 6-digit code sent to ")
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))) {
                            append(email)
                        }
                        append(".")
                    },
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(32.dp))

                AppTextField(
                    value = otp,
                    onValueChange = { otp = it },
                    label = "OTP Code"
                )

                Spacer(modifier = Modifier.height(16.dp))

                FPPasswordTextField(
                    value = newPassword,
                    onValueChange = { newPassword = it },
                    label = "New Password"
                )

                Spacer(modifier = Modifier.height(16.dp))

                FPPasswordTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = "Confirm Password"
                )

                Spacer(modifier = Modifier.height(32.dp))


                var isLoading by remember { mutableStateOf(false) }

                AppButton(
                    text = if (isLoading) "Verifying..." else "Verify OTP",
                    onClick = {
                        if (isLoading) return@AppButton // prevent re-clicks
                        isLoading = true

                            val request = VerifyOtpRequest(email, otp, newPassword, confirmPassword)
                            RetrofitInstance.api.verifyOtp(request)
                                .enqueue(object : Callback<VerifyOtpResponse> {
                                    override fun onResponse(
                                        call: Call<VerifyOtpResponse>,
                                        response: Response<VerifyOtpResponse>
                                    ) {
                                        if (response.isSuccessful && response.body()?.status == "success") {

                                            toastMessage = response.body()?.message ?: "Password reset successful!"
                                            toastType = lk.nibm.hdse242ft.artauctionapp.Reusable_Components.ToastType.SUCCESS
                                            showToastState = true
                                            // Trigger navigation after toast
                                            navigateToScreen = "login"
                                        } else if(response.isSuccessful && response.body()?.status == "errorOtp")
                                        {
                                            toastMessage = response.body()?.message ?: "OTP verification failed!"
                                            toastType = lk.nibm.hdse242ft.artauctionapp.Reusable_Components.ToastType.ERROR
                                            showToastState = true
                                            // Navigate back to Forgot Password screen
                                            // Trigger navigation after toast (just like success)
                                            navigateToScreen = "login"

                                        }else {
                                            toastMessage = response.body()?.message ?: "OTP verification failed!"
                                            toastType = lk.nibm.hdse242ft.artauctionapp.Reusable_Components.ToastType.ERROR
                                            showToastState = true
                                        }
                                    }

                                    override fun onFailure(call: Call<VerifyOtpResponse>, t: Throwable) {
                                        toastMessage = "API call failed: ${t.message}"
                                        toastType = lk.nibm.hdse242ft.artauctionapp.Reusable_Components.ToastType.ERROR
                                        showToastState = true
                                    }
                                })

                    }
                )
            }
        }

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
}
