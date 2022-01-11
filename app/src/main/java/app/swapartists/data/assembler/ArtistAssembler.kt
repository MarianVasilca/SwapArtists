package app.swapartists.data.assembler

import app.swapartists.data.model.ArtistDetails
import app.swapartists.data.model.ArtistNode
import app.swapartists.data.model.FavoriteArtist
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ArtistAssembler @Inject constructor() {

    fun toFavoriteArtist(
        item: ArtistNode
    ): FavoriteArtist {
        return FavoriteArtist(
            id = item.id,
            name = item.name,
            disambiguation = item.disambiguation
        )
    }

    fun toFavoriteArtist(
        item: ArtistDetails
    ): FavoriteArtist {
        return FavoriteArtist(
            id = item.id,
            name = item.name,
            disambiguation = item.disambiguation
        )
    }

}