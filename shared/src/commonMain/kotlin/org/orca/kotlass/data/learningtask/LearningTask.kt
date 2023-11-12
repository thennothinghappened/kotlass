package org.orca.kotlass.data.learningtask

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.orca.kotlass.data.common.fileasset.FileAsset
import org.orca.kotlass.data.grading.GradingItem

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
    val categoryId: Int,

    /**
     * List of included file attachments for this task.
     */
    val attachments: List<FileAsset>?,

    /**
     * List of assigned students to this task who are viewable to this user,
     * their submissions, and results.
     */
    val students: List<LearningTaskTargetStudent>,

    /**
     * List of submission items required to submit this task.
     */
    val submissionItems: List<LearningTaskSubmissionItem>?,

    /**
     * List of [GradingItem] references to [GradingScheme]s.
     */
    val gradingItems: List<GradingItem>?

)