package app.swapartists.ui.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.swapartists.data.model.FavoriteArtist
import app.swapartists.data.repository.ArtistRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val artistRepository: ArtistRepository
) : ViewModel() {

    val items = artistRepository.getItems()

    fun onFavoriteClick(item: FavoriteArtist) {
        viewModelScope.launch {
            artistRepository.deleteByID(listOf(item.id))
        }
    }
}