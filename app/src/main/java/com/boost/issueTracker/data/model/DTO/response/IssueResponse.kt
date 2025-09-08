package com.boost.issueTracker.data.model.DTO.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class IssueResponse(
    val url: String?,
    @SerialName("repository_url") val repositoryUrl: String?,
    @SerialName("labels_url") val labelsUrl: String?,
    @SerialName("comments_url") val commentsUrl: String?,
    @SerialName("events_url") val eventsUrl: String?,
    @SerialName("html_url") val htmlUrl: String?,
    val id: Long,
    @SerialName("node_id") val nodeId: String?,
    val number: Int,
    val title: String?,
    val user: UserResponse,
    val labels: List<LabelResponse>,
    val state: String?,
    val locked: Boolean,
    val assignee: AssigneeResponse?,
    val assignees: List<AssigneeResponse>?,
    val milestone: MilestoneResponse?,
    val comments: Int,
    @SerialName("created_at") val createdAt: String,
    @SerialName("updated_at") val updatedAt: String?,
    @SerialName("closed_at") val closedAt: String?,
    @SerialName("author_association") val authorAssociation: String?,
    @SerialName("active_lock_reason") val activeLockReason: String?,
    val body: String?,
    val reactions: ReactionsResponse,
    @SerialName("timeline_url") val timelineUrl: String?,
    @SerialName("performed_via_github_app") val performedViaGithubApp: String?,
    @SerialName("state_reason") val stateReason: String?,
)