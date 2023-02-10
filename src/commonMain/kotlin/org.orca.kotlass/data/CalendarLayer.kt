package org.orca.kotlass.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.orca.kotlass.utils.ColourSerializer

// todo: how are these used?

/**
 * Data to send to get the list of CalendarLayers
 */
@Serializable
data class CalendarLayersRequest(
    val start: Int = 0,
    val limit: Int = 25,
    val page: Int = 1
)

/**
 * Singular CalendarLayer
 */
@Serializable
data class CalendarLayer(
    @SerialName("__type") private val dataType: String,
    @Serializable(ColourSerializer::class)
    val color: Long,
    val defaultHidden: Boolean,
    val id: Int,
    val isCustom: Boolean,
    val title: String,
    val viewOnly: Boolean, //todo: what is this for
    private val calendarManagerUserIds: Int? = null,
    private val isICal: Boolean,
    private val userIdTarget: Int? = null
)