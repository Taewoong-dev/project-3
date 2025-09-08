package com.boost.issueTracker.data.model.DTO.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateMilestoneRequest(
    val title: String,
    val description: String,
    @SerialName("due_on") val dueOn: String,
)

@Serializable
data class CreateMilestoneRequestWithoutDueOn(
    val title: String,
    val description: String,
)