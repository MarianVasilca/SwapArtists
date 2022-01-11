package app.swapartists.data.repository

import app.swapartists.ArtistQuery
import app.swapartists.data.db.FavoriteArtistDao
import app.swapartists.data.model.GetArtistsError
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.exception.ApolloException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.doThrow
import org.mockito.kotlin.mock

@ExperimentalCoroutinesApi
class ArtistRepositoryImplTest {

    private lateinit var subject: ArtistRepositoryImpl
    private lateinit var apiClient: ApolloClient
    private lateinit var favoriteArtistDao: FavoriteArtistDao

    @Test(expected = GetArtistsError::class)
    fun getArtist_throws() = runBlockingTest {

        apiClient = mock {
            onBlocking {
                query(any<ArtistQuery>())
            } doThrow ApolloException()
        }
        favoriteArtistDao = mock()
        subject = ArtistRepositoryImpl(apiClient, favoriteArtistDao)

        subject.getArtist("123")
    }

}