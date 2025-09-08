package com.boost.issueTracker.ui.issue.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.boost.issueTracker.databinding.ItemIssueListBinding
import com.boost.issueTracker.ui.issue.main.model.IssueContentWithText

class IssueListAdapter : ListAdapter<IssueContentWithText, IssueListViewHolder>(IssueListDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IssueListViewHolder {
        val binding = ItemIssueListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return IssueListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: IssueListViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    override fun onViewRecycled(holder: IssueListViewHolder) {
        super.onViewRecycled(holder)
        holder.unBind()
    }

    fun removeItem(issueId: Long) {
        val target = currentList.find{ it.id == issueId}

        target?.run{
            val newList = currentList.toMutableList()
            newList.remove(this)
            submitList(newList)
        }
    }

    object IssueListDiffCallback : DiffUtil.ItemCallback<IssueContentWithText>() {
        override fun areItemsTheSame(oldItem: IssueContentWithText, newItem: IssueContentWithText): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: IssueContentWithText, newItem: IssueContentWithText): Boolean {
            return oldItem == newItem
        }
    }
}