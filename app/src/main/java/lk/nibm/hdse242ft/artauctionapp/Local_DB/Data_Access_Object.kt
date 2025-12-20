package lk.nibm.hdse242ft.artauctionapp.Local_DB

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Query("SELECT * FROM user LIMIT 1")
    suspend fun getUser(): User?


    @Query("SELECT * FROM User LIMIT 1")
    fun getUserFlow(): Flow<User?>   // Flow automatically emits updates


    @Query("DELETE FROM user")
    suspend fun clearUser()

    // Delete a specific user by ID
    @Query("DELETE FROM user WHERE id = :userId")
    suspend fun deleteUserById(userId: Int)

    //Add this function to fetch all users
    @Query("SELECT * FROM user")
    suspend fun getAllUsers(): List<User>   //Flow<List<User>> when change data it automatically change

    @Update
    suspend fun updateUser(user: User)

    // Or update by user_unique_id
    @Query("""
        UPDATE user SET
            name = :name,
            email = :email,
            address = :address,
            latitude = :latitude,
            longitude = :longitude
        WHERE user_unique_id = :userUniqueId
    """)
    suspend fun updateUserByUniqueId(
        userUniqueId: String,
        name: String,
        email: String,
        address: String,
        latitude: Double?,
        longitude: Double?
    )
}
