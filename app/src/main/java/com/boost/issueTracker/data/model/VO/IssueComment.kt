package com.boost.issueTracker.data.model.VO

import com.boost.issueTracker.data.model.DTO.response.ReactionsResponse

data class IssueComment(
    val user: String,
    val userImage: String,
    val createdAt: String,
    val comment: String,
    val reaction: ReactionsResponse
)
