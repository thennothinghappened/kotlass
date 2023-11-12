package org.orca.kotlass.data.learningtask

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.orca.kotlass.data.common.fileasset.FileAsset
import org.orca.kotlass.data.common.fileasset.FileType

/**
 * A submission to a [LearningTask] by a given [LearningTaskTargetStudent].
 *
 * Note that this is basically a duplicate of a [FileAsset], but the
 * property names are changed and some added, so we can't easily reuse it.
 */
@Serializable
data class LearningTaskSubmission(

    /**
     * Uploaded filename.
     */
    @SerialName("fileName")
    val filename: String,

    /**
     * Compass Asset ID for the submitted file.
     */
    val fileId: String,

    /**
     * Type of the uploaded asset.
     */
    @SerialName("submissionFileType")
    val resourceType: FileType,

    /**
     * ID of the task's matching submission upload option.
     */
    @SerialName("taskSubmissionItemId")
    val taskSubmissionOptionId: Int,

    /**
     * Timestamp of the upload of this submission item.
     */
    val timestamp: Instant
)