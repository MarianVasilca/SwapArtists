package app.swapartists.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    primaryKeys = [FavoriteArtist.KEY],
    tableName = "favorite_artists"
)
data class FavoriteArtist(
    @ColumnInfo(name = KEY) val id: String,
    @ColumnInfo(name = "favorite_artist_name") val name: String?,
    @ColumnInfo(name = "favorite_artist_disambiguation") val disambiguation: String?,
) {
    companion object {
        const val KEY = "favorite_artist_id"
    }
}