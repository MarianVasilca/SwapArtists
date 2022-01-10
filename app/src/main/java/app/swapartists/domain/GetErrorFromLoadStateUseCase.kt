package app.swapartists.domain

import androidx.paging.LoadState
import app.swapartists.R
import app.swapartists.data.model.GetArtistsError
import app.swapartists.data.model.NoNetworkException
import app.swapartists.data.model.ServerUnreachableException
import com.apollographql.apollo3.exception.ApolloException
import javax.inject.Inject

class GetErrorFromLoadStateUseCase @Inject constructor(
) {

    fun getErrorResourceFromLoadState(loadState: LoadState): Int {
        val errorResult = (loadState as? LoadState.Error)
        return when (errorResult?.error) {
            is NoNetworkException, is ServerUnreachableException -> R.string.error_check_network_connection
            is ApolloException, is GetArtistsError -> R.string.error_results_text
            else -> R.string.error_results_text
        }
    }

}