package com.boost.issueTracker.ui.label

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boost.issueTracker.data.repository.DataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LabelViewModel @Inject constructor(
    private val labelRepository: DataRepository
): ViewModel() {
    private val _labelList = MutableStateFlow<LabelUiState>(LabelUiState.Loading("로딩중"))
    val labelList = _labelList.asStateFlow()

    init {
        getLabelList()
    }

    fun getLabelList() {
        viewModelScope.launch {
            val result = labelRepository.getLabelList()
            result.fold(
                onSuccess = {
                    _labelList.value = LabelUiState.Success(it)
                },
                onFailure = {
                    _labelList.value = LabelUiState.Error(it)
                }
            )
        }
    }
}