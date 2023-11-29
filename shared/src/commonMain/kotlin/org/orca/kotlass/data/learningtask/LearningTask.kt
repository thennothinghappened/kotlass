package org.orca.kotlass.data.learningtask

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.orca.kotlass.data.common.fileasset.FileAsset
import org.orca.kotlass.data.grading.GradingItem
import org.orca.kotlass.dateutils.InstantLenientSerializer

/**
 * A Learning Task is set classwork tracked on Compass.
 */
@Serializable
data class LearningTask(
    /**
     * Readable name of this task.
     */
    @SerialName("name")
    val name: String,

    /**
     * Description of this task.
     */
    @SerialName("description")
    val description: String,

    /**
     * Unique ID of this task.
     */
    @SerialName("id")
    val id: Int,

    /**
     * The ID of the [Activity] owning this task.
     */
    @SerialName("activityId")
    val activityId: Int,

    /**
     * The timestamp for creation of this task.
     */
    @SerialName("createdTimestamp")
    val createdTimestamp: Instant,

    /**
     * The timestamp when this task is due.
     */
    @Serializable(with = InstantLenientSerializer::class)
    @SerialName("dueDateTimestamp")
    val dueDateTimestamp: Instant?,

    /**
     * Corresponding ID of a given [LearningTaskCategory].
     */
    @SerialName("categoryId")
    val categoryId: Int,

    /**
     * List of included file attachments for this task.
     */
    @SerialName("attachments")
    val attachments: List<FileAsset>?,

    /**
     * List of assigned students to this task who are viewable to this user,
     * their submissions, and results.
     */
    @SerialName("students")
    val students: List<LearningTaskTargetStudent>,

    /**
     * List of submission items required to submit this task.
     */
    @SerialName("submissionItems")
    val submissionItems: List<LearningTaskSubmissionItem>?,

    /**
     * List of [GradingItem] references to [GradingScheme]s.
     */
    @SerialName("gradingItems")
    val gradingItems: List<GradingItem>?

)