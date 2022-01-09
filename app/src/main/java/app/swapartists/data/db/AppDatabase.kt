package app.swapartists.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * The Room database for this app
 */
//@Database(
//    entities = [],
//    version = 1,
//    exportSchema = false,
//)
abstract class AppDatabase : RoomDatabase() {

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