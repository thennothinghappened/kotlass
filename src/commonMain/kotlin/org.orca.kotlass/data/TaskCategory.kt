package org.orca.kotlass.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Data to send to get the list of CalendarLayers
 */
@Serializable
data class TaskCategoriesRequest(
    val start: Int = 0,
    val limit: Int = 25,
    val page: Int = 1,
)

/**
 * Data type received from getCalendarsByUser,
 * Contains array of Calendars
 */
@Serializable
data class TaskCategoryList(override val h: String? = null, override val d: Array<TaskCategory>? = null) : CData

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