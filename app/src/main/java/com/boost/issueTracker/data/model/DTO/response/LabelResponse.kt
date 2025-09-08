package com.boost.issueTracker.data.model.DTO.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LabelResponse(
    val id: Long,
    @SerialName("node_id") val nodeId: String,
    val url: String,
    val name: String,
    val color: String,
    val default: Boolean,
    val description: String?
)
