package com.boost.issueTracker.data.model.DTO.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CloseIssueRequest(
    @SerialName("issue_number") val issueNumber: Int,
    @SerialName("state") val state: String,
)