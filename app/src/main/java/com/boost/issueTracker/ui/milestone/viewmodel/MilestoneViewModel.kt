package com.boost.issueTracker.ui.milestone.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boost.issueTracker.data.repository.DataRepository
import com.boost.issueTracker.ui.milestone.uistate.MilestoneUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MilestoneViewModel @Inject constructor(
    private val milestoneRepository: DataRepository
): ViewModel() {
    private val _milestoneList = MutableStateFlow<MilestoneUiState>(MilestoneUiState.Loading("로딩중"))
    val milestoneList: StateFlow<MilestoneUiState> = _milestoneList
    private val _creationMilestoneEvent = MutableStateFlow<MilestoneUiState>(MilestoneUiState.Loading("로딩중"))
    val creationMilestoneEvent: StateFlow<MilestoneUiState> = _creationMilestoneEvent

    init {
        getMilestoneList()
    }

    fun getMilestoneList() {
        viewModelScope.launch {
            val result = milestoneRepository.getMilestoneList()
            result.fold(
                onSuccess = {
                    _milestoneList.value = MilestoneUiState.Success(it)
                },
                onFailure = {
                    _milestoneList.value = MilestoneUiState.Error(it)
                }
            )
        }
    }

    fun createMilestone(title: String, content: String, dueOn: String) {
        viewModelScope.launch {
            val result = milestoneRepository.createMilestone(title, content, dueOn)
            _creationMilestoneEvent.value = MilestoneUiState.Loading("로딩중")
            result.fold(
                onSuccess = {
                    _creationMilestoneEvent.value = MilestoneUiState.Success(listOf(it))
                },
                onFailure = {
                    _creationMilestoneEvent.value = MilestoneUiState.Error(it)
                }
            )
        }
    }

    fun backToMilestoneList() {
        _creationMilestoneEvent.value = MilestoneUiState.Completion("Milestone 생성 완료")
        getMilestoneList()
    }

    fun loadMilestoneList() {
        _creationMilestoneEvent.value = MilestoneUiState.Loading("로딩중")
    }
}