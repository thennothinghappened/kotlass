package org.orca.kotlass.data.news

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.orca.kotlass.data.common.fileasset.FileType

/**
 * An item posted by a staff member to the Compass newsfeed. A news item may contain a description
 * as HTML, and contains zero or more attached files.
 */
@Serializable
data class NewsItem(

    /**
     * Unique ID of this news item.
     */
    @SerialName("NewsItemId")
    val id: String,

    /**
     * Title of the item.
     */
    @SerialName("Title")
    val title: String,

    /**
     * HTML-formatted description text.
     */
    @SerialName("Content1")
    val description: String,

    /**
     * List of attachments for this news item.
     */
    @SerialName("Attachments")
    val attachments: List<NewsItemAttachment>,

    /**
     * Whether this item was posted as an important priority item.
     */
    @SerialName("Priority")
    val priority: Boolean,

    /**
     * The date this item was posted.
     */
    @SerialName("PostDateTime")
    val postedAt: Instant,

    /**
     * The ID of the user who posted this item.
     */
    @SerialName("UserId")
    val posterId: Int

)

/**
 * An attachment for a news item.
 */
@Serializable
data class NewsItemAttachment(

    /**
     * Unique ID of this attachment.
     */
    @SerialName("AssetId")
    val id: Int,

    /**
     * Name of this attachment.
     */
    @SerialName("Name")
    val name: String,

    /**
     * Asset type of this attachment.
     */
    @SerialName("FileAssetType")
    val type: FileType,

    /**
     * Whether this attachment is an image - only applicable to [FileType.Link] attachments.
     */
    @SerialName("IsImage")
    val isImage: Boolean

)
