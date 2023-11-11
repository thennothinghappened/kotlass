package org.orca.kotlass.client

/**
 * Relevant information needed for the client to log in with
 * for a given Compass instance.
 */
data class CompassUserCredentials(
    val domain: String,
    val userId: Int,
    val cookie: String,
)