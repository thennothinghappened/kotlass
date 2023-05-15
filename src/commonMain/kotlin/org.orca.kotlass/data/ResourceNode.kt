package org.orca.kotlass.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import org.orca.kotlass.utils.FileTypeSerializer

/**
 * Data to send to get the ResourceNode by activityId
 */
@Serializable
data class ActivityResourcesRequest(
    val activityId: String
)

/**
 * A Resource Node is a singular 'node' in the folder tree of Compass' Resources.
 *
 * Nodes may have one of multiple [FileType]s.
 * Folder nodes populate the [children] list with further nodes linked below.
 * Nodes are linked to their parent by specifying their parent node's ID, in addition
 * to storing their own ID.
 *
 * Internally Compass refers to this as the 'Wiki', but this only appears to be
 * used for Resources.
 *
 * @property children List of child nodes for a folder
 * @property content Details about the content of this node
 * @property createdByUsername The name of the person who created this node
 * @property fileType The file type of this node
 * @property icon A URL path to the icon specified for this node
 * @property id The ID for this node
 * @property name Name of this node
 * @property parentNodeId ID of the node this is a child of
 */
@Serializable
data class ResourceNode(
    @SerialName("__type") private val dataType: String,
    val children: List<ResourceNode>,
    val content: ResourceNodeContent? = null,
    val createdByUsername: String,
    @Serializable(FileTypeSerializer::class)
    @SerialName("type") val fileType: FileType,
    val icon: String,
    val id: Int,
    val name: String,
    val parentNodeId: Int? = null,

    private val createdBy: Int? = null,
    private val inheritedPermissions: Boolean,
    private val permissions: List<Int>,
    private val viewLevel: Int
) {
    public fun find(id: Int) = children.find { it.id == id }

    public fun find(path: List<Int>) =
        path.fold<Int, ResourceNode?>(this) { node, i ->
            return@fold node?.find(i)
        }
}

/**
 * Details about a [ResourceNode].
 *
 * @property author The name of the person who created this node content
 * @property description Details about this node content
 * @property filename The filename of the node content when it was uploaded
 * @property uri Special property for [FileType.Link] specifying the link destination
 * @property fileAssetId ID for the associated file if stored on Compass for using with [org.orca.kotlass.IKotlassClient] `downloadFile()`
 */
@Serializable
data class ResourceNodeContent(
    @SerialName("__type") private val dataType: String,
    val author: String? = null,
    val description: String? = null,
    val filename: String? = null,
    val uri: String? = null,
    val fileAssetId: String? = null,
)