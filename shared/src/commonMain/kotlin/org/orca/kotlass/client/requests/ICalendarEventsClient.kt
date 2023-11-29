package org.orca.kotlass.client.requests

import kotlinx.datetime.LocalDate
import org.orca.kotlass.client.CompassApiResult
import org.orca.kotlass.data.calendar.CalendarEvent

/**
 * Client for getting a list of [CalendarEvent]s.
 */
interface ICalendarEventsClient {
    /**
     * Get the [CalendarEvent]s for our [credentials]' `userId`, optionally
     * for a given [activityId], otherwise for all activities from [startDate] to [endDate].
     */
    suspend fun getCalendarEvents(
        startDate: LocalDate,
        endDate: LocalDate = startDate,
        activityId: Int? = null
    ): CompassApiResult<List<CalendarEvent>>
}