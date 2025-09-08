package com.boost.issueTracker.ui.issue.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boost.issueTracker.data.model.VO.IssueContent
import com.boost.issueTracker.data.repository.DataRepository
import com.boost.issueTracker.ui.issue.uistate.IssueUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.net.UnknownHostException
import javax.inject.Inject

@HiltViewModel
class IssueListViewModel @Inject constructor(
    private val issueRepository: DataRepository
) : ViewModel() {
    private val _issueList = MutableStateFlow<IssueUiState>(IssueUiState.Loading(Throwable("로딩중")))
    val issueList: StateFlow<IssueUiState> = _issueList.asStateFlow()

    private val _filterStatus = MutableLiveData(DefaultStatusValues.filterStatus)
    val filterStatus: LiveData<FilterStatus> = _filterStatus

    private val _assigneeStatus = MutableLiveData(DefaultStatusValues.assigneeStatus)
    val assigneeStatus: LiveData<Map<String, Boolean>> = _assigneeStatus

    private val _labelStatus = MutableLiveData(DefaultStatusValues.labelStatus)
    val labelStatus: LiveData<Map<String, Boolean>> = _labelStatus

    private val _milestoneStatus = MutableLiveData(DefaultStatusValues.milestoneStatus)
    val milestoneStatus: LiveData<Map<String, Boolean>> = _milestoneStatus

    private val _creatorStatus = MutableLiveData(DefaultStatusValues.creatorStatus)
    val creatorStatus: LiveData<Map<String, Boolean>> = _creatorStatus

    private fun getQueryMap() = filterQueryMap() +
            getAssigneeStatus().toQueryMap("assignee", true) +
            getLabelStatus().toQueryMap("labels", false) +
            getMilestoneStatus().toQueryMap("milestone", true) +
            getCreatorStatus().toQueryMap("creator", true)

    fun resetStatus() {
        updateFilterStatus(DefaultStatusValues.filterStatus)
        updateAssigneeStatus(DefaultStatusValues.assigneeStatus)
        updateLabelStatus(DefaultStatusValues.labelStatus)
        updateMilestoneStatus(DefaultStatusValues.milestoneStatus)
        updateCreatorStatus(DefaultStatusValues.creatorStatus)
    }

    fun getIssueListFromAPI() {
        viewModelScope.launch {
            val result = issueRepository.getNetworkIssueList(getQueryMap())
            val value = result.getOrNull()
            value?.let {
                _issueList.value = IssueUiState.Success(value)
            } ?: run {
                _issueList.value = when (result.exceptionOrNull()) {
                    is HttpException -> IssueUiState.Failure(result.exceptionOrNull() ?: Exception("Unknown Error"))
                    is UnknownHostException -> IssueUiState.NoConnection(issueRepository.getLocalIssueList())
                    else -> {
                        IssueUiState.Failure(result.exceptionOrNull() ?: Exception("Unknown Error"))
                    }
                }
            }
        }
    }

    // Filter Status Methods
    fun updateFilterStatus(newFilterStatus: FilterStatus) {
        _filterStatus.value = newFilterStatus
    }
    private fun filterQueryMap() = mapOf(_filterStatus.value?.toQueryPair() ?: Pair("state", "all"))

    // Assignee Status Methods
    fun getAssigneeStatus() = assigneeStatus.value ?: mapOf()

    fun updateAssigneeStatus(newAssigneeStatus: Map<String, Boolean>) {
        _assigneeStatus.value = newAssigneeStatus
    }

    // Label Status Methods
    fun getLabelStatus() = labelStatus.value ?: mapOf()

    fun updateLabelStatus(newLabelStatus: Map<String, Boolean>) {
        _labelStatus.value = newLabelStatus
    }

    // Milestone Status Methods
    fun getMilestoneStatus() = milestoneStatus.value ?: mapOf()

    fun updateMilestoneStatus(newMilestoneStatus: Map<String, Boolean>) {
        _milestoneStatus.value = newMilestoneStatus
    }

    // Creator Status Methods
    fun getCreatorStatus() = creatorStatus.value ?: mapOf()

    fun updateCreatorStatus(newCreatorStatus: Map<String, Boolean>) {
        _creatorStatus.value = newCreatorStatus
    }

    fun closeIssue(issueNumber: Int, issues: List<IssueContent>) {
        viewModelScope.launch {
            val result = issueRepository.closeIssue(issueNumber)
            _issueList.value = result.getOrNull()?.let {
                IssueUiState.Success(issues)
            }?: run {
                IssueUiState.Failure(result.exceptionOrNull() ?: Throwable())
            }
        }
    }

    fun closeIssueByList(issueNumber: Int) {
        viewModelScope.launch {
            issueRepository.closeIssue(issueNumber)
        }
    }

    fun createIssue(title: String, content: String, labels: List<String>) {
        viewModelScope.launch {
            val result = issueRepository.createIssue(title, content, labels)
            val isSuccess = result.getOrNull()
            if (isSuccess == null) {
                _issueList.value = IssueUiState.Failure(result.exceptionOrNull() ?: Throwable())
            }
        }
    }

    fun refreshIssues() {
        _issueList.value = IssueUiState.Loading(Throwable("로딩중"))
        getIssueListFromAPI()
    }

    fun moveDataByNavigation(issues: List<IssueContent>) {
        _issueList.value = IssueUiState.Success(issues)
    }

    private fun Map<String, Boolean>.toQueryMap(queryKey: String, isSingleSelection: Boolean) =
        if (isSingleSelection)
            toSingleQueryMap(queryKey) else toMultipleQueryMap(queryKey)

    private fun Map<String, Boolean>.toSingleQueryMap(queryKey: String) =
        if (count { it.value } != 0)
            mapOf(queryKey to filter { it.value }.keys.first()) else mapOf()

    private fun Map<String, Boolean>.toMultipleQueryMap(queryKey: String) = mapOf(
        queryKey to filter { it.value }.keys.joinToString(",")
    )
}