package lk.nibm.hdse242ft.artauctionapp.Api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query


data class SignupRequest(
    val name: String,
    val email: String,
    val password: String,
    val confirm_password: String,  // 👈 new field
    val address: String,
    val latitude: Double?,
    val longitude: Double?,
    val role: String,
    val agree_terms: Int,
    val photo: String
)

data class SignupResponse(
    val status: String,
    val message: String,
    val user_id: Int? = null
)

data class LoginRequest(
    val email: String,
    val password: String
)

data class LoginResponse(
    val status: String,
    val message: String,
    val data: UserData?
)

data class UserData(
    val id: Int,
    val user_unique_id: String,  // <- add this
    val name: String,
    val email: String,
    val password: String?,
    val address: String?,
    val latitude: Double?,
    val longitude: Double?,
    val role: String,
    val photo: String?,
    val status: String?,
    val agree_terms: Int?,
    val created_at: String?
)

// === Request & Response for listing artwork ===
data class ListArtworkRequest(
    val title: String,
    val description: String,
    val publish_by: String, // ✅ Add this
    val category: String,
    val artist: String,
    val width_cm: Int,
    val height_cm: Int,
    val year_created: Int,
    val medium: String,
    val style: String,
    val location: String,
    val image_path: String,   // base64 or uploaded path
    val auction_start: String, // "YYYY-MM-DD HH:MM"
    val auction_end: String,
    val starting_price: Double,
    val buy_now_price: Double?,
    val predicted_price: Double?
)

data class ListArtworkResponse(
    val status: String,
    val message: String,
    val id: Int? // Inserted artwork ID
)

//forget password
// Request to send OTP
data class ForgotPasswordRequest(
    val email: String,
//    val userId: Int
)

// Response after sending OTP
data class ForgotPasswordResponse(
    val status: String,
    val message: String
)

// Request to verify OTP and reset password
data class VerifyOtpRequest(
    val email: String,
    val otp: String,
    val new_password: String,
    val confirm_password: String
)

// Response after verifying OTP
data class VerifyOtpResponse(
    val status: String,
    val message: String
)


// Request to verify OTP and Signup
data class VerifyOtpRequest_signup(
    val id: Int?,
    val email: String,
    val otp: String,

)

// Response after verifying OTP Signup
data class VerifyOtpResponse_signup(
    val status: String,
    val message: String,

)

//Fetch Artwork
data class Artwork(
    val id: Int,
    val title: String,
    val description: String?,
    val image_path: String?,
    val category: String?,
    val artist: String?,
    val publish_by: String?,
    val width_cm: Int?,
    val height_cm: Int?,
    val year_created: Int?,
    val medium: String?,
    val style: String?,
    val location: String?,
    val starting_price: Double?,
    val buy_now_price: Double?,
    val auction_start: String?,
    val auction_end: String?,
    val status: String?
)


data class ArtworkResponse(
    val status: String,
    val message: String?,
    val data: List<Artwork>?
)

// Fetch live artworks Response
data class Live_ArtworkResponse(
    val status: String,
    val message: String?,
    val data: List<Artwork>?
)
// Request for updating profile
data class UpdateProfileRequest(
    val user_id: Int,
    val name: String,
    val email: String,
    val address: String,
    val latitude: Double?,
    val longitude: Double?
)

// Response for updating profile
data class UpdateProfileResponse(
    val status: String,
    val message: String
)
// Request for updating profile Image
data class UpdateProfileImageRequest(
    val user_id: Int,
    val photo: String // filename or base64
)
// Response for updating profile Image
data class UpdateProfileImageResponse(
    val status: String,
    val message: String
)
// Request for Change Password Request
data class ChangePasswordRequest(
    val user_id: Int,
    val current_password: String,
    val new_password: String,
    val confirm_password: String
)
// Response for Change Password Request
data class ChangePasswordResponse(
    val status: String,
    val message: String
)

interface ApiService {
    @POST("sign_up.php")
    fun signup(@Body request: SignupRequest): Call<SignupResponse>

    @POST("login.php")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    // ✅ Add new API for artwork listing
    @POST("list_new_artwork.php")
    fun listArtwork(@Body request: ListArtworkRequest): Call<ListArtworkResponse>

    // Send OTP for forgot password
    @POST("send_otp.php") // ← your PHP backend endpoint for sending OTP
    fun sendOtp(@Body request: ForgotPasswordRequest): Call<ForgotPasswordResponse>

    // Verify OTP and reset password
    @POST("verify_otp.php")
    fun verifyOtp(@Body request: VerifyOtpRequest): Call<VerifyOtpResponse>

    //signUp verify otp
    @POST("verify_otp_signup.php")
    fun verifyOtpSignup(@Body request: VerifyOtpRequest_signup): Call<VerifyOtpResponse_signup>

    // Fetch all artworks uploaded by a specific user
    @GET("get_user_artworks.php")
    fun getUserArtworks(@Query("user_id") userId: Int): Call<ArtworkResponse>

    // Fetch live artworks for the artist dashboard
    @GET("get_live_artworks.php")
    fun getLiveArtworks(@Query("user_id") userId: Int): Call<Live_ArtworkResponse>

    //updating profile
    @POST("update_profile.php") // <- your new backend endpoint
    fun updateProfile(@Body request: UpdateProfileRequest): Call<UpdateProfileResponse>

    //updating profile Image
    @POST("update_profile_image.php")
    fun updateProfileImage(@Body request: UpdateProfileImageRequest): Call<UpdateProfileImageResponse>

    //Change Password
    @POST("change_password.php")
    fun changePassword(@Body request: ChangePasswordRequest): Call<ChangePasswordResponse>
}
