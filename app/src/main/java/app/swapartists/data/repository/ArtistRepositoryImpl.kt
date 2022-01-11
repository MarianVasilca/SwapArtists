package app.swapartists.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import app.swapartists.ArtistQuery
import app.swapartists.data.db.FavoriteArtistDao
import app.swapartists.data.model.ArtistDetails
import app.swapartists.data.model.FavoriteArtist
import app.swapartists.data.model.GetArtistsError
import app.swapartists.data.paging.ArtistPagingSource
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.exception.ApolloException
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ArtistRepositoryImpl @Inject constructor(
    private val apiClient: ApolloClient,
    private val favoriteArtistDao: FavoriteArtistDao
) : ArtistRepository {

    override fun getArtistsPaged(
        query: String,
    ) = Pager(
        PagingConfig(
            pageSize = ArtistPagingSource.DEFAULT_PAGE_SIZE,
            prefetchDistance = ArtistPagingSource.DEFAULT_PREFETCH_PAGES,
            enablePlaceholders = false,
            initialLoadSize = ArtistPagingSource.DEFAULT_INITIAL_LOAD_SIZE,
        )
    ) {
        ArtistPagingSource(
            query = query,
            apiClient = apiClient
        )
    }.flow

    override suspend fun getArtist(id: String): ArtistDetails {
        val artistQuery = ArtistQuery(id)
        val errorMessage = "Couldn't fetch artist with id = $id"
        try {
            return apiClient.query(artistQuery).execute()
                .dataAssertNoErrors.node?.artistDetailsFragment
                ?: throw GetArtistsError(errorMessage, null)
        } catch (e: ApolloException) {
            throw GetArtistsError(errorMessage, e)
        }
    }

    override fun getFavoriteArtists(): Flow<List<FavoriteArtist>> = favoriteArtistDao.getItems()
    override suspend fun existsFavoriteArtistByID(id: String): Boolean =
        favoriteArtistDao.existsByID(id)

    override fun getFlowByID(id: String): Flow<FavoriteArtist?> = favoriteArtistDao.getFlowByID(id)
    override suspend fun insertFavoriteArtist(item: FavoriteArtist) = favoriteArtistDao.insert(item)
    override suspend fun deleteFavoriteArtistByID(ids: Collection<String>) =
        favoriteArtistDao.deleteByID(ids)
}