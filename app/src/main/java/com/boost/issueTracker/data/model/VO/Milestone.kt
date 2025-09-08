package com.boost.issueTracker.data.model.VO

data class Milestone(
    val title: String,
    val description: String?,
    val dueOn: String?,
    val openIssues: Int,
    val closedIssues: Int,
)
