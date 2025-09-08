package com.boost.issueTracker.data.model.VO

data class IssueContent(
    val id: Long,
    val title: String,
    val state: String,
    val writer: String,
    val avatarUrl: String,
    val assignee: String,
    val labels: List<Label>,
    val content: String?,
    val mileStone: String?,
    val issueNumber: Int,
    val createdAt: String
)
