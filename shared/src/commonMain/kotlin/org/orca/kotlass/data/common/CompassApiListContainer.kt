package org.orca.kotlass.data.common

import kotlinx.serialization.Serializable

/**
 * Container sometimes used by Compass to contain a list
 * of items.
 */
@Serializable
internal data class CompassApiListContainer<T>(
    val data: List<T>
)