package com.boost.issueTracker.ui.issue.viewmodel

object DefaultStatusValues {
    val filterStatus = FilterStatus(
        isStatusOpenChecked = true,
        isStatusClosedChecked = false,
        isAssignedToMeChecked = false,
        isCreatedByMeChecked = false,
        isMentionedToMeChecked = false
    )

    val assigneeStatus = mapOf(
        "none" to false,
        "godrm" to false,
        "ivybae" to false,
        "Ameri-Kano" to false
    )

    val labelStatus = mapOf(
        "Android" to false,
        "bug" to false,
        "feature" to false,
        "K006" to false
    )

    val milestoneStatus = mapOf(
        "none" to false,
        "1" to false,
    )

    val creatorStatus = mapOf(
        "Ameri-Kano" to false
    )
}