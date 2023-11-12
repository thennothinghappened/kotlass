package org.orca.kotlass

import co.touchlab.kermit.Logger
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus
import org.orca.kotlass.client.COMPASS_PRIVATE_TEST_DATA
import org.orca.kotlass.client.CompassApiClient
import org.orca.kotlass.client.CompassApiError
import org.orca.kotlass.client.CompassApiResult
import org.orca.kotlass.data.common.CalendarEvent
import org.orca.kotlass.dateutils.now
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract
import kotlin.test.Test
import kotlin.test.assertIs
import kotlin.test.assertTrue

class CommonTest {

    @OptIn(ExperimentalContracts::class)
    private inline fun <T> assertSuccess(res: CompassApiResult<T>) {
        contract { returns() implies (res is CompassApiResult.Success) }

        if (res !is CompassApiResult.Failure) {
            return
        }

        when (val error = res.error) {
            is CompassApiError.HasCause -> {
                throw error.error
            }

            else -> {
                throw Exception(error.toString())
            }
        }
    }

    /**
     * Temporary test used for overall testing functionality.
     */
    @Test
    fun mainTest(): Unit = runBlocking {
        val client = CompassApiClient(COMPASS_PRIVATE_TEST_DATA.credentials)
        val res = client.getActivity(COMPASS_PRIVATE_TEST_DATA.classActivityId)

        assertSuccess(res)

        Logger.i { res.data.toString() }
    }
}