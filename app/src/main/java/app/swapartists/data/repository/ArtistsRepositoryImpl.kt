package app.swapartists.data.repository

import com.apollographql.apollo3.ApolloClient
import javax.inject.Inject

class ArtistsRepositoryImpl @Inject constructor(
    private val apiClient: ApolloClient
) : ArtistsRepository {
}