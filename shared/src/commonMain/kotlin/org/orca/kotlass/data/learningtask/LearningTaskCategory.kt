package org.orca.kotlass.data.learningtask

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A category that [LearningTask]s are sorted into for organisation!
 */
@Serializable
data class LearningTaskCategory(
    /**
     * Readable name of this category.
     */
    @SerialName("categoryName")
    val name: String,

    /**
     * Unique ID of this category.
     */
    @SerialName("categoryId")
    val id: Int,

    /**
     * Colour of this category in the general hex format: "`#RRGGBB`".
     */
    @SerialName("categoryColour")
    val colour: String
)