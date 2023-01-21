package org.orca.kotlass.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

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