package org.orca.kotlass.data

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Data to send to get the newsfeed
 */
@Serializable
data class NewsFeedRequest(
    val activityId: Int? = null,
    val limit: String = "50",
    val start: String = "0"
)

/**
 * Compass' "DataExtGridDataContainer" for containing NewsItems
 */
@Serializable
data class NewsItemList(
    @SerialName("__type") private val dataType: String,
    val data: Array<NewsItem>,
    val total: Int
)

/**
 * Data type received from getMyNewsFeedPaged,
 * Contains NewsItemList - array of newsfeed items
 */
@Serializable
data class NewsItemListContainer(override val h: String? = null, override val d: NewsItemList? = null) : CData

/**
 * A news item on the newsfeed.
 */
@Serializable
data class NewsItem(
    @SerialName("__type") private val dataType: String,
    @SerialName("Title") val title: String,
    @SerialName("Content1") val content1: String?,
    @SerialName("Content2") val content2: String?,
    @SerialName("Attachments") val attachments: Array<NewsItemAttachment>,
    @SerialName("CommunicationType") val communicationType: Int, //todo: what are communication types
    @SerialName("Priority") val priority: Boolean,
    @SerialName("NotificationStatus") val notificationStatus: Int, //todo: what are the statuses
    @SerialName("CreatedByAdmin") val createdByAdmin: Boolean,
    @SerialName("SenderId") val senderId: Int,
    @SerialName("UserImageUrl") val userImageUrl: String,
    @SerialName("UserName") val userName: String,
    @Serializable(InstantSerializer::class)
    @SerialName("PostDateTime") val postDateTime: Instant?, // todo: time post was made?
    @Serializable(InstantSerializer::class)
    @SerialName("Start") val startDate: Instant?, // todo: point post will be shown from?
    @Serializable(InstantSerializer::class)
    @SerialName("Finish") val finishDate: Instant?, // useful to know when post expires
    @SerialName("NewsItemGroupTargets") val newsItemGroupTargets: Array<NewsItemGroupTarget>, // the group of people this will be sent to
    @SerialName("ShowImagesFullScreen") val showImagesFullscreen: Boolean,
    @SerialName("UserId") private val userId: Int, // duplicate of above
    @SerialName("EmailSentDate") private val emailSentDate: String,
    @SerialName("Locked") private val locked: Boolean,
    @SerialName("NewsItemCustomGroupTargets") private val newsItemCustomGroupTargets: NewsItemCustomGroupTarget, // this doesn't seem to be used
    @SerialName("NewsItemId") private val newsItemId: String, // todo: this probably isn't useful
    @SerialName("PublicWebsite") private val publicWebsite: Boolean,
    @SerialName("PublishToLinkedSchools") private val publishToLinkedSchools: Boolean,
    @SerialName("PublishToTalkingPoints") private val publishToTalkingPoints: Boolean,
    @SerialName("StartFinishString") private val startFinishString: String,
    @SerialName("TalkingPointsTags") private val talkingPointsTags: Array<String>,
)

/**
 * An attachment for a newsfeed item.
 */
@Serializable
data class NewsItemAttachment(
    @SerialName("__type") private val dataType: String,
    @SerialName("AssetId") val assetId: Int,
    @SerialName("FileAssetType") val fileAssetType: Int,
    @SerialName("IsImage") val isImage: Boolean,
    @SerialName("Name") val name: String,
    @SerialName("OriginalFileName") val originalFileName: String,
    @SerialName("UiLink") val uiLink: String,
    @SerialName("SourceOrganisationId") private val sourceOrganisastionId: String?,
    @SerialName("Url") private val url: String?
)

/**
 * Custom target for newsfeed item
 */
@Serializable
data class NewsItemCustomGroupTarget(
    @SerialName("__type") private val dataType: String,
    @SerialName("CampusIds") val campusIds: Array<Int>,
    @SerialName("CustomGroupIds") val customGroupIds: Array<Int>
)

/**
 * Target for newsfeed item
 */
@Serializable
data class NewsItemGroupTarget(
    @SerialName("__type") private val dataType: String,
    @SerialName("ActivityIds") val activityIds: Array<Unit>,
    @SerialName("BaseRole") val baseRole: Int,
    @SerialName("CampusIds") val campusIds: Array<Int>,
    @SerialName("FormGroups") val formGroups: Array<Unit>,
    @SerialName("Future") val future: Boolean,
    @SerialName("Houses") val houses: Array<String>, // todo: what format (is this a string?)
    @SerialName("UserIds") val userIds: Array<Int>,
    @SerialName("YearLevels") val yearLevsl: Array<Int>
)