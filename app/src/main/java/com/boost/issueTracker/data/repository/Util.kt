package com.boost.issueTracker.data.repository

import com.boost.issueTracker.data.model.DTO.response.AssigneeResponse
import com.boost.issueTracker.data.model.DTO.response.CommentResponse
import com.boost.issueTracker.data.model.DTO.response.IssueResponse
import com.boost.issueTracker.data.model.DTO.response.LabelResponse
import com.boost.issueTracker.data.model.DTO.response.MilestoneResponse
import com.boost.issueTracker.data.model.VO.FilterClasses
import com.boost.issueTracker.data.model.VO.IssueComment
import com.boost.issueTracker.data.model.VO.IssueContent
import com.boost.issueTracker.data.model.VO.Label
import com.boost.issueTracker.data.model.VO.Milestone

internal fun IssueResponse.toIssueContent(): IssueContent =
    IssueContent(
        id = this.id,
        title = this.title ?: "",
        state = this.state ?: "",
        writer = this.user.login,
        avatarUrl = this.user.avatarUrl,
        assignee = this.assignee?.login ?: "",
        labels = this.labels.map { it.toLabel() },
        content = this.body,
        mileStone = this.milestone?.title,
        issueNumber = this.number,
        createdAt = this.createdAt
    )

internal fun AssigneeResponse.toAssignee(): FilterClasses.Assignee =
    FilterClasses.Assignee(this.login)

fun MilestoneResponse.toMilestone(): Milestone = Milestone(
    title = this.title,
    description = this.description,
    dueOn = this.dueOn,
    openIssues = this.openIssues,
    closedIssues = this.closedIssues
)

fun LabelResponse.toLabel(): Label = Label(
    name = this.name,
    description = this.description,
    color = this.color
)

fun CommentResponse.toComment(): IssueComment = IssueComment(
    user = this.user.login,
    userImage = this.user.avatarUrl,
    createdAt = this.createdAt,
    comment = this.body?: "",
    reaction = this.reactions
)