package lk.nibm.hdse242ft.artauctionapp.Local_DB

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context

@Database(entities = [User::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao // singletone instance(bind entity and dao)

    companion object {
        @Volatile // represent singletone object as this
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).fallbackToDestructiveMigration() // <- clears old DB and recreates
                 .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
