package org.orca.kotlass.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LocationList(override val h: String? = null, override val d: Array<Location>? = null) : CData

@Serializable
data class Location(
    @SerialName("__type") val dataType: String,
    val archived: Boolean,
    val building: String?,
    val id: Int,
    val longName: String,
    @SerialName("n") val roomNumber: String,
    val roomName: String
)
