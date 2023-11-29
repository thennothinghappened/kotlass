package org.orca.kotlass.client.requests

import org.orca.kotlass.client.CompassApiResult
import org.orca.kotlass.data.grading.GradingScheme

/**
 * Client for [GradingScheme]s.
 */
interface IGradingSchemesClient {
    /**
     * Get applicable [GradingScheme] list used for Learning Tasks.
     */
    suspend fun getGradingSchemesForLearningTasks(): CompassApiResult<List<GradingScheme>>
}