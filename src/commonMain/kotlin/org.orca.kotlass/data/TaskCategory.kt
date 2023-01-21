package org.orca.kotlass.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Singular CalendarLayer
 */
@Serializable
data class TaskCategory(
    @SerialName("__type") private val dataType: String,
    val categoryColour: String,
    val categoryId: Int,
    val categoryName: String
)