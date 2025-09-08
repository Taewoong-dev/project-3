package com.boost.issueTracker.ui.issue.detail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boost.issueTracker.data.model.VO.IssueComment
import com.boost.issueTracker.data.repository.DataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import kotlin.time.Duration

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val detailRepository: DataRepository
) : ViewModel() {
    private val _issueDetail: MutableStateFlow<IssueDetailUiState> =
        MutableStateFlow(IssueDetailUiState.Loading("로딩중"))
    val issueDetail: StateFlow<IssueDetailUiState> = _issueDetail

    private val _issueComments: MutableStateFlow<List<IssueComment>> =
        MutableStateFlow(emptyList())
    val issueComments: StateFlow<List<IssueComment>> = _issueComments

    fun getIssueDetail(id: Int) {
        viewModelScope.launch {
            _issueDetail.value = IssueDetailUiState.Loading("로딩중")
            detailRepository.getNetworkIssueDetail(id).fold(
                onSuccess = {
                    _issueDetail.value =
                        IssueDetailUiState.Success(
                            it.copy(
                                createdAt = getTimeDiffStringByDuration(
                                    getTimeDiffDuration(it.createdAt)
                                )
                            )
                        )
                },
                onFailure = {
                    _issueDetail.value = IssueDetailUiState.Failure(it)
                }
            )
        }
    }

    fun getIssueComments(id: Int) {
        viewModelScope.launch {
            detailRepository.getIssueComments(id).fold(
                onSuccess = {
                    _issueComments.value = it.map { comment ->
                        comment.copy(
                            createdAt = getTimeDiffStringByDuration(
                                getTimeDiffDuration(comment.createdAt)
                            )
                        )
                    }
                },
                onFailure = {
                    _issueComments.value = emptyList()
                }
            )
        }
    }

    fun createIssueComment(id: Int, comment: String) {
        viewModelScope.launch {
            detailRepository.createIssueComment(id, comment).fold(
                onSuccess = {
                    getIssueComments(id)
                },
                onFailure = {
                    // 그대로
                }
            )
        }
    }

    private fun getTimeDiffDuration(time: String): Duration {
        val createdInstant = Instant.from(DateTimeFormatter.ISO_INSTANT.parse(time)).epochSecond
        val timeDiffInstant = Instant.now().minusSeconds(createdInstant)
        return Duration.parse("${timeDiffInstant.epochSecond}s")
    }

    private fun getTimeDiffStringByDuration(duration: Duration) = when {
        duration.inWholeDays >= 30 -> {
            " ${duration.inWholeDays / 30}개월"
        }

        duration.inWholeDays >= 7 -> {
            "${duration.inWholeDays / 7}주"
        }

        duration.inWholeDays > 0 -> {
            "${duration.inWholeDays}일"
        }

        duration.inWholeHours > 0 -> {
            "${duration.inWholeHours}시간"
        }

        duration.inWholeMinutes > 0 -> {
            "${duration.inWholeMinutes}분"
        }

        duration.inWholeSeconds > 0 -> {
            "${duration.inWholeSeconds}초"
        }

        else -> "방금"
    }

    private fun getTimeDiffStringByLong(second: Long): String = when {
        second < 60 -> " ${second}초"
        second / 60 < 60 -> "${second / 60}분"
        second / 3600 < 24 -> "${second / 3600}시간"
        second / 3600 / 24 < 7 -> "${second / 3600 / 24}일"
        second / 3600 / 24 / 7 < 4 -> "${second / 3600 / 24 / 7}주"
        second / 3600 / 24 / 7 / 4 < 30 -> " ${second / 3600 / 24 / 7 / 4}개월"
        else -> " ${second / 3600 / 24 / 7 / 4 / 30}년"
    }
}