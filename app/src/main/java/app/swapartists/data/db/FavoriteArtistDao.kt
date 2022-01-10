package app.swapartists.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import app.swapartists.data.model.FavoriteArtist
import kotlinx.coroutines.flow.Flow

/**
 * The Data Access Object for the [FavoriteArtist] class.
 */
@Dao
interface FavoriteArtistDao {
    @Query("SELECT * FROM favorite_artists ORDER BY favorite_artist_name")
    fun getItems(): Flow<List<FavoriteArtist>>

    @Query("SELECT * FROM favorite_artists WHERE favorite_artist_id = :id")
    suspend fun getByID(id: String): FavoriteArtist?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: FavoriteArtist)

    @Query(" DELETE FROM favorite_artists WHERE favorite_artist_id in (:ids)")
    suspend fun deleteByID(ids: Collection<String>)
}