package com.boost.issueTracker.data.model.DTO.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CommentResponse(
    val id: Long,
    @SerialName("node_id") val nodeId: String,
    val url: String,
    val body: String?,
    @SerialName("html_url") val htmlUrl: String,
    val user: UserResponse,
    @SerialName("created_at") val createdAt: String,
    @SerialName("updated_at") val updatedAt: String,
    @SerialName("issue_url") val issueUrl: String,
    @SerialName("author_association") val authorAssociation: String,
    val reactions: ReactionsResponse
)