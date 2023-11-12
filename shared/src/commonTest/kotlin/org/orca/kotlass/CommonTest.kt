package org.orca.kotlass

import co.touchlab.kermit.Logger
import kotlinx.coroutines.runBlocking
import org.orca.kotlass.client.COMPASS_PRIVATE_TEST_DATA
import org.orca.kotlass.client.CompassApiClient
import org.orca.kotlass.client.CompassApiError
import org.orca.kotlass.client.CompassApiResult
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
    fun `test getting grading schemes`(): Unit = runBlocking {
        val res = client.getGradingSchemesForLearningTasks()
        assertSuccess(res)

        Logger.i { res.data.toString() }
    }

    @Test
    fun `test getting activity by id`(): Unit = runBlocking {
        val res = client.getActivity(COMPASS_PRIVATE_TEST_DATA.classActivityId)
        assertSuccess(res)

        Logger.i { res.data.toString() }
    }

    @Test
    fun `test getting activity by instance id`(): Unit = runBlocking {
        val res = client.getActivity(COMPASS_PRIVATE_TEST_DATA.classActivityInstanceId)
        assertSuccess(res)

        Logger.i { res.data.toString() }
    }

    @Test
    fun `test getting activity instance`(): Unit = runBlocking {
        val res = client.getActivityInstance(COMPASS_PRIVATE_TEST_DATA.classActivityInstanceId)
        assertSuccess(res)

        Logger.i { res.data.toString() }
    }

    @Test
    fun `test getting academic groups`(): Unit = runBlocking {
        val res = client.getAcademicGroups()
        assertSuccess(res)

        Logger.i { res.data.toString() }
    }
}