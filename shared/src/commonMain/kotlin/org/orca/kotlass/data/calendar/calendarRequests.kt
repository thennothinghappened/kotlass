package org.orca.kotlass.data.calendar

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

/**
 * Request for getting a [List] of [CalendarEvent]s!
 */
@Serializable
internal data class CompassGetCalendarEventsByUser(
    val userId: Int,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val activityId: Int? = null
)