package lk.nibm.hdse242ft.artauctionapp.Api;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000|\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\bf\u0018\u00002\u00020\u0001J\u0018\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\b\b\u0001\u0010\u0005\u001a\u00020\u0006H\'J\u0018\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\b0\u00032\b\b\u0001\u0010\t\u001a\u00020\nH\'J\u0018\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\f0\u00032\b\b\u0001\u0010\t\u001a\u00020\nH\'J\u0018\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u000e0\u00032\b\b\u0001\u0010\u0005\u001a\u00020\u000fH\'J\u0018\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00110\u00032\b\b\u0001\u0010\u0005\u001a\u00020\u0012H\'J\u0018\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00140\u00032\b\b\u0001\u0010\u0005\u001a\u00020\u0015H\'J\u0018\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00170\u00032\b\b\u0001\u0010\u0005\u001a\u00020\u0018H\'J\u0018\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u001a0\u00032\b\b\u0001\u0010\u0005\u001a\u00020\u001bH\'J\u0018\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\u001d0\u00032\b\b\u0001\u0010\u0005\u001a\u00020\u001eH\'J\u0018\u0010\u001f\u001a\b\u0012\u0004\u0012\u00020 0\u00032\b\b\u0001\u0010\u0005\u001a\u00020!H\'J\u0018\u0010\"\u001a\b\u0012\u0004\u0012\u00020#0\u00032\b\b\u0001\u0010\u0005\u001a\u00020$H\'\u00a8\u0006%"}, d2 = {"Llk/nibm/hdse242ft/artauctionapp/Api/ApiService;", "", "changePassword", "Lretrofit2/Call;", "Llk/nibm/hdse242ft/artauctionapp/Api/ChangePasswordResponse;", "request", "Llk/nibm/hdse242ft/artauctionapp/Api/ChangePasswordRequest;", "getLiveArtworks", "Llk/nibm/hdse242ft/artauctionapp/Api/Live_ArtworkResponse;", "userId", "", "getUserArtworks", "Llk/nibm/hdse242ft/artauctionapp/Api/ArtworkResponse;", "listArtwork", "Llk/nibm/hdse242ft/artauctionapp/Api/ListArtworkResponse;", "Llk/nibm/hdse242ft/artauctionapp/Api/ListArtworkRequest;", "login", "Llk/nibm/hdse242ft/artauctionapp/Api/LoginResponse;", "Llk/nibm/hdse242ft/artauctionapp/Api/LoginRequest;", "sendOtp", "Llk/nibm/hdse242ft/artauctionapp/Api/ForgotPasswordResponse;", "Llk/nibm/hdse242ft/artauctionapp/Api/ForgotPasswordRequest;", "signup", "Llk/nibm/hdse242ft/artauctionapp/Api/SignupResponse;", "Llk/nibm/hdse242ft/artauctionapp/Api/SignupRequest;", "updateProfile", "Llk/nibm/hdse242ft/artauctionapp/Api/UpdateProfileResponse;", "Llk/nibm/hdse242ft/artauctionapp/Api/UpdateProfileRequest;", "updateProfileImage", "Llk/nibm/hdse242ft/artauctionapp/Api/UpdateProfileImageResponse;", "Llk/nibm/hdse242ft/artauctionapp/Api/UpdateProfileImageRequest;", "verifyOtp", "Llk/nibm/hdse242ft/artauctionapp/Api/VerifyOtpResponse;", "Llk/nibm/hdse242ft/artauctionapp/Api/VerifyOtpRequest;", "verifyOtpSignup", "Llk/nibm/hdse242ft/artauctionapp/Api/VerifyOtpResponse_signup;", "Llk/nibm/hdse242ft/artauctionapp/Api/VerifyOtpRequest_signup;", "app_debug"})
public abstract interface ApiService {
    
    @retrofit2.http.POST(value = "sign_up.php")
    @org.jetbrains.annotations.NotNull()
    public abstract retrofit2.Call<lk.nibm.hdse242ft.artauctionapp.Api.SignupResponse> signup(@retrofit2.http.Body()
    @org.jetbrains.annotations.NotNull()
    lk.nibm.hdse242ft.artauctionapp.Api.SignupRequest request);
    
