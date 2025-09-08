package com.boost.issueTracker.ui.issue.uistate

import com.boost.issueTracker.data.model.VO.IssueContent

sealed class IssueUiState {
    data class Success(val issues: List<IssueContent>) : IssueUiState()
    data class Loading(val exception: Throwable) : IssueUiState()
    data class Failure(val exception: Throwable) : IssueUiState()
    data class NoConnection(val localIssue: List<IssueContent>): IssueUiState()
}