package com.boost.issueTracker.ui.issue.detail

import com.boost.issueTracker.data.model.VO.IssueContent

sealed class IssueDetailUiState{
    data class Success(val issue: IssueContent): IssueDetailUiState()
    data class Loading(val message: String): IssueDetailUiState()
    data class Failure(val exception: Throwable): IssueDetailUiState()
}
