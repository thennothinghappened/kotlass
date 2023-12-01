package org.orca.kotlass

import kotlinx.coroutines.runBlocking
import kotlinx.datetime.LocalDate
import org.orca.kotlass.client.COMPASS_PRIVATE_TEST_DATA
import org.orca.kotlass.client.CompassApiClient
import org.orca.kotlass.client.CompassApiError
import org.orca.kotlass.client.CompassApiResult
import org.orca.kotlass.dateutils.now
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract
import kotlin.test.Test

class CommonTest {

    private val client = CompassApiClient(COMPASS_PRIVATE_TEST_DATA.credentials)

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

    @Test
    fun `test authentication`(): Unit = runBlocking {
        val res = client.checkAuthentication()
        assertSuccess(res)
    }

    @Test
    fun `test reloading credentials`(): Unit = runBlocking {
        `test authentication`()
        client.setCredentials(COMPASS_PRIVATE_TEST_DATA.credentials)
        `test authentication`()
    }

    @Test
    fun `test getting grading schemes`(): Unit = runBlocking {
        val res = client.getGradingSchemesForLearningTasks()
        assertSuccess(res)
    }

    @Test
    fun `test getting calendar events for a day`(): Unit = runBlocking {
        val res = client.getCalendarEvents(LocalDate.now())
        assertSuccess(res)
    }

    @Test
    fun `test getting activity by id`(): Unit = runBlocking {
        val res = client.getActivity(COMPASS_PRIVATE_TEST_DATA.classActivityId)
        assertSuccess(res)
    }

    @Test
    fun `test getting activity by instance id`(): Unit = runBlocking {
        val res = client.getActivity(COMPASS_PRIVATE_TEST_DATA.classActivityInstanceId)
        assertSuccess(res)
    }

    @Test
    fun `test getting activity instance`(): Unit = runBlocking {
        val res = client.getActivityInstance(COMPASS_PRIVATE_TEST_DATA.classActivityInstanceId)
        assertSuccess(res)
    }

    @Test
    fun `test getting academic groups`(): Unit = runBlocking {
        val res = client.getAcademicGroups()
        assertSuccess(res)
    }

    @Test
    fun `test getting learning tasks for activity id`(): Unit = runBlocking {
        val res = client.getLearningTasksForActivity(COMPASS_PRIVATE_TEST_DATA.classActivityId)
        assertSuccess(res)
    }

    @Test
    fun `test getting learning tasks for our user`(): Unit = runBlocking {
        val res = client.getLearningTasks()
        assertSuccess(res)
    }

    @Test
    fun `test getting our user info`(): Unit = runBlocking {
        val res = client.getMyUserDetails()
        assertSuccess(res)
    }

    @Test
    fun `test getting all staff`(): Unit = runBlocking {
        val res = client.getAllStaff()
        assertSuccess(res)
    }
}