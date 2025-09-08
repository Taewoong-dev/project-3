package com.boost.issueTracker.ui.issue.main.adapter

import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.boost.issueTracker.databinding.ItemIssueListBinding
import com.boost.issueTracker.ui.issue.main.model.IssueContentWithText

class IssueListViewHolder(private val binding: ItemIssueListBinding) : ViewHolder(binding.root) {
    private val margin = LinearLayout.LayoutParams(
        LinearLayout.LayoutParams.WRAP_CONTENT,
        LinearLayout.LayoutParams.WRAP_CONTENT
    ).apply {
        setMargins(0, 0, 30, 0)
    }

    fun bind(item: IssueContentWithText) {
        with(binding) {
            tvIssueTitle.text = item.title
            tvIssueContent.text = item.content
            tvMilestone.text = item.mileStone

            item.labelButtons.forEach {
                llLabels.addView(it, margin)
            }

            llLabels.dividerPadding = 0
            svLabels.isHorizontalScrollBarEnabled = false
            tvIssueTitle.setOnClickListener { item.onClickNavigatingDetail() }
            tvIssueContent.setOnClickListener { item.onClickNavigatingDetail() }
            btnIssue.setOnClickListener {
                item.onClickPopUpMenu(it)
            }
        }
    }

    fun unBind() {
        binding.llLabels.removeAllViews()
    }
}