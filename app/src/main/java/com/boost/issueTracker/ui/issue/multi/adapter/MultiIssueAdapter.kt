package com.boost.issueTracker.ui.issue.multi.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.annotation.MenuRes
import androidx.recyclerview.widget.RecyclerView
import com.boost.issueTracker.R
import com.boost.issueTracker.data.model.VO.IssueContent
import com.boost.issueTracker.databinding.MultiViewItemBinding
import com.boost.issueTracker.ui.issue.multi.listener.CheckBoxListener
import com.boost.issueTracker.ui.issue.multi.listener.ItemCloseListener
import com.boost.issueTracker.ui.issue.multi.listener.ItemDeleteListener
import com.boost.issueTracker.ui.issue.multi.listener.CloseIssueListener
import com.boost.issueTracker.ui.issue.multi.listener.NavigateToListener
import com.boost.issueTracker.ui.issue.multi.model.IssueContentForAdapter

class MultiIssueAdapter(private val multiItems: MutableList<IssueContentForAdapter> = mutableListOf()) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var navigateToListener: NavigateToListener
    private lateinit var closeIssueListener: CloseIssueListener

    // issue upload 이벤트
    fun add(newItems: List<IssueContentForAdapter>) {
        val total = itemCount
        multiItems.clear()
        notifyItemRangeRemoved(0, total)
        multiItems.addAll(newItems)
        notifyItemRangeInserted(0, itemCount)
    }

    // issue create 이벤트
    fun create(newIssue: IssueContentForAdapter) {
        multiItems.add(newIssue)
        notifyItemInserted(0)
    }

    // 아이템 뷰에서 동작하는 이벤트, 이슈 닫기 이벤트, Listener에서 viewmodel로 close issue 요청을 한 뒤 요청이 성공이면 UI에서 삭제
    fun close(issue: IssueContent) {
        val target = multiItems.find { it.id == issue.id }
        val index = multiItems.indexOf(target)
        index.run { delete(index) }
    }

    // 아이템 뷰에서 동작하는 이벤트, 이슈 삭제 이벤트, UI상에서만 삭제
    private fun delete(itemPosition: Int) {
        multiItems.removeAt(itemPosition)
        notifyItemRemoved(itemPosition)
        notifyItemRangeChanged(itemPosition, itemCount)
    }

    fun closeMultiple() {
        val targetIssues = multiItems.toList().filter { it.isSelected }
        targetIssues.forEach { closeIssueListener.closeIssue(it.issueNumber) }
    }

    // 일괄 삭제 버튼 이벤트, 애니매이션을 위해 개별 삭제마다 notify 적용, while문에서 item의 인덱스 추적
    fun deleteMultiple() {
        val iterator = multiItems.iterator()
        var index = 0

        while (iterator.hasNext()) {
            val item = iterator.next()
            if (item.isSelected) {
                iterator.remove()
                notifyItemRemoved(index)
                notifyItemRangeChanged(index, itemCount)
                index--
            }
            index++
        }
    }

    // 전체 선택 버튼 이벤트
    fun selectAllItems(selected: Boolean) {
        for (index in multiItems.indices) {
            val copied = multiItems[index].copy(isSelected = selected)
            multiItems[index] = copied
            notifyItemRangeChanged(0, itemCount)
        }
    }

    fun getItems(): List<IssueContentForAdapter> = multiItems

    fun setNavigateToListener(customListener: NavigateToListener) {
        navigateToListener = customListener
    }

    fun setCloseIssueListener(customListener: CloseIssueListener) {
        closeIssueListener = customListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MultiPanelViewHolder(
            MultiViewItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), navigateToListener
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = multiItems[position]
        val deleteListener = ItemDeleteListener { itemPosition ->
            delete(itemPosition)
        }
        val closeListener = ItemCloseListener { closeIssueListener.closeIssue(item.issueNumber) }
        val checkBoxListener = CheckBoxListener { isChecked ->
            multiItems[position] = item.copy(isSelected = isChecked)
        }
        (holder as MultiPanelViewHolder).bind(
            item,
            deleteListener,
            closeListener,
            checkBoxListener,
        )
    }

    override fun getItemCount(): Int = multiItems.size

    class MultiPanelViewHolder(
        private val binding: MultiViewItemBinding,
        navigateListener: NavigateToListener
    ) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            val commonClickListener = View.OnClickListener { navigateListener.move() }
            with(binding) {
                multiItemTitle.setOnClickListener(commonClickListener)
                multiItemLabel.setOnClickListener(commonClickListener)
            }
        }

        fun bind(
            item: IssueContentForAdapter,
            deleteListener: ItemDeleteListener,
            closeListener: ItemCloseListener,
            checkBoxListener: CheckBoxListener,
        ) {
            with(binding) {
                multiItemTitle.text = item.title
                multiItemLabel.text =
                    StringBuilder("\uD83D\uDEA9${item.mileStone}, \uD83C\uDFF7\uFE0F${item.labels}")
                multiItemBtn.setOnClickListener {
                    showMenu(
                        binding.root.context,
                        it,
                        R.menu.close_delete_menu_item,
                        deleteListener,
                        closeListener
                    )
                }
                multiItemCheckbox.setOnCheckedChangeListener { _, isChecked ->
                    checkBoxListener.check(isChecked)
                }
                multiItemCheckbox.isChecked = item.isSelected
            }
        }

        private fun showMenu(
            context: Context,
            v: View,
            @MenuRes menuRes: Int,
            deleteListener: ItemDeleteListener,
            closeListener: ItemCloseListener
        ) {
            val popup = PopupMenu(context, v)
            popup.menuInflater.inflate(menuRes, popup.menu)

            popup.setOnMenuItemClickListener { menuItem: MenuItem ->
                if (menuItem.itemId == R.id.issue_close) {
                    closeListener.close()
                } else {
                    deleteListener.delete(adapterPosition)
                }
                true
            }
            popup.setOnDismissListener {

            }
            popup.show()
        }

    }
}