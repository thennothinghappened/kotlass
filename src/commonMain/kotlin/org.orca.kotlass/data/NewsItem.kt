package org.orca.kotlass.data

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
data class NewsItemsContainer(
    @SerialName("__type") val dataType: String,
    val data: Array<NewsItem>,
    val total: Int
)

/**
 * Data type received from getMyNewsFeedPaged,
 * Contains NewsItemContainer - array of newsfeed items
 */
@Serializable
data class NewsItemList(override val h: String? = null, override val d: NewsItemsContainer? = null) : CData

/**
 * A news item on the newsfeed.
 */
@Serializable
data class NewsItem(
    @SerialName("__type") val dataType: String,
    @SerialName("Title") val title: String,
    @SerialName("Content1") val content1: String?,
    @SerialName("Content2") val content2: String?,
    @SerialName("Attachments") val attachments: Array<NewsItemAttachment>,
    @SerialName("CommunicationType") val communicationType: Int,
    @SerialName("Priority") val priority: Boolean,
    @SerialName("NotificationStatus") val notificationStatus: Int,
    @SerialName("CreatedByAdmin") val createdByAdmin: Boolean,
    @SerialName("SenderId") val senderId: Int,
    @SerialName("UserId") val userId: Int,
    @SerialName("UserImageUrl") val userImageUrl: String,
    @SerialName("UserName") val userName: String,
    @SerialName("EmailSentDate") val emailSentDate: String,
    @SerialName("PostDateTime") val postDateTime: String,
    @SerialName("Start") val startDate: String,
    @SerialName("Finish") val finishDate: String,
    @SerialName("Locked") val locked: Boolean,
    @SerialName("NewsItemCustomGroupTargets") val newsItemCustomGroupTargets: NewsItemCustomGroupTarget,
    @SerialName("NewsItemGroupTargets") val newsItemGroupTargets: Array<NewsItemGroupTarget>,
    @SerialName("NewsItemId") val newsItemId: String,
    @SerialName("PublicWebsite") val publicWebsite: Boolean,
    @SerialName("PublishToLinkedSchools") val publishToLinkedSchools: Boolean,
    @SerialName("PublishToTalkingPoints") val publishToTalkingPoints: Boolean,
    @SerialName("ShowImagesFullScreen") val showImagesFullscreen: Boolean,
    @SerialName("StartFinishString") val startFinishString: String,
    @SerialName("TalkingPointsTags") val talkingPointsTags: Array<String>,
)

/**
 * An attachment for a newsfeed item.
 */
@Serializable
data class NewsItemAttachment(
    @SerialName("__type") val dataType: String,
    @SerialName("AssetId") val assetId: Int,
    @SerialName("FileAssetType") val fileAssetType: Int,
    @SerialName("IsImage") val isImage: Boolean,
    @SerialName("Name") val name: String,
    @SerialName("OriginalFileName") val originalFileName: String,
    @SerialName("SourceOrganisationId") val sourceOrganisastionId: String?,
    @SerialName("UiLink") val uiLink: String,
    @SerialName("Url") val url: String?
)

/**
 * Custom target for newsfeed item
 */
@Serializable
data class NewsItemCustomGroupTarget(
    @SerialName("__type") val dataType: String,
    @SerialName("CampusIds") val campusIds: Array<Int>,
    @SerialName("CustomGroupIds") val customGroupIds: Array<Int>
)

/**
 * Target for newsfeed item
 */
@Serializable
data class NewsItemGroupTarget(
    @SerialName("__type") val dataType: String,
    @SerialName("ActivityIds") val activityIds: Array<Unit>,
    @SerialName("BaseRole") val baseRole: Int,
    @SerialName("CampusIds") val campusIds: Array<Int>,
    @SerialName("FormGroups") val formGroups: Array<Unit>,
    @SerialName("Future") val future: Boolean,
    @SerialName("Houses") val houses: Array<String>,
    @SerialName("UserIds") val userIds: Array<Int>,
    @SerialName("YearLevels") val yearLevsl: Array<Int>
)