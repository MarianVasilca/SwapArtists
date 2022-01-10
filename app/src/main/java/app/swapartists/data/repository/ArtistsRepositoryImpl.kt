package app.swapartists.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import app.swapartists.data.paging.ArtistPagingSource
import com.apollographql.apollo3.ApolloClient
import javax.inject.Inject

class ArtistsRepositoryImpl @Inject constructor(
    private val apiClient: ApolloClient
) : ArtistsRepository {

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
}