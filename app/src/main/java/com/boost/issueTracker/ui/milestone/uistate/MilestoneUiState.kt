package com.boost.issueTracker.ui.milestone.uistate

import com.boost.issueTracker.data.model.VO.Milestone

sealed class MilestoneUiState {
    data class Loading(val message: String): MilestoneUiState()
    data class Success(val milestoneList: List<Milestone>): MilestoneUiState()
    data class Completion(val message: String): MilestoneUiState()
    data class Error(val throwable: Throwable): MilestoneUiState()
}