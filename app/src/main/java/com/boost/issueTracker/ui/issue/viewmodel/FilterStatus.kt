package com.boost.issueTracker.ui.issue.viewmodel

data class FilterStatus(
    val isStatusOpenChecked: Boolean,
    val isStatusClosedChecked: Boolean,
    val isAssignedToMeChecked: Boolean,
    val isCreatedByMeChecked: Boolean,
    val isMentionedToMeChecked: Boolean
) {
    fun toQueryPair(): Pair<String, String> {
        return when {
            isStatusOpenChecked -> "state" to "open"
            isStatusClosedChecked -> "state" to "closed"
            isAssignedToMeChecked -> "assignee" to "Ameri-Kano"
            isCreatedByMeChecked -> "creator" to "Ameri-Kano"
            isMentionedToMeChecked -> "mentioned" to "Ameri-Kano"
            else -> "state" to "all"
        }
    }
}
