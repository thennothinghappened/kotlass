package org.orca.kotlass

import org.orca.kotlass.client.COMPASS_PRIVATE_TEST_DATA
import org.orca.kotlass.client.CompassApiClient
import kotlin.test.Test
import kotlin.test.assertTrue

class CommonTest {
    /**
     * Temporary test used for overall testing functionality.
     */
    @Test
    fun mainTest() {
        val client = CompassApiClient(COMPASS_PRIVATE_TEST_DATA.credentials)
    }
}