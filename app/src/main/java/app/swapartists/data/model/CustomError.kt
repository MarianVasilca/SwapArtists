package app.swapartists.data.model

class GetArtistsError(message: String, cause: Throwable?) : Throwable(message, cause)

open class NetworkException(error: Throwable) : RuntimeException(error)

class NoNetworkException(error: Throwable) : NetworkException(error)

class ServerUnreachableException(error: Throwable) : NetworkException(error)