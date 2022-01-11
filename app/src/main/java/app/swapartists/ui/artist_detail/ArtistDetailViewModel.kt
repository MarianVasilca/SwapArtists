package app.swapartists.ui.artist_detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import app.swapartists.data.model.ArtistDetails
import app.swapartists.data.repository.ArtistRepository
import app.swapartists.domain.ToggleFavoriteArtistUseCase
import app.swapartists.utilities.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class ArtistDetailViewModel @Inject constructor(
    private val artistRepository: ArtistRepository,
    private val toggleFavoriteArtistUseCase: ToggleFavoriteArtistUseCase
) : ViewModel() {

    private val artistID = MutableLiveData<String>()

    val isLoading = MutableLiveData(false)
    val isError = SingleLiveEvent<Unit>()
    val artist = MutableLiveData<ArtistDetails>()

    val favoriteArtist = artistID.asFlow()
        .flatMapLatest { artistRepository.getFlowByID(it) }

    fun setArtistID(artistID: String) {
        if (this.artistID.value != artistID) {
            this.artistID.value = artistID
            onArtistIdChanged(artistID)
        }
    }

    fun onFavoriteClick() {
        val item = artist.value ?: return
        viewModelScope.launch {
            toggleFavoriteArtistUseCase.toggleFavoriteArtist(item)
        }
    }

    private fun onArtistIdChanged(artistID: String) {
        refreshData(artistID)
    }

    private fun refreshData(artistID: String) {
        launchDataLoad {
            artist.value = artistRepository.getArtist(artistID)
        }
    }

    private fun launchDataLoad(block: suspend () -> Unit): Job {
        return viewModelScope.launch {
            try {
                isLoading.value = true
                block()
            } catch (error: Throwable) {
                isError.call()
            } finally {
                isLoading.value = false
            }
        }
    }
}