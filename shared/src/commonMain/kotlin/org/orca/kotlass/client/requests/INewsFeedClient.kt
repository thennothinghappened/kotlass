package org.orca.kotlass.client.requests

import org.orca.kotlass.client.CompassApiResult
import org.orca.kotlass.data.news.NewsItem

/**
 * Client supplying the Compass newsfeed.
 */
interface INewsFeedClient {

    suspend fun newsForActivityPaged(
        activityId: Int,
        limit: Int = CompassApiRequestDefaults.DEFAULT_LIMIT,
        offset: Int = 0
    ): CompassApiResult<List<NewsItem>>

    suspend fun newsPaged(
        limit: Int = CompassApiRequestDefaults.DEFAULT_LIMIT,
        offset: Int = 0
    ): CompassApiResult<List<NewsItem>>

}
