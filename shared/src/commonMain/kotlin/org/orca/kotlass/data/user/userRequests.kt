package org.orca.kotlass.data.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Request to get a given [User] by their [id].
 */
@Serializable
internal data class CompassGetUserRequest(
    @SerialName("targetUserId")
    val id: Int
)