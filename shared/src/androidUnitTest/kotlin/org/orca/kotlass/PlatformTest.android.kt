package org.orca.kotlass

import org.junit.Assert.assertTrue
import org.junit.Test

class PlatformTest {

    @Test
    fun testExample() {
        assertTrue("Check android is mentioned", Greeting().greeting().contains("android"))
    }
}