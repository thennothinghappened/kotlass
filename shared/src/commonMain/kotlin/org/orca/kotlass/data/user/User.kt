package org.orca.kotlass.data.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A given user on Compass!
 */
@Serializable
data class User(

    /**
     * The user's first name.
     */
    @SerialName("userFirstName")
    val firstName: String,

    /**
     * The user's last name.
     */
    @SerialName("userLastName")
    val lastName: String,

    /**
     * Internal path to a photo of the user.
     */
    @SerialName("userPhotoPath")
    val photoPath: String,

    /**
     * The user's email address.
     */
    @SerialName("userEmail")
    val email: String,

    /**
     * Unique ID of the user.
     */
    @SerialName("userId")
    val id: Int,

)
