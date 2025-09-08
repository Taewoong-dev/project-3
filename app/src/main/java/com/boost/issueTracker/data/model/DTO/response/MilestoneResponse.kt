package com.boost.issueTracker.data.model.DTO.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MilestoneResponse(
    val url: String,
    @SerialName("html_url") val htmlUrl: String,
    @SerialName("labels_url") val labelsUrl: String,
    val id: Long,
    @SerialName("node_id") val nodeId: String,
    val number: Int,
    val title: String,
    val description: String?,
    val creator: UserResponse,
    @SerialName("open_issues") val openIssues: Int,
    @SerialName("closed_issues") val closedIssues: Int,
    val state: String,
    @SerialName("created_at") val createdAt: String,
    @SerialName("updated_at") val updatedAt: String,
    @SerialName("due_on") val dueOn: String?,
    @SerialName("closed_at") val closedAt: String?
)
