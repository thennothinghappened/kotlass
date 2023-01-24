package org.orca.kotlass.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * An Academic Group represents a specific group (generally a year)
 * to sort by.
 */
@Serializable
data class AcademicGroup(
    @SerialName("__type") private val dataType: String,
    val id: Int,
    val importIdentifier: String,
    val isRelevant: Boolean,
    val name: String
)