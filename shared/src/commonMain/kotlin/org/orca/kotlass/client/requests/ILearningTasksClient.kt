package org.orca.kotlass.client.requests

import org.orca.kotlass.client.CompassApiResult
import org.orca.kotlass.data.activity.Activity
import org.orca.kotlass.data.learningtask.LearningTask
import org.orca.kotlass.data.user.User

/**
 * Client for getting and modifying [LearningTask]s.
 */
interface ILearningTasksClient {
    /**
     * Get a list of [LearningTask]s for a given [Activity]'s ID.
     */
    suspend fun getLearningTasksForActivity(
        activityId: Int,
        limit: Int = CompassApiRequestDefaults.DEFAULT_LIMIT,
        offset: Int = 0
    ): CompassApiResult<List<LearningTask>>

    /**
     * Get all [LearningTask]s for a given [User]'s ID.
     */
    suspend fun getLearningTasksForUserId(
        userId: Int,
        limit: Int = CompassApiRequestDefaults.DEFAULT_LIMIT,
        offset: Int = 0
    ): CompassApiResult<List<LearningTask>>

    /**
     * Get all [LearningTask]s for the default [User]'s ID.
     */
    suspend fun getLearningTasks(
        limit: Int = CompassApiRequestDefaults.DEFAULT_LIMIT,
        offset: Int = 0
    ): CompassApiResult<List<LearningTask>>
}
