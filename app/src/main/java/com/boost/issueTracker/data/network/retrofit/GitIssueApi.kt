package com.boost.issueTracker.data.network.retrofit

import com.boost.issueTracker.data.model.DTO.request.CloseIssueRequest
import com.boost.issueTracker.data.model.DTO.request.CreateCommentRequest
import com.boost.issueTracker.data.model.DTO.request.CreateIssueRequest
import com.boost.issueTracker.data.model.DTO.request.CreateMilestoneRequest
import com.boost.issueTracker.data.model.DTO.request.CreateMilestoneRequestWithoutDueOn
import com.boost.issueTracker.data.model.DTO.response.CommentResponse
import com.boost.issueTracker.data.model.DTO.response.IssueResponse
import com.boost.issueTracker.data.model.DTO.response.LabelResponse
import com.boost.issueTracker.data.model.DTO.response.MilestoneResponse
import retrofit2.Response
import javax.inject.Inject

class GitIssueApi @Inject constructor(
    private val api: GitIssueService
) : IssueNetworkDataSource {

    override suspend fun getIssues(queries: Map<String, String>): Response<List<IssueResponse>> {
        return api.getIssueList(queries)
    }

    override suspend fun getIssueDetail(issueNumber: Int): Response<IssueResponse> {
        return api.getIssueDetail(issueNumber)
    }

    override suspend fun getIssueComments(issueNumber: Int): Response<List<CommentResponse>> {
        return api.getIssueComments(issueNumber)
    }

    override suspend fun createIssueComment(
        issueNumber: Int,
        comment: String
    ): Response<CommentResponse> {
        return api.createIssueComments(issueNumber, CreateCommentRequest(comment))
    }

    override suspend fun createIssue(issue: CreateIssueRequest): Response<IssueResponse> {
        return api.createIssue(issue)
    }

    override suspend fun closeIssue(
        issueNumber: Int,
        issue: CloseIssueRequest
    ): Response<IssueResponse> {
        return api.closeIssue(issueNumber, issue)
    }

    override suspend fun getMilestone(): Response<List<MilestoneResponse>> = api.getMilestones()

    override suspend fun getLabels(): Response<List<LabelResponse>> = api.getLabels()

    override suspend fun createMilestone(title: String, content: String, dueOn: String): Response<MilestoneResponse> {
        return if(dueOn.length < 10) {
            api.createMilestoneWithoutDueOn(
                CreateMilestoneRequestWithoutDueOn(title, content)
            )
        }else{
            api.createMilestone(
                CreateMilestoneRequest(title, content, dueOn)
            )
        }
    }
}