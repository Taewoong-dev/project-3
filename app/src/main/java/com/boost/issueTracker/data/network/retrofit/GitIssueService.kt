package com.boost.issueTracker.data.network.retrofit

import com.boost.issueTracker.data.model.DTO.request.CloseIssueRequest
import com.boost.issueTracker.data.model.DTO.request.CreateCommentRequest
import com.boost.issueTracker.data.model.DTO.request.CreateIssueRequest
import com.boost.issueTracker.data.model.DTO.request.CreateMilestoneRequest
import com.boost.issueTracker.data.model.DTO.request.CreateMilestoneRequestWithoutDueOn
import com.boost.issueTracker.data.model.DTO.response.AssigneeResponse
import com.boost.issueTracker.data.model.DTO.response.CommentResponse
import com.boost.issueTracker.data.model.DTO.response.IssueResponse
import com.boost.issueTracker.data.model.DTO.response.LabelResponse
import com.boost.issueTracker.data.model.DTO.response.MilestoneResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface GitIssueService {
    @GET("issues")
    suspend fun getIssueList(@QueryMap queries: Map<String, String>): Response<List<IssueResponse>>

    @GET("issues/{issue_number}")
    suspend fun getIssueDetail(
        @Path("issue_number") issueNumber: Int
    ): Response<IssueResponse>

    @GET("issues/{issue_number}/comments")
    suspend fun getIssueComments(
        @Path("issue_number") issueNumber: Int
    ): Response<List<CommentResponse>>

    @POST("issues/{issue_number}/comments")
    suspend fun createIssueComments(
        @Path("issue_number") issueNumber: Int,
        @Body comment: CreateCommentRequest
    ): Response<CommentResponse>

    @GET("assignees")
    suspend fun getAssignees(
        @Query("per_page") perPage: Int = 100,
    ): Response<List<AssigneeResponse>>

    @GET("milestones")
    suspend fun getMilestones(
        @Query("per_page") perPage: Int = 100,
    ): Response<List<MilestoneResponse>>

    @GET("labels")
    suspend fun getLabels(
        @Query("per_page") perPage: Int = 100,
    ): Response<List<LabelResponse>>

    @POST("issues")
    suspend fun createIssue(
        @Body issue: CreateIssueRequest
    ): Response<IssueResponse>

    @POST("{issue_number}")
    suspend fun closeIssue(
        @Path("issue_number") issueNumber: Int,
        @Body issue: CloseIssueRequest
    ): Response<IssueResponse>

    @POST("milestones")
    suspend fun createMilestone(
        @Body milestone: CreateMilestoneRequest
    ): Response<MilestoneResponse>

    @POST("milestones")
    suspend fun createMilestoneWithoutDueOn(
        @Body milestone: CreateMilestoneRequestWithoutDueOn
    ): Response<MilestoneResponse>
}