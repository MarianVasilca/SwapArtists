package app.swapartists.ui.artists

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import app.swapartists.data.model.ArtistModel
import app.swapartists.data.model.ArtistNode
import app.swapartists.data.repository.ArtistRepository
import app.swapartists.domain.GetErrorFromLoadStateUseCase
import app.swapartists.domain.ToggleFavoriteArtistUseCase
import app.swapartists.utilities.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
@HiltViewModel
class ArtistsViewModel @Inject constructor(
    private val repository: ArtistRepository,
    private val errorFromLoadStateUseCase: GetErrorFromLoadStateUseCase,
    private val toggleFavoriteArtistUseCase: ToggleFavoriteArtistUseCase
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

    private val artists = flowOf(
        clearListCh.receiveAsFlow().map { PagingData.empty() },
        debouncedSearchQuery
            .filterNotNull()
            .flatMapLatest { repository.getArtistsPaged(it) }
            .cachedIn(viewModelScope)
    ).flattenMerge(2)

    private val savedArtists = repository.getItems()
    private val savedArtistIDs = savedArtists.mapLatest { items -> items.map { it.id } }

    val favoriteArtists = combineTransform(artists, savedArtistIDs) { artists, savedArtistIDs ->
        val result = artists.map {
            val isSaved = it.id in savedArtistIDs
            ArtistModel(isSaved, it)
        }
        emit(result)
    }

    fun onLoadStatesUpdated(loadStates: CombinedLoadStates) {
        setRefreshStatus(loadStates.refresh)
        setRefreshError(loadStates.refresh)
    }

    fun onQueryTextChanged(text: String?) {
        searchQuery.value = text
        checkEmptySearchText()
    }

    fun onFavoriteClick(item: ArtistNode) {
        viewModelScope.launch {
            toggleFavoriteArtistUseCase.toggleFavoriteArtist(item)
        }
    }

    private fun checkEmptySearchText() {
        if (searchQuery.value.isNullOrBlank()) {
            clearArtists()
        }
    }

    private fun clearArtists() {
        viewModelScope.launch {
            clearListCh.send(Unit)
        }
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