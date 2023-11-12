package org.orca.kotlass.data.learningtask

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.orca.kotlass.data.common.Manager
import org.orca.kotlass.data.grading.GradingSchemeOption

/**
 * A result for a [LearningTask] for a given [LearningTaskTargetStudent],
 * on a [GradingScheme] metric.
 */
@Serializable
data class LearningTaskResult(

    /**
     * Unique ID of the [GradingSchemeOption] this result is set to,
     * if this is a fixed option.
     */
    @SerialName("reportGradingSchemeOptionId")
    val gradingSchemeOptionId: String?,

    /**
     * The result string, if used.
     */
    val result: String,

    /**
     * User ID of the [Manager] who last modified this result.
     */
    val modifiedByUserId: Int,

    /**
     * Timestamp that this result was last modified.
     */
    val modifiedTimestamp: Instant,

    /**
     * The [GradingItem] ID this result is for.
     */
    @SerialName("taskGradingItemId")
    val gradingItemId: Int,

)
