package org.orca.kotlass.data.academicgroup

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Compass uses these groups for discriminating specific learning task groups,
 * generally by years. Generally, you want to grab the [current] group, to load
 * current tasks.
 */
@Serializable
data class AcademicGroup(
    /**
     * Unique ID for this group.
     */
    @SerialName("id")
    val id: Int,

    /**
     * Readable name for this group.
     */
    @SerialName("name")
    val name: String,

    /**
     * Whether this is the currently used group, only *one*
     * group will have this flag.
     */
    @SerialName("isRelevant")
    val current: Boolean,
)