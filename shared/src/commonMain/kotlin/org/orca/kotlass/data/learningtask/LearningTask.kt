package org.orca.kotlass.data.learningtask

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A Learning Task is set classwork tracked on Compass.
 */
@Serializable
data class LearningTask(
    /**
     * Readable name of this task.
     */
    val name: String,

    /**
     * Description of this task.
     */
    val description: String,

    /**
     * Unique ID of this task.
     */
    val id: Int,

    /**
     * The ID of the [Activity] owning this task.
     */
    val activityId: Int,

    /**
     * The timestamp for creation of this task.
     */
    val createdTimestamp: Instant,

    /**
     * The timestamp when this task is due.
     */
    val dueDateTimestamp: Instant,

    /**
     * Corresponding ID of a given [LearningTaskCategory].
     */
    val categoryId: Int

)