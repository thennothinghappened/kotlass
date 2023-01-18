package org.orca.kotlass.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LocationList(override val h: String? = null, override val d: Array<Location>? = null) : CData

@Serializable
data class Location(
    @SerialName("__type") val dataType: String,
    val archived: Boolean,
    val availableForBooking: Boolean? = null,
    val building: String? = null,
    val computerNumber: Int? = null,
    val hasCooling: Boolean? = null,
    val hasDvd: Boolean? = null,
    val hasGas: Boolean? = null,
    val hasHeating: Boolean? = null,
    val hasProjector: Boolean? = null,
    val hasSmartboard: Boolean? = null,
    val hasSoundfield: Boolean? = null,
    val hasSpeakers: Boolean? = null,
    val hasTv: Boolean? = null,
    val hasWater: Boolean? = null,
    val hasWheelchair: Boolean? = null,
    val hasWhiteboard: Boolean? = null,
    val hash: String? = null,
    val id: Int,
    val longName: String,
    @SerialName("n") val roomNumber: String? = null,
    val roomName: String? = null,
    val seatNumber: Int? = null,
    val shortName: String? = null,

)
