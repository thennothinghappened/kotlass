package org.orca.kotlass.client

import org.orca.kotlass.client.CompassUserCredentials

/**
 * Container for your test data (and credentials) for your
 * Compass instance to test the API client with.
 *
 * An instance in a file adjacent to this one is needed
 * creatively called `COMPASS_PRIVATE_TEST_DATA`,
 * which mustn't be checked into git if you intend to
 * test Kotlass. This isn't a great solution, but oh well.
 *
 * At some point I'd like to migrate this to a resource,
 * or command-line parameters such that it isn't built with
 * at all, but that's still finicky in KMP.
 */
data class CompassPrivateTestData(
    val credentials: CompassUserCredentials,
    val classActivityId: Int,
    val classActivityInstanceId: String,
)