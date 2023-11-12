package org.orca.kotlass.data.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Request to get a given [UserDetails] by their [id].
 */
@Serializable
internal data class CompassGetUserDetailsRequest(
    @SerialName("targetUserId")
    val userId: Int
)

/**
 * Request to get all staff members.
 */
@Serializable
internal data class CompassGetStaffRequest(
    /**
     * Max number of [User]s to return.
     */
    val limit: Int
)
