package lk.nibm.hdse242ft.artauctionapp.Local_DB

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User(
    @PrimaryKey val id: Int,
    val user_unique_id: String,   // ✅ add unique ID
    val name: String,
    val email: String,
    val role: String,
    val photo: String?,
    val address: String?,
    val latitude: Double?,
    val longitude: Double?,
)
