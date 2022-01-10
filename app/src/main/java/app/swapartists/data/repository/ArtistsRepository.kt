package app.swapartists.data.repository

import androidx.paging.PagingData
import app.swapartists.data.model.ArtistDetails
import app.swapartists.data.model.ArtistNode
import kotlinx.coroutines.flow.Flow

interface ArtistsRepository {

    fun getArtistsPaged(
        query: String
    ): Flow<PagingData<ArtistNode>>

    suspend fun getArtist(
        id: String
    ): ArtistDetails
}