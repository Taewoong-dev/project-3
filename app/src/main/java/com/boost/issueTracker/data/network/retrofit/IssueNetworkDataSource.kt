package com.boost.issueTracker.data.network.retrofit

import com.boost.issueTracker.data.model.DTO.request.CloseIssueRequest
import com.boost.issueTracker.data.model.DTO.request.CreateIssueRequest
import com.boost.issueTracker.data.model.DTO.response.CommentResponse
import com.boost.issueTracker.data.model.DTO.response.IssueResponse
import com.boost.issueTracker.data.model.DTO.response.LabelResponse
import com.boost.issueTracker.data.model.DTO.response.MilestoneResponse
import retrofit2.Response

interface IssueNetworkDataSource {
    suspend fun getIssues(queries: Map<String, String>): Response<List<IssueResponse>>
    suspend fun getIssueDetail(issueNumber: Int): Response<IssueResponse>
    suspend fun createIssue(issue: CreateIssueRequest): Response<IssueResponse>
    suspend fun closeIssue(issueNumber: Int, issue: CloseIssueRequest): Response<IssueResponse>
    suspend fun getMilestone(): Response<List<MilestoneResponse>>
    suspend fun getLabels(): Response<List<LabelResponse>>
    suspend fun createMilestone(
        title: String,
        content: String,
        dueOn: String
    ): Response<MilestoneResponse>
    suspend fun getIssueComments(issueNumber: Int): Response<List<CommentResponse>>
    suspend fun createIssueComment(issueNumber: Int, comment: String): Response<CommentResponse>
}