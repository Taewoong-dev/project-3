package com.boost.issueTracker.data.network.demo

import com.boost.issueTracker.data.model.DTO.response.IssueResponse

interface DemoIssueDataSource {
    suspend fun receiveIssues(): List<IssueResponse>
}