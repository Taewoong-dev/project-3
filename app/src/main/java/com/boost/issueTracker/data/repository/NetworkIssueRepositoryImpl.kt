package com.boost.issueTracker.data.repository

import com.boost.issueTracker.data.model.DTO.request.CloseIssueRequest
import com.boost.issueTracker.data.model.DTO.request.CreateIssueRequest
import com.boost.issueTracker.data.model.DTO.response.IssueResponse
import com.boost.issueTracker.data.model.VO.IssueComment
import com.boost.issueTracker.data.model.VO.IssueContent
import com.boost.issueTracker.data.model.VO.Label
import com.boost.issueTracker.data.model.VO.Milestone
import com.boost.issueTracker.data.network.retrofit.IssueNetworkDataSource
import com.boost.issueTracker.data.network.demo.DemoIssueDataSource
import retrofit2.HttpException
import retrofit2.Response
import java.net.UnknownHostException
import javax.inject.Inject

class NetworkIssueRepositoryImpl @Inject constructor(
    private val gitIssueApi: IssueNetworkDataSource,
    private val demoApi: DemoIssueDataSource
) : DataRepository {
    override suspend fun getNetworkIssueList(queries: Map<String, String>): Result<List<IssueContent>> {
        return try {
            val response = gitIssueApi.getIssues(queries)
            val value = response.body() ?: throw HttpException(response)
            Result.success(value.map { it.toIssueContent() })
        } catch (e: HttpException) {
            Result.failure(Throwable(e))
        } catch (e: UnknownHostException) {
            Result.failure(e)
        }
    }

    override suspend fun getLocalIssueList(): List<IssueContent> {
        return demoApi.receiveIssues().map { it.toIssueContent() }
    }

    override suspend fun getNetworkIssueDetail(issueNumber: Int): Result<IssueContent> {
        val response = gitIssueApi.getIssueDetail(issueNumber)
        return returnResult(response)
    }

    override suspend fun getIssueComments(issueNumber: Int): Result<List<IssueComment>> = try {
        val response = gitIssueApi.getIssueComments(issueNumber)
        val value = response.body() ?: throw HttpException(response)
        Result.success(value.map { it.toComment() })
    } catch (e: HttpException) {
        Result.failure(Throwable(e))
    } catch (e: UnknownHostException) {
        Result.failure(e)
    }

    override suspend fun createIssueComment(issueNumber: Int, comment: String): Result<IssueComment> = try {
        val response = gitIssueApi.createIssueComment(issueNumber, comment)
        response.body()?.run {
            Result.success(this.toComment())
        } ?: throw Exception("댓글이 비어있습니다.")
    } catch (e: HttpException) {
        Result.failure(Throwable(e))
    } catch (e: Exception) {
        Result.failure(Throwable(e))
    }

    override suspend fun getMilestoneList(): Result<List<Milestone>> = try {
        val value = gitIssueApi.getMilestone().body() ?: throw Exception("마일스톤이 비어있습니다.")
        Result.success(value.map { it.toMilestone() })
    } catch (e: HttpException) {
        Result.failure(Throwable(e))
    } catch (e: Exception) {
        Result.failure(Throwable(e))
    }

    override suspend fun getLabelList(): Result<List<Label>> = try {
        val value = gitIssueApi.getLabels().body() ?: throw Exception("라벨이 비어있습니다.")
        Result.success(value.map { it.toLabel() })
    } catch (e: HttpException) {
        Result.failure(Throwable(e))
    } catch (e: Exception) {
        Result.failure(Throwable(e))
    }

    override suspend fun createIssue(
        title: String, content: String, assignees: List<String>
    ): Result<IssueContent> {
        val response = gitIssueApi.createIssue(
            issue = CreateIssueRequest(
                title = title,
                body = content,
                assignees = assignees,
                labels = listOf(),
            )
        )
        return returnResult(response)
    }

    override suspend fun closeIssue(issueNumber: Int): Result<IssueContent> {
        val response = gitIssueApi.closeIssue(
            issueNumber = issueNumber, issue = CloseIssueRequest(
                issueNumber = issueNumber, state = "closed"
            )
        )
        return returnResult(response)
    }

    override suspend fun createMilestone(
        title: String, content: String, dueOn: String
    ): Result<Milestone> = try {
        val response = gitIssueApi.createMilestone(title, content, dueOn)
        response.body()?.run {
            Result.success(this.toMilestone())
        } ?: throw Exception("마일스톤이 비어있습니다.")
    } catch (e: HttpException) {
        Result.failure(Throwable(e))
    } catch (e: Exception) {
        Result.failure(Throwable(e))
    }

    private fun returnResult(response: Response<IssueResponse>): Result<IssueContent> = try {
        response.body()?.run {
            Result.success(this.toIssueContent())
        } ?: throw Exception("이슈가 비어있습니다.")
    } catch (e: HttpException) {
        Result.failure(Throwable(e))
    } catch (e: Exception) {
        Result.failure(Throwable(e))
    }
}