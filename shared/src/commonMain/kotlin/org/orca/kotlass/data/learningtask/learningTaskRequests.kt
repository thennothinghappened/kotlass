package org.orca.kotlass.data.learningtask

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.orca.kotlass.data.activity.Activity

/**
 * Request to get a list of [LearningTask]s for a given [Activity.id].
 */
@Serializable
internal data class CompassGetLearningTasksForActivityId(
    /**
     * [Activity.id] to get tasks for.
     */
    @SerialName("activityId")
    val activityId: Int,

    /**
     * Maximum number of tasks to return.
     */
    @SerialName("limit")
    val limit: Int
)

/**
 * Request to get a list of [LearningTask]s for a given user ID.
 */
@Serializable
internal data class CompassGetLearningTasksForUserId(
    /**
     * [User.id] to get tasks for.
     */
    @SerialName("userId")
    val userId: Int,

    /**
     * Maximum number of tasks to return.
     */
    @SerialName("limit")
    val limit: Int
)