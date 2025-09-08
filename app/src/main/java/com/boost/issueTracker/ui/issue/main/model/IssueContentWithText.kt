package com.boost.issueTracker.ui.issue.main.model

import android.view.View
import android.widget.Button
import com.boost.issueTracker.data.model.VO.Label

data class IssueContentWithText(
    val id: Long,
    val title: String,
    val state: String,
    val writer: String,
    val avatarUrl: String,
    val assignee: String,
    val labels: List<Label>,
    var labelButtons: List<Button> = emptyList(),
    var content: String?,
    var mileStone: String? = null,
    val issueNumber: Int,
    val createdAt: String,
    val onClickNavigatingDetail: () -> Unit,
    val onClickPopUpMenu: (it: View) -> Unit
) {
    init {
        mileStone = mileStoneInfo()
        content = contentInfo()
    }

    private fun mileStoneInfo(): String = this.mileStone?.run { " $this" } ?: " 마일스톤 없음"

    private fun contentInfo(): String = this.content?.run { this } ?: "내용 없음"
}