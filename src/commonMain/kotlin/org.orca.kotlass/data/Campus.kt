package org.orca.kotlass.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Campus (
    @SerialName("__type") val dataType: String,
    val active: Boolean,
    val ageId: String,
    val archived: Boolean,
    val campusGuid: String,
    val campusId: Int,
    val campusName: String,
    val defaultCampus: Boolean,
    val importIdentifier: String,
    val name: String
)