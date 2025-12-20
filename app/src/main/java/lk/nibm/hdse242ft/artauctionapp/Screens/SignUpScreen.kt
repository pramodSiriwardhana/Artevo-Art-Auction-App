package lk.nibm.hdse242ft.artauctionapp.Screens

import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import lk.nibm.hdse242ft.artauctionapp.Api.RetrofitInstance
import lk.nibm.hdse242ft.artauctionapp.Api.SignupRequest
import lk.nibm.hdse242ft.artauctionapp.Api.SignupResponse
import lk.nibm.hdse242ft.artauctionapp.Reusable_Components.AppButton
import lk.nibm.hdse242ft.artauctionapp.Reusable_Components.AppCard
import lk.nibm.hdse242ft.artauctionapp.Reusable_Components.AppClickableText
import lk.nibm.hdse242ft.artauctionapp.Reusable_Components.AppTextField
import lk.nibm.hdse242ft.artauctionapp.Reusable_Components.CheckboxWithClickableText
import lk.nibm.hdse242ft.artauctionapp.Reusable_Components.LocationPermissionButton
import lk.nibm.hdse242ft.artauctionapp.Reusable_Components.ProfilePhotoPicker
import lk.nibm.hdse242ft.artauctionapp.Reusable_Components.RoleDropdown
import lk.nibm.hdse242ft.artauctionapp.ui.theme.HeadingColor
import lk.nibm.hdse242ft.artauctionapp.R
import lk.nibm.hdse242ft.artauctionapp.Reusable_Components.FPButton
import lk.nibm.hdse242ft.artauctionapp.Reusable_Components.SwipeDismissToast
import lk.nibm.hdse242ft.artauctionapp.Reusable_Components.ToastType
import lk.nibm.hdse242ft.artauctionapp.Reusable_Components.uriToBase64
import retrofit2.Call
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(navController: NavController, modifier: Modifier = Modifier) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var selectedRole by remember { mutableStateOf("Select Role") }

    var profilePhotoUri by remember { mutableStateOf<Uri?>(null) }
    var passwordVisibility by remember { mutableStateOf(false) }
    var confirmPasswordVisibility by remember { mutableStateOf(false) }
    var agreeToTerms by remember { mutableStateOf(false) }

    var location by remember { mutableStateOf("") }
    val context = LocalContext.current

    var expanded by remember { mutableStateOf(false) }
    val roles = listOf("bidder", "seller")

    val scrollState = androidx.compose.foundation.rememberScrollState()
    var userId by remember { mutableStateOf(0) } // Add this at the top with your other states

    // Toast state
    var toastMessage by remember { mutableStateOf("") }
    var toastType by remember { mutableStateOf(ToastType.INFO) }
    var showToastState by remember { mutableStateOf(false) }
    var navigateToOtp by remember { mutableStateOf(false) }

    var isLoading by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 24.dp, vertical = 32.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally

    ) {
        AppCard {
            Text(
                text = "Create Account",
                fontSize = 32.sp,
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                color = HeadingColor,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center // ✅ Center this text only
            )
            Spacer(modifier = Modifier.height(24.dp))

            val context = LocalContext.current
            var profilePhotoUri by remember { mutableStateOf<Uri?>(null) }

            ProfilePhotoPicker(
                context = context,
                imageUri = profilePhotoUri,
                placeholder = painterResource(id = R.drawable.default_profile),
                onImageSelected = { uri -> profilePhotoUri = uri }
            )
            Spacer(modifier = Modifier.height(16.dp))

            AppTextField(value = name, onValueChange = { name = it }, label = "Full Name")
            AppTextField(value = email, onValueChange = { email = it }, label = "Email")
            AppTextField(value = password, onValueChange = { password = it }, label = "Password", isPassword = true)
            AppTextField(value = confirmPassword, onValueChange = { confirmPassword = it }, label = "Confirm Password", isPassword = true)

            var latitude by remember { mutableStateOf(0.0) }
            var longitude by remember { mutableStateOf(0.0) }

            LocationPermissionButton(context, onLocationReceived = { addr, lat, lon ->
                location = addr
                latitude = lat
                longitude = lon
            }) { onClick ->
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



            Spacer(modifier = Modifier.height(16.dp))

            RoleDropdown(
                selectedRole = selectedRole,
                roles = roles,
                expanded = expanded,
                onExpandedChange = { expanded = it },
                onRoleSelected = { selectedRole = it }
            )

            Spacer(modifier = Modifier.height(16.dp))

            CheckboxWithClickableText(
                checked = agreeToTerms,
                onCheckedChange = { agreeToTerms = it },
                text = "I agree to Privacy Policy and Auction Rules & Regulations.",
                clickableSpans = listOf(
                    Triple("Privacy Policy", Color(0xFF4FF7AC)) {
                        // Navigate to Privacy Policy screen or open URL
                        println("Privacy Policy clicked")
                    },
                    Triple("Auction Rules & Regulations", Color(0xFF4FF7AC)) {
                        // Navigate to Rules screen or open URL
                        println("Auction Rules clicked")
                    }
                ),
                defaultColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
            )


            Spacer(modifier = Modifier.height(24.dp))

            FPButton(
                text = if (isLoading) "Signing Up..." else "Sign Up",
                enabled = !isLoading,
                onClick = {
                    isLoading = true  // immediately show loading

                    val photoBase64 = profilePhotoUri?.let { uriToBase64(context, it) } ?: ""
                    val request = SignupRequest(
                        name = name,
                        email = email,
                        password = password,
                        confirm_password = confirmPassword,
                        address = location,
                        latitude = latitude,
                        longitude = longitude,
                        role = selectedRole,
                        agree_terms = if (agreeToTerms) 1 else 0,
                        photo = photoBase64
                    )

                    RetrofitInstance.api.signup(request).enqueue(object : retrofit2.Callback<SignupResponse> {
                        override fun onResponse(call: Call<SignupResponse>, response: retrofit2.Response<SignupResponse>) {
                            isLoading = false // hide loading immediately

                            if (response.isSuccessful && response.body()?.status == "success") {
                                // Inside onResponse when signup is successful
                                userId = response.body()?.user_id ?: 0  // update outer state

                                toastMessage = response.body()?.message ?: "Signup successful!"
                                toastType = ToastType.SUCCESS
                                showToastState = true


                                // Trigger navigation after toast
                                navigateToOtp = true

                            } else {
                                toastMessage = response.body()?.message ?: "Signup failed!"
                                toastType = ToastType.ERROR
                                showToastState = true
                            }
                        }

                        override fun onFailure(call: Call<SignupResponse>, t: Throwable) {
                            isLoading = false
                            toastMessage = "API call failed: ${t.message}"
                            toastType = ToastType.ERROR
                            showToastState = true
                        }
                    })
                }
            )




            Spacer(modifier = Modifier.height(16.dp))

            AppClickableText(
                text = "Already have an account? Login",
                color = MaterialTheme.colorScheme.onSurface,
                onClick = {
                    navController.navigate("login") {
                        popUpTo("signUp") { inclusive = true }
                    }
                }
            )

        }
    }

    // Display toast overlay
    if (showToastState) {
        SwipeDismissToast(
            message = toastMessage,
            type = toastType,
            //duration = 6000L,
            onDismiss = { showToastState = false }
        ) }


    // Delay navigation until toast is visible
    if (navigateToOtp) {
        LaunchedEffect(navigateToOtp) {
            delay(3000) // wait for toast duration
            navController.navigate("otp_screen_signUp/${userId}/$email") {
                popUpTo("otp_screen_signUp/{userId}/{email}") { inclusive = true }
            }
            navigateToOtp = false
        }
}
}



