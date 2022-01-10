package app.swapartists.ui.artist_detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.swapartists.data.model.ArtistDetails
import app.swapartists.data.repository.ArtistsRepository
import app.swapartists.utilities.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArtistDetailViewModel @Inject constructor(
    private val artistsRepository: ArtistsRepository
) : ViewModel() {

    private val artistID = MutableLiveData<String>()

    val isLoading = MutableLiveData(false)
    val isError = SingleLiveEvent<Unit>()
    val artist = MutableLiveData<ArtistDetails>()

    fun setArtistID(artistID: String) {
        if (this.artistID.value != artistID) {
            this.artistID.value = artistID
            onArtistIdChanged(artistID)
        }
    }

    private fun onArtistIdChanged(artistID: String) {
        refreshData(artistID)
    }

    private fun refreshData(artistID: String) {
        launchDataLoad {
            artist.value = artistsRepository.getArtist(artistID)
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