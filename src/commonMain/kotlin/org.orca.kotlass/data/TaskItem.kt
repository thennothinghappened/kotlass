package org.orca.kotlass.data

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.orca.kotlass.utils.InstantNullableSerializer

/**
 * Data to send to create or modify a TaskItem
 */
@Serializable
data class TaskItemRequest(
    val task: TaskItemRequestBody
) {
    @Serializable
    data class TaskItemRequestBody(
        var id: Int = 0,
        var status: Boolean = false,
        var taskName: String,
        @Serializable(InstantNullableSerializer::class)
        var dueDate: Instant? = null
    )
}

/**
 * A single task, which is a user-defined name and completion date.
 *
 * Of note, compass doesn't seem to limit the length of the taskName property in these, so they could be used for data storage between clients.
 */
@Serializable
data class TaskItem(
    @SerialName("__type") private val dataType: String? = null,
    @Serializable(InstantNullableSerializer::class)
    val dueDate: Instant? = null,
    val id: Int = 0,
    val status: Boolean = false,
    val taskName: String
)