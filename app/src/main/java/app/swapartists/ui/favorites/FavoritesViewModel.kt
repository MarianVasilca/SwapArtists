package app.swapartists.ui.favorites

import androidx.lifecycle.ViewModel
import app.swapartists.data.repository.ArtistRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    artistRepository: ArtistRepository
) : ViewModel() {

    val items = artistRepository.getItems()
}