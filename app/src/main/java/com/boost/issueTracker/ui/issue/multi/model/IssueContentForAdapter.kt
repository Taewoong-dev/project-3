package com.boost.issueTracker.ui.issue.multi.model

import com.boost.issueTracker.data.model.VO.Label

data class IssueContentForAdapter(
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
    val createdAt: String,
    val isSelected: Boolean = false
)
