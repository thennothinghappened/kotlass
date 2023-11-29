package org.orca.kotlass.data.grading

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Reference to a [GradingScheme].
 */
@Serializable
data class GradingItem(

    /**
     * ID of the matching [GradingScheme] for this item.
     */
    @SerialName("measureUniqueId")
    val gradingSchemeId: String,

    /**
     * ID of this item, referenced in [LearningTask] results.
     */
    @SerialName("id")
    val id: Int
)