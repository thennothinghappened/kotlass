package org.orca.kotlass.client.requests

import org.orca.kotlass.client.CompassApiResult
import org.orca.kotlass.data.academicgroup.AcademicGroup

/**
 * Client for getting [AcademicGroup]s.
 */
interface IAcademicGroupsClient {
    /**
     * Get list of [AcademicGroup]s.
     */
    suspend fun getAcademicGroups(): CompassApiResult<List<AcademicGroup>>
}