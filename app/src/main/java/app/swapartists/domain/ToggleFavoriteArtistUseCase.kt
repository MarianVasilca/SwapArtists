package app.swapartists.domain

import app.swapartists.data.assembler.ArtistAssembler
import app.swapartists.data.model.ArtistNode
import app.swapartists.data.repository.ArtistRepository
import javax.inject.Inject

class ToggleFavoriteArtistUseCase @Inject constructor(
    private val artistRepository: ArtistRepository,
    private val artistAssembler: ArtistAssembler
) {

    suspend fun toggleFavoriteArtist(item: ArtistNode) {
        val existsInDb = artistRepository.getByID(item.id) != null
        if (existsInDb) {
            artistRepository.deleteByID(listOf(item.id))
        } else {
            val favoriteArtist = artistAssembler.toFavoriteArtist(item)
            artistRepository.insert(favoriteArtist)
        }
    }

}