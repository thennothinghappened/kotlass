package org.orca.kotlass

import co.touchlab.kermit.Logger
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus
import org.orca.kotlass.client.COMPASS_PRIVATE_TEST_DATA
import org.orca.kotlass.client.CompassApiClient
import org.orca.kotlass.client.CompassApiResult
import org.orca.kotlass.data.common.CalendarEvent
import org.orca.kotlass.dateutils.now
import kotlin.test.Test
import kotlin.test.assertIs
import kotlin.test.assertTrue

class CommonTest {
    /**
     * Temporary test used for overall testing functionality.
     */
    @Test
    fun mainTest(): Unit = runBlocking {
        val client = CompassApiClient(COMPASS_PRIVATE_TEST_DATA.credentials)
        val res = client.getCalendarEvents(LocalDate(2023, 11, 3))

        assertIs<CompassApiResult.Success<List<CalendarEvent>>>(res)

        Logger.i { res.data.toString() }
    }
}