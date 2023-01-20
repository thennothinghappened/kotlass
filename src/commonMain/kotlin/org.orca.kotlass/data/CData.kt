package org.orca.kotlass.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CData<T>(
    val h: String? = null,
    val d: T? = null
)

@Serializable
data class DataExtGridDataContainer<T>(
    @SerialName("__type") private val dataType: String,
    val data: List<T>,
    private val total: Int
)