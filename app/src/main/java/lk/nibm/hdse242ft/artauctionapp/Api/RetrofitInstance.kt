package lk.nibm.hdse242ft.artauctionapp.Api

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val BASE_URL = "http://192.168.8.187/HND_FINAL/Mobile/"

    private val gson = GsonBuilder()
        .setLenient() // ← make Gson lenient
        .create()

    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson)) // use lenient Gson  jason object convert in to kotlin  object
            .build()
            .create(ApiService::class.java)
    }
}
