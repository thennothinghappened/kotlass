package org.orca.kotlass.data.calendar

import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Request for getting a [List] of [CalendarEvent]s!
 */
@Serializable
internal data class CompassGetCalendarEventsByUser(
    @SerialName("userId")
    val userId: Int,

    @SerialName("startDate")
    val startDate: LocalDate,

    @SerialName("endDate")
    val endDate: LocalDate,

    @SerialName("activityId")
    val activityId: Int? = null
)