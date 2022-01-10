package app.swapartists.di

import app.swapartists.data.repository.ArtistRepository
import app.swapartists.data.repository.ArtistRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindArtistsRepository(
        artistsRepository: ArtistRepositoryImpl
    ): ArtistRepository
}