    @retrofit2.http.POST(value = "login.php")
    @org.jetbrains.annotations.NotNull()
    public abstract retrofit2.Call<lk.nibm.hdse242ft.artauctionapp.Api.LoginResponse> login(@retrofit2.http.Body()
    @org.jetbrains.annotations.NotNull()
    lk.nibm.hdse242ft.artauctionapp.Api.LoginRequest request);
    
    @retrofit2.http.POST(value = "list_new_artwork.php")
    @org.jetbrains.annotations.NotNull()
    public abstract retrofit2.Call<lk.nibm.hdse242ft.artauctionapp.Api.ListArtworkResponse> listArtwork(@retrofit2.http.Body()
    @org.jetbrains.annotations.NotNull()
    lk.nibm.hdse242ft.artauctionapp.Api.ListArtworkRequest request);
    
    @retrofit2.http.POST(value = "send_otp.php")
    @org.jetbrains.annotations.NotNull()
    public abstract retrofit2.Call<lk.nibm.hdse242ft.artauctionapp.Api.ForgotPasswordResponse> sendOtp(@retrofit2.http.Body()
    @org.jetbrains.annotations.NotNull()
    lk.nibm.hdse242ft.artauctionapp.Api.ForgotPasswordRequest request);
    
    @retrofit2.http.POST(value = "verify_otp.php")
    @org.jetbrains.annotations.NotNull()
    public abstract retrofit2.Call<lk.nibm.hdse242ft.artauctionapp.Api.VerifyOtpResponse> verifyOtp(@retrofit2.http.Body()
    @org.jetbrains.annotations.NotNull()
    lk.nibm.hdse242ft.artauctionapp.Api.VerifyOtpRequest request);
    
    @retrofit2.http.POST(value = "verify_otp_signup.php")
    @org.jetbrains.annotations.NotNull()
    public abstract retrofit2.Call<lk.nibm.hdse242ft.artauctionapp.Api.VerifyOtpResponse_signup> verifyOtpSignup(@retrofit2.http.Body()
    @org.jetbrains.annotations.NotNull()
    lk.nibm.hdse242ft.artauctionapp.Api.VerifyOtpRequest_signup request);
    
    @retrofit2.http.GET(value = "get_user_artworks.php")
    @org.jetbrains.annotations.NotNull()
    public abstract retrofit2.Call<lk.nibm.hdse242ft.artauctionapp.Api.ArtworkResponse> getUserArtworks(@retrofit2.http.Query(value = "user_id")
    int userId);
    
    @retrofit2.http.GET(value = "get_live_artworks.php")
    @org.jetbrains.annotations.NotNull()
    public abstract retrofit2.Call<lk.nibm.hdse242ft.artauctionapp.Api.Live_ArtworkResponse> getLiveArtworks(@retrofit2.http.Query(value = "user_id")
    int userId);
    
    @retrofit2.http.POST(value = "update_profile.php")
    @org.jetbrains.annotations.NotNull()
    public abstract retrofit2.Call<lk.nibm.hdse242ft.artauctionapp.Api.UpdateProfileResponse> updateProfile(@retrofit2.http.Body()
    @org.jetbrains.annotations.NotNull()
    lk.nibm.hdse242ft.artauctionapp.Api.UpdateProfileRequest request);
    
    @retrofit2.http.POST(value = "update_profile_image.php")
    @org.jetbrains.annotations.NotNull()
    public abstract retrofit2.Call<lk.nibm.hdse242ft.artauctionapp.Api.UpdateProfileImageResponse> updateProfileImage(@retrofit2.http.Body()
    @org.jetbrains.annotations.NotNull()
    lk.nibm.hdse242ft.artauctionapp.Api.UpdateProfileImageRequest request);
    
    @retrofit2.http.POST(value = "change_password.php")
    @org.jetbrains.annotations.NotNull()
    public abstract retrofit2.Call<lk.nibm.hdse242ft.artauctionapp.Api.ChangePasswordResponse> changePassword(@retrofit2.http.Body()
    @org.jetbrains.annotations.NotNull()
    lk.nibm.hdse242ft.artauctionapp.Api.ChangePasswordRequest request);
}