package org.orca.kotlass.data.user

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A given user on Compass!
 *
 * Note: distinction between this and [UserDetails] is this
 * seems to be more a summary.
 */
@Serializable
data class User(

    /**
     * Unique ID for this user.
     */
    @SerialName("id")
    val id: Int,

    /**
     * Display short code name for this user.
     */
    @SerialName("displayCode")
    val codeName: String,

    /**
     * User's first name.
     */
    @SerialName("fn")
    val firstName: String,

    /**
     * User's last name.
     */
    @SerialName("ln")
    val lastName: String,

    /**
     * User's full name.
     */
    @SerialName("n")
    val fullName: String,

    /**
     * Timestamp this User was added.
     */
    @SerialName("start")
    val startTimestamp: Instant,

)
