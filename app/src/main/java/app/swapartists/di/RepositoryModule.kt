package app.swapartists.di

import app.swapartists.data.repository.ArtistsRepository
import app.swapartists.data.repository.ArtistsRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindArtistsRepository(
        artistsRepository: ArtistsRepositoryImpl
    ): ArtistsRepository
}