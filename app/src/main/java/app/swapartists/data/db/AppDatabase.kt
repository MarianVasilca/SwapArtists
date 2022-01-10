package app.swapartists.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import app.swapartists.data.model.FavoriteArtist

/**
 * The Room database for this app
 */
@Database(
    entities = [
        FavoriteArtist::class
    ],
    version = 1,
    exportSchema = false,
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun favoriteArtistDao(): FavoriteArtistDao

    companion object {

        private const val DATABASE_NAME = "swapartists-db"

        // For Singleton instantiation
        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room
                .databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
                .build()
        }
    }
}