package org.orca.kotlass.data.learningtask

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.orca.kotlass.data.common.fileasset.FileType

/**
 * A submission criteria for a [LearningTask], usually an upload box.
 */
@Serializable
data class LearningTaskSubmissionItem(
    /**
     * Unique ID used to identify which option to upload to,
     * and by [LearningTaskSubmission]s to specify their parent item.
     */
    val id: Int,

    /**
     * Readable name of this item.
     */
    val name: String,

    /**
     * File type of the upload for this item.
     */
    @SerialName("type")
    val fileType: FileType

)
