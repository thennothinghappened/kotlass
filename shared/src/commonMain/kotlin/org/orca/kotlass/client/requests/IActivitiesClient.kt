package org.orca.kotlass.client.requests

import org.orca.kotlass.client.CompassApiResult
import org.orca.kotlass.data.academicgroup.AcademicGroup
import org.orca.kotlass.data.activity.Activity
import org.orca.kotlass.data.activity.ActivityInstance
import org.orca.kotlass.data.calendar.CalendarEvent

/**
 * Client for getting [Activity]s and their [ActivityInstance]s
 */
interface IActivitiesClient {

    /**
     * Get the list of activities the given user normally has. This generally means classes,
     * as they are repeating scheduled activities for which the user is enrolled in.
     *
     * The given [academicGroup] may be null, in which case the current group is assumed.
     */
    suspend fun getStandardActivities(
        academicGroup: AcademicGroup? = null
    ): CompassApiResult<List<Activity>>

    /**
     * Get a given [Activity] by an [instanceId] belonging to it.
     */
    suspend fun getActivity(instanceId: String): CompassApiResult<Activity>

    /**
     * Get an [Activity] by its [activityId].
     */
    suspend fun getActivity(activityId: Int): CompassApiResult<Activity>

    /**
     * Get an [Activity] by one of its [ActivityInstance]s.
     */
    suspend fun getActivity(instance: ActivityInstance): CompassApiResult<Activity>

    /**
     * Get an [ActivityInstance] by its [instanceId].
     */
    suspend fun getActivityInstance(instanceId: String): CompassApiResult<ActivityInstance>

    /**
     * Get an [ActivityInstance] by its referencing [CalendarEvent.Instanced]
     */
    suspend fun getActivityInstance(calendarEvent: CalendarEvent.Instanced): CompassApiResult<ActivityInstance>
}