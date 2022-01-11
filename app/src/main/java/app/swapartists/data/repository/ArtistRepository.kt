package app.swapartists.data.repository

import androidx.paging.PagingData
import app.swapartists.data.model.ArtistDetails
import app.swapartists.data.model.ArtistNode
import app.swapartists.data.model.FavoriteArtist
import kotlinx.coroutines.flow.Flow

interface ArtistRepository {

    fun getArtistsPaged(
        query: String
    ): Flow<PagingData<ArtistNode>>

    suspend fun getArtist(
        id: String
    ): ArtistDetails

    fun getFavoriteArtists(): Flow<List<FavoriteArtist>>
    suspend fun existsFavoriteArtistByID(id: String): Boolean
    fun getFlowByID(id: String): Flow<FavoriteArtist?>
    suspend fun insertFavoriteArtist(item: FavoriteArtist)
    suspend fun deleteFavoriteArtistByID(ids: Collection<String>)
}