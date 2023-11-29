package org.orca.kotlass.data.activity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Request for getting a given [Activity] by its [Activity.id].
 */
@Serializable
internal data class CompassGetActivityById(
    @SerialName("activityId")
    val activityId: Int
)

/**
 * Request for getting a given [Activity] by an [instanceId] belonging to it.
 */
@Serializable
internal data class CompassGetActivityByInstanceId(
    @SerialName("instanceId")
    val instanceId: String
)