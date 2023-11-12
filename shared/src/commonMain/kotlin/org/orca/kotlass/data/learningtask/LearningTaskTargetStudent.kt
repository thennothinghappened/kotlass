package org.orca.kotlass.data.learningtask

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.orca.kotlass.dateutils.InstantLenientSerializer

@Serializable
data class LearningTaskTargetStudent(
    /**
     * The timestamp when this task is due for this student
     * in the case of extensions.
     */
    @Serializable(with = InstantLenientSerializer::class)
    val dueDateTimestamp: Instant? = null,

    /**
     * Unique ID of the student this target is for.
     */
    @SerialName("userId")
    val id: Int,

    /**
     * Name of the target student.
     */
    @SerialName("userName")
    val name: String,

    /**
     * Overall timestamp of full submission, i.e. the final submitted item.
     * TODO: that's a guess, check this!
     */
    @Serializable(with = InstantLenientSerializer::class)
    val submittedTimestamp: Instant? = null,

    /**
     * List of submitted uploads by this student.
     */
    val submissions: List<LearningTaskSubmission>?,

    /**
     * Status on whether this task was submitted by this student.
     */
    val submissionStatus: LearningTaskSubmissionStatus
)