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

    fun getItems(): Flow<List<FavoriteArtist>>
    suspend fun getByID(id: String): FavoriteArtist?
    fun getFlowByID(id: String): Flow<FavoriteArtist?>
    suspend fun insert(item: FavoriteArtist)
    suspend fun deleteByID(ids: Collection<String>)
}