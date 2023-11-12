package org.orca.kotlass.data.activity

import kotlinx.serialization.Serializable

/**
 * Request for getting a given [Activity] by its [Activity.id].
 */
@Serializable
internal data class CompassGetActivityById(
    val activityId: Int
)

/**
 * Request for getting a given [Activity] by an [instanceId] belonging to it.
 */
@Serializable
internal data class CompassGetActivityByInstanceId(
    val instanceId: String
)