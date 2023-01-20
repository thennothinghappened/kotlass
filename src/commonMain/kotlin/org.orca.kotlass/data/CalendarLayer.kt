package org.orca.kotlass.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// todo: how are these used?

/**
 * Data to send to get the list of CalendarLayers
 */
@Serializable
data class CalendarLayersRequest(
    val start: Int = 0,
    val limit: Int = 25,
    val page: Int = 1,
)

/**
 * Data type received from getCalendarsByUser,
 * Contains array of Calendars
 */
@Serializable
data class CalendarLayerList(override val h: String? = null, override val d: Array<CalendarLayer>? = null) : CData

/**
 * Singular CalendarLayer
 */
@Serializable
data class CalendarLayer(
    private @SerialName("__type") val dataType: String,
    val color: String,
    val defaultHidden: Boolean,
    val id: Int,
    val isCustom: Boolean,
    val title: String,
    val viewOnly: Boolean, //todo: what is this for
    private val calendarManagerUserIds: Int? = null,
    private val isICal: Boolean,
    private val userIdTarget: Int? = null
)