package com.boost.issueTracker.ui.label

import com.boost.issueTracker.data.model.VO.Label

sealed class LabelUiState {
    data class Loading(val message: String): LabelUiState()
    data class Success(val labelList: List<Label>): LabelUiState()
    data class Error(val throwable: Throwable): LabelUiState()
}