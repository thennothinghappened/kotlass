package org.orca.kotlass.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class CData<T>(
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