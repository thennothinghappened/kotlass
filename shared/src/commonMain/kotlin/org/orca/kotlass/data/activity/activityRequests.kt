package org.orca.kotlass.data.activity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.orca.kotlass.data.academicgroup.AcademicGroup

/**
 * Request for getting a given [Activity] by its [Activity.id].
 */
@Serializable
internal data class ActivityByIdRequest(
    @SerialName("activityId")
    val activityId: Int
)

/**
 * Request for getting a given [Activity] by an [instanceId] belonging to it.
 */
@Serializable
internal data class ActivityByInstanceIdRequest(
    @SerialName("instanceId")
    val instanceId: String
)

/**
 * Request for getting the standard list of activities the user is enrolled in.
 */
@Serializable
internal data class StandardActivitiesRequest(

    @SerialName("userId")
    val userId: Int,

    /**
     * The [AcademicGroup] ID to query, or `-1` for current.
     */
    @SerialName("academicGroupId")
    val academicGroupId: Int = -1

)
