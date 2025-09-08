package com.boost.issueTracker.data.model.enums

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class IssueState {
    @SerialName("open")
    OPEN,
    @SerialName("closed")
    CLOSED
}