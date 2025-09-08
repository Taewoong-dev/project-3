package com.boost.issueTracker.data.model.DTO.request

import kotlinx.serialization.Serializable

@Serializable
data class CreateCommentRequest(
    val body: String
)
