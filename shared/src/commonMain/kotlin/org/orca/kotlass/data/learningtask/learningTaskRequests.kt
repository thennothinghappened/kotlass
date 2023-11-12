package org.orca.kotlass.data.learningtask

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
    val activityId: Int,

    /**
     * Maximum number of tasks to return.
     */
    val limit: Int
)