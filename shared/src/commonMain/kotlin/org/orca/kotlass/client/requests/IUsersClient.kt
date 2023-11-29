package org.orca.kotlass.client.requests

import org.orca.kotlass.client.CompassApiResult
import org.orca.kotlass.data.user.User
import org.orca.kotlass.data.user.UserDetails

interface IUsersClient {
    /**
     * Get a given user's [UserDetails].
     *
     * *As a student*, this will return no data for any user other than their
     * own ID.
     */
    suspend fun getUserDetails(userId: Int): CompassApiResult<UserDetails>

    /**
     * Get our [UserDetails].
     */
    suspend fun getMyUserDetails(): CompassApiResult<UserDetails>

    /**
     * Get the list of all staff members [User].
     */
    suspend fun getAllStaff(limit: Int = CompassApiRequestDefaults.DEFAULT_LIMIT): CompassApiResult<List<User>>
}