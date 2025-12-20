package lk.nibm.hdse242ft.artauctionapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.runtime.* // Import for remember, LaunchedEffect, mutableStateOf
import kotlinx.coroutines.delay // Import for delay
import lk.nibm.hdse242ft.artauctionapp.ui.theme.ArtAuctionAppTheme// Import your custom theme
import androidx.navigation.compose.NavHost // Import for NavHost
import androidx.navigation.compose.composable // Import for composable
import androidx.navigation.compose.rememberNavController // Import for rememberNavController
import androidx.navigation.navArgument
import lk.nibm.hdse242ft.artauctionapp.Screens.AppStartScreen
import lk.nibm.hdse242ft.artauctionapp.Screens.ArtistDashboardScreen
import lk.nibm.hdse242ft.artauctionapp.Screens.Bidder.UserHomeScreen
import lk.nibm.hdse242ft.artauctionapp.Screens.LoginScreen // Import LoginScreen
import lk.nibm.hdse242ft.artauctionapp.Screens.ForgotPasswordScreen // Import ForgotPasswordScreen
import lk.nibm.hdse242ft.artauctionapp.Screens.MyArtScreen
import lk.nibm.hdse242ft.artauctionapp.Screens.OTPVerificationScreen
import lk.nibm.hdse242ft.artauctionapp.Screens.OTPVerificationScreen_signup
import lk.nibm.hdse242ft.artauctionapp.Screens.Seller.EditArtworkScreen
import lk.nibm.hdse242ft.artauctionapp.Screens.Seller.ListNewArtworkScreen
import lk.nibm.hdse242ft.artauctionapp.Screens.Seller.ProfileUpdateScreen
import lk.nibm.hdse242ft.artauctionapp.Screens.SignUpScreen
import lk.nibm.hdse242ft.artauctionapp.Screens.SplashScreen
import lk.nibm.hdse242ft.artauctionapp.Test.ModernUserListScreen


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ArtAuctionAppTheme { // Apply your custom theme to the entire app
                val navController = rememberNavController() // Create a NavController

                // State to control whether the splash screen is shown
                var showSplashScreen by remember { mutableStateOf(true) }

                // LaunchedEffect runs a suspend function once when the composable enters the composition
                LaunchedEffect(key1 = true) {
                    delay(3000L) // Display splash screen for 3 seconds
                    showSplashScreen = false // Hide splash screen after delay
                }

                Surface(
                    modifier = Modifier.fillMaxSize(), // Fills the entire screen
                    color = MaterialTheme.colorScheme.background // Uses the background color defined in your theme
                ) {
//                    if (showSplashScreen) {
//                        SplashScreen() // Show the splash screen
//                    } else {
                        // After the splash screen, set up the navigation host
                      //  NavHost(navController = navController, startDestination = "login") {
                    NavHost(navController = navController, startDestination = "app_start") {

                        // 1️⃣ App start screen (splash + DB check)
                        composable("app_start") {
                            AppStartScreen(navController = navController)
                        }

                        composable("login") {
                            LoginScreen(navController = navController) // Pass navController to LoginScreen
                        }
                        composable("user_list") { ModernUserListScreen() } // ✅ add this user list local db

                        composable("forgotPassword") {
                            ForgotPasswordScreen(navController = navController) // Pass navController  ListNewArtworkScreen OTPVerificationScreen
                        }
                        composable("signUp") {
                            SignUpScreen(navController = navController) // Pass navController SignUpScreen
                        }
                        composable("list_new_auction_screen") {
                            ListNewArtworkScreen(navController = navController) // Pass navController
                        }
                        composable("artist_dashboard_screen") {
                            ArtistDashboardScreen(navController = navController) // Pass navController
                        }
                        composable("my_art_screen") {
                            MyArtScreen(navController = navController) // Pass navController
                        }                            // Corrected route to accept the artworkId parameter
                        composable("edit_artwork_screen/{artworkId}") { backStackEntry ->
                            // Extract the artworkId from the navigation arguments
                            val artworkId = backStackEntry.arguments?.getString("artworkId") ?: "1"
                            EditArtworkScreen(navController = navController, artworkId = artworkId)
                        }

                        composable("user_home") {
                            UserHomeScreen(navController = navController)
                        }
                        composable("profile_screen") {
                            // Replace with your ProfileUpdateScreen
                            ProfileUpdateScreen(

                                navController = navController,

                                onUpdateProfile = { fullName, email, location, imageUri ->
                                    // Handle profile update
                                },
                                onChangePassword = { password, confirmPassword ->
                                    // Handle password change
                                }
                            )

                        }
                        // navigate otp verification screen(forget password)
                        composable(
                            route = "otp_screen/{email}",
                            arguments = listOf(navArgument("email") { defaultValue = "" })
                        ) { backStackEntry ->
                            val email = backStackEntry.arguments?.getString("email") ?: ""
                            OTPVerificationScreen(navController = navController, email = email)
                        }

                        // navigate SIGN UP otp verification screen
                        composable("otp_screen_signUp/{userId}/{email}") { backStackEntry ->
                            val id =
                                backStackEntry.arguments?.getString("userId")?.toIntOrNull() ?: 0
                            val email = backStackEntry.arguments?.getString("email") ?: ""
                            OTPVerificationScreen_signup(navController, id, email)
                        }

                        // You will add more composable routes for other screens here

                    }

                    //}

                    //}
                }
            }
        }
    }
}
