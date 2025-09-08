package com.boost.issueTracker.data.model.DTO.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReactionsResponse(
    val url: String,
    @SerialName("total_count") val totalCount: Int,
    @SerialName("+1") val plusOne: Int,
    @SerialName("-1") val minusOne: Int,
    val laugh: Int,
    val hooray: Int,
    val confused: Int,
    val heart: Int,
    val rocket: Int,
    val eyes: Int
)