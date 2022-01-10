package app.swapartists.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import app.swapartists.ArtistsQuery
import app.swapartists.data.model.ArtistNode
import app.swapartists.data.model.GetArtistsError
import app.swapartists.data.model.NoNetworkException
import app.swapartists.data.model.ServerUnreachableException
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Optional
import com.apollographql.apollo3.exception.ApolloException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * A [PagingSource] that uses the before/after keys returned in page requests.
 */
class ArtistPagingSource(
    private val query: String,
    private val apiClient: ApolloClient
) : PagingSource<String, ArtistNode>() {

    override suspend fun load(
        params: LoadParams<String>
    ): LoadResult<String, ArtistNode> {
        return try {
            val artistsQuery = ArtistsQuery(
                query,
                params.loadSize,
                Optional.presentIfNotNull(params.key)
            )

            val data = apiClient.query(artistsQuery)
                .execute()
                .dataAssertNoErrors

            val pageInfo = data.search?.artists?.pageInfo
            val pageNodes = data.search?.artists?.nodes?.filterNotNull() ?: emptyList()

            LoadResult.Page(
                data = pageNodes,
                prevKey = null,
                nextKey = pageInfo?.endCursor
            )
        } catch (e: SocketTimeoutException) {
            LoadResult.Error(NoNetworkException(e))
        } catch (e: UnknownHostException) {
            LoadResult.Error(ServerUnreachableException(e))
        } catch (e: ApolloException) {
            LoadResult.Error(
                GetArtistsError("Couldn't fetch artists for query = $query", e)
            )
        } catch (e: Throwable) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<String, ArtistNode>): String {
        return DEFAULT_REFRESH_KEY
    }

    companion object {
        const val DEFAULT_REFRESH_KEY = "Queen"
        const val DEFAULT_PAGE_SIZE = 15
        const val DEFAULT_PREFETCH_PAGES = 1
        const val DEFAULT_INITIAL_LOAD_SIZE = DEFAULT_PAGE_SIZE
    }
}