package com.boost.issueTracker.data.repository

import com.boost.issueTracker.data.model.VO.IssueComment
import com.boost.issueTracker.data.model.VO.IssueContent
import com.boost.issueTracker.data.model.VO.Label
import com.boost.issueTracker.data.model.VO.Milestone

interface DataRepository {
    suspend fun getNetworkIssueList(queries: Map<String, String>): Result<List<IssueContent>>
    suspend fun getLocalIssueList(): List<IssueContent>
    suspend fun getNetworkIssueDetail(issueNumber: Int): Result<IssueContent>
    suspend fun getMilestoneList(): Result<List<Milestone>>
    suspend fun getLabelList(): Result<List<Label>>
    suspend fun createIssue(
        title: String, content: String, assignees: List<String>
    ): Result<IssueContent>
    suspend fun closeIssue(issueNumber: Int): Result<IssueContent>
    suspend fun createMilestone(
        title: String, content: String, dueOn: String
    ): Result<Milestone>
    suspend fun getIssueComments(issueNumber: Int): Result<List<IssueComment>>
    suspend fun createIssueComment(issueNumber: Int, comment: String): Result<IssueComment>
}