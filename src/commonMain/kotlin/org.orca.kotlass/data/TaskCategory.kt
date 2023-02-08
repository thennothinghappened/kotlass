package org.orca.kotlass.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.orca.kotlass.utils.ColourSerializer

/**
 * Singular CalendarLayer
 */
@Serializable
data class TaskCategory(
    @SerialName("__type") private val dataType: String,
    @Serializable(ColourSerializer::class)
    val categoryColour: Long,
    val categoryId: Int,
    val categoryName: String
)