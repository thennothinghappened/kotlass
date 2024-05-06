package org.orca.kotlass.data.learningtask

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import org.orca.kotlass.data.activity.Activity

/**
 * A request to query the list of learning tasks.
 */
@Serializable
internal sealed interface LearningTasksRequest {

    /**
     * Maximum number of tasks to return.
     */
    @SerialName("limit")
    val limit: Int

    /**
     * The offset into the list to begin returning items from, for pagination.
     */
    @SerialName("start")
    val offset: Int

    /**
     * Request to get a list of [LearningTask]s for a given [Activity.id].
     */
    @Serializable
    class ForActivityId(
        /**
         * [Activity.id] to get tasks for.
         */
        @SerialName("activityId")
        val activityId: Int,

        @SerialName("limit")
        override val limit: Int,

        @SerialName("start")
        override val offset: Int

    ) : LearningTasksRequest

    /**
     * Request to get a list of [LearningTask]s for a given user ID.
     */
    @Serializable
    class ForUserId(

        /**
         * [User.id] to get tasks for.
         */
        @SerialName("userId")
        val userId: Int,

        @SerialName("limit")
        override val limit: Int,

        @SerialName("start")
        override val offset: Int

    ) : LearningTasksRequest

}
