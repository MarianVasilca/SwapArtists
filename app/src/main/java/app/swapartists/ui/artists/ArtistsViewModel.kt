package app.swapartists.ui.artists

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.cachedIn
import app.swapartists.data.repository.ArtistsRepository
import app.swapartists.domain.GetErrorFromLoadStateUseCase
import app.swapartists.utilities.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
@HiltViewModel
class ArtistsViewModel @Inject constructor(
    private val repository: ArtistsRepository,
    private val errorFromLoadStateUseCase: GetErrorFromLoadStateUseCase
) : ViewModel() {

    private val clearListCh = Channel<Unit>(Channel.CONFLATED)
    private val searchQuery = MutableLiveData<String>()
    private val debouncedSearchQuery = searchQuery
        .asFlow()
        .map { it?.trim() }
        .filter { !it.isNullOrBlank() && it.length >= SEARCH_TEXT_MIN_LENGTH }
        .debounce(SEARCH_TEXT_DEBOUNCE_TIME)

    val isLoading = MutableLiveData(false)
    val infoMessageResID = SingleLiveEvent<Int>()

    val artists = flowOf(
        clearListCh.receiveAsFlow().map { PagingData.empty() },
        debouncedSearchQuery
            .filterNotNull()
            .flatMapLatest { repository.getArtistsPaged(it) }
            .cachedIn(viewModelScope)
    ).flattenMerge(2)

    fun onLoadStatesUpdated(loadStates: CombinedLoadStates) {
        setRefreshStatus(loadStates.refresh)
        setRefreshError(loadStates.refresh)
    }

    private fun setRefreshStatus(state: LoadState) {
        isLoading.value = state is LoadState.Loading
    }

    private fun setRefreshError(state: LoadState) {
        if (state is LoadState.Error) {
            infoMessageResID.value = errorFromLoadStateUseCase.getErrorResourceFromLoadState(state)
        }
    }

    companion object {
        private const val SEARCH_TEXT_DEBOUNCE_TIME = 500L // in millis
        private const val SEARCH_TEXT_MIN_LENGTH = 1
    }
}