package org.orca.kotlass.data

import io.ktor.http.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.json.Json

@Serializable
internal data class CData<T>(
    @SerialName("h") val error: String? = null,
    @SerialName("d") val data: T? = null
)

@Serializable
data class DataExtGridDataContainer<T>(
    @SerialName("__type") private val dataType: String,
    val data: List<T>,
    private val total: Int
)

internal val json = Json {
    encodeDefaults = true
}

internal interface Request {
    val start: Int
    val page: Int
    val limit: Int
}

internal interface SortType {
    val property: String
    val direction: String

    object Directions {
        const val ascending = "ASC"
        const val descending = "DESC"
    }
}

@Serializable
data class BaseApiRequest(
    override val start: Int = 0,
    override val limit: Int = 25,
    override val page: Int = 1
) : Request

@Serializable
class EmptyRequest()

/**
 * Return value for a request to the API.
 */
sealed interface NetResponse<T> {

    interface Error<T> {
        val error: Throwable
    }

    /**
     * When compass returns a 'h' code, this means error, either in our packet, or the cookie we used. Unfortunately it doesn't specify which is which.
     */
    data class RequestFailure<T>(private val httpStatusCode: HttpStatusCode) : Error<T>, NetResponse<T> {
        override val error = Throwable("Failed to connect with status code $httpStatusCode")
    }

    /**
     * When the client itself experiences an error.
     * The client intentionally has the JSON serialization setting to ignore unknown fields disabled, this makes sure I don't miss anything to document them correctly where possible.
     */
    data class ClientError<T>(override val error: Throwable) : Error<T>, NetResponse<T>

    /**
     * Request succeeded!
     */
    data class Success<T>( val data: T) : NetResponse<T>

}