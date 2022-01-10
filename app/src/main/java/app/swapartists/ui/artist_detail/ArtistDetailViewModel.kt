package app.swapartists.ui.artist_detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ArtistDetailViewModel @Inject constructor(
) : ViewModel() {
    private val artistID = MutableLiveData<String>()

    fun setArtistID(artistID: String) {
        if (this.artistID.value != artistID) {
            this.artistID.value = artistID
        }
    }
}