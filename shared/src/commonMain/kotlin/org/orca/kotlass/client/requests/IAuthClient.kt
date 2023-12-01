package org.orca.kotlass.client.requests

import org.orca.kotlass.client.CompassApiResult
import org.orca.kotlass.client.CompassUserCredentials

/**
 * Client for checking authentication status with Compass.
 */
interface IAuthClient {
    /**
     * Return whether we are successfully authenticated with Compass.
     *
     * NOTE: this cannot check if the provided [CompassUserCredentials.userId] is valid!
     */
    suspend fun checkAuthentication(): CompassApiResult<Unit?>
}