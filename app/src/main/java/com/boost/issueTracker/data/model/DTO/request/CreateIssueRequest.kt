package com.boost.issueTracker.data.model.DTO.request

import kotlinx.serialization.Serializable

@Serializable
data class CreateIssueRequest(
    val title: String,
    val body: String? = null,
    val assignees: List<String>? = null,
    val labels: List<String>? = null,
    val milestone: String? = null,
    val state: String? = null,
)
