package org.orca.kotlass.client.requests

import org.orca.kotlass.client.CompassApiResult
import org.orca.kotlass.client.CompassUserCredentials

/**
 * Client for checking authentication status with Compass.
 */
interface IAuthClient {

    /**
     * Set the new credentials for this client.
     */
    fun setCredentials(credentials: CompassUserCredentials)

    /**
     * Return whether provided credentials are successfully authenticated with Compass.
     *
     * NOTE: this cannot check if the provided [CompassUserCredentials.userId] is valid!
     */
    suspend fun checkAuthentication(): CompassApiResult<Unit?>
}