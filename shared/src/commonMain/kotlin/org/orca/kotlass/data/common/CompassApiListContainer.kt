package org.orca.kotlass.data.common

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Container sometimes used by Compass to contain a list
 * of items.
 */
@Serializable
internal data class CompassApiListContainer<T>(
    @SerialName("data")
    val data: List<T>
)