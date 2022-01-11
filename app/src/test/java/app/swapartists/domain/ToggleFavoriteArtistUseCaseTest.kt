package app.swapartists.domain

import app.swapartists.data.assembler.ArtistAssembler
import app.swapartists.data.model.ArtistNode
import app.swapartists.data.model.FavoriteArtist
import app.swapartists.data.repository.ArtistRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyCollection
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.*

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class ToggleFavoriteArtistUseCaseTest {

    private val repository: ArtistRepository = mock()
    private val assembler: ArtistAssembler = mock()
    private lateinit var subject: ToggleFavoriteArtistUseCase

    @Before
    fun setup() {
        subject = ToggleFavoriteArtistUseCase(repository, assembler)
    }

    @Test
    fun toggleFavoriteArtist_ExistsInDb() = runBlockingTest {
        val entity = FavoriteArtist("1", null, null)
        val node = ArtistNode("1", null, null)

        whenever(assembler.toFavoriteArtist(any<ArtistNode>()))
            .doReturn(entity)

        whenever(repository.existsFavoriteArtistByID(entity.id))
            .thenReturn(true)

        subject.toggleFavoriteArtist(node)

        verify(repository, times(1))
            .deleteFavoriteArtistByID(anyCollection())
    }

    @Test
    fun toggleFavoriteArtist_insertInDb() = runBlockingTest {
        val entity = FavoriteArtist("1", null, null)
        val node = ArtistNode("1", null, null)

        whenever(assembler.toFavoriteArtist(any<ArtistNode>()))
            .doReturn(entity)

        whenever(repository.existsFavoriteArtistByID(entity.id))
            .thenReturn(false)

        subject.toggleFavoriteArtist(node)

        verify(repository, times(1))
            .insertFavoriteArtist(entity)
    }
}