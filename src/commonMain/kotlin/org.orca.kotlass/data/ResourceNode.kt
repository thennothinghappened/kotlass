package org.orca.kotlass.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

/**
 * Data to send to get the ResourceNode by activityId
 */
@Serializable
data class ActivityResourcesRequest(
    val activityId: String
)

@Serializable
data class ResourceNode(
    @SerialName("__type") private val dataType: String,
    val children: List<ResourceNode>,
    val content: ResourceNodeContent? = null,
    val createdByUsername: String,
    val icon: String,
    val id: Int,
    val name: String,

    private val createdBy: Int? = null,
    private val inheritedPermissions: Boolean,
    private val parentNodeId: Int? = null,
    private val permissions: List<Int>,
    @SerialName("type") private val _type: Int,
    private val viewLevel: Int
) {
    @Transient val fileType = getFileType(_type)
}
@Serializable
data class ResourceNodeContent(
    @SerialName("__type") private val dataType: String,
    val author: String? = null,
    val description: String? = null,
    val filename: String? = null,
    val uri: String? = null,
    val fileAssetId: String? = null,

)