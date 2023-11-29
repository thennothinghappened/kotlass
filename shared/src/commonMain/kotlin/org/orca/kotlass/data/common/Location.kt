package org.orca.kotlass.data.common

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Container for a location and its covering location
 * if one has been set.
 *
 * (side node: the concept of a 'covering location' is
 *  pretty funny for the purpose of consistent naming)
 */
@Serializable
data class LocationContainer(
    @SerialName("LocationDetails")
    val location: Location,

    @SerialName("CoveringLocationDetails")
    val coveringLocation: Location?
)

/**
 * A given location for an event or class to take place at.
 */
@Serializable
data class Location(
    /**
     * Short readable name of this location.
     */
    @SerialName("shortName")
    val shortName: String,

    /**
     * Long readable name of this location.
     */
    @SerialName("longName")
    val longName: String
)