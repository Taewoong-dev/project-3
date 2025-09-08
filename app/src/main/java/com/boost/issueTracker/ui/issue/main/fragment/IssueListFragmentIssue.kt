package com.boost.issueTracker.ui.issue.main.fragment

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.PopupWindow
import androidx.annotation.MenuRes
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.ColorUtils
import androidx.core.graphics.luminance
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.boost.issueTracker.R
import com.boost.issueTracker.data.model.VO.IssueContent
import com.boost.issueTracker.databinding.FragmentIssueListBinding
import com.boost.issueTracker.databinding.PopupChecklistMenuBinding
import com.boost.issueTracker.ui.issue.IssueBaseFragment
import com.boost.issueTracker.ui.issue.main.adapter.IssueListAdapter
import com.boost.issueTracker.ui.issue.main.model.IssueContentWithText
import com.boost.issueTracker.ui.issue.uistate.IssueUiState
import com.boost.issueTracker.ui.issue.viewmodel.FilterStatus
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class IssueListFragmentIssue :
    IssueBaseFragment<FragmentIssueListBinding>(FragmentIssueListBinding::inflate) {
    private lateinit var issueListAdapter: IssueListAdapter
    private val checkBoxesList = mutableListOf<List<CheckBox>>()
    private lateinit var openCheckBox: CheckBox

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initChipGroup()
        initSwipeLayout()
        initRecyclerView()
        setTopBar()
        initFloatingActionButton()
        observeStatus()
    }

    private fun setTopBar() {
        binding.tbIssueList.setNavigationOnClickListener {
            viewmodel.moveDataByNavigation(issueListAdapter.currentList.map { it.toIssueContent() })
            findNavController().navigate(R.id.action_issue_fragment_to_issue_multi_fragment)  // 이슈 다중 선택 Fragment 로 이동
        }
    }

    private fun initChipGroup() {
        initStatusChip()
        binding.chipAsignee.initChipPopup(
            viewmodel.getAssigneeStatus().toMutableMap(),
            viewmodel::updateAssigneeStatus,
            isSingleSelection = true
        )
        binding.chipLabel.initChipPopup(
            viewmodel.getLabelStatus().toMutableMap(),
            viewmodel::updateLabelStatus,
            isSingleSelection = false
        )
        binding.chipMilestone.initChipPopup(
            viewmodel.getMilestoneStatus().toMutableMap(),
            viewmodel::updateMilestoneStatus,
            isSingleSelection = true
        )
        binding.chipCreator.initChipPopup(
            viewmodel.getCreatorStatus().toMutableMap(),
            viewmodel::updateCreatorStatus,
            isSingleSelection = true
        )
        binding.chipResetFilter.setOnClickListener {
            viewmodel.resetStatus()
            resetCheckBoxes()
            viewmodel.refreshIssues()
        }
    }

    private fun initStatusChip() {
        val popUpBinding = PopupChecklistMenuBinding.inflate(layoutInflater)
        val statusPopupWindow = PopupWindow(
            popUpBinding.root,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )

        val checkBoxes = popUpBinding.root.children.map { it as CheckBox }.toList()
        setSingleSelection(checkBoxes, statusPopupWindow)

        statusPopupWindow.setOnDismissListener {
            viewmodel.updateFilterStatus(
                FilterStatus(
                    isStatusOpenChecked = popUpBinding.checkOpen.isChecked,
                    isStatusClosedChecked = popUpBinding.checkClosed.isChecked,
                    isAssignedToMeChecked = popUpBinding.checkAssigned.isChecked,
                    isCreatedByMeChecked = popUpBinding.checkCreated.isChecked,
                    isMentionedToMeChecked = popUpBinding.checkMentioned.isChecked
                )
            )
            binding.chipStatus.isChecked = checkBoxes.isAnyChecked()
            viewmodel.refreshIssues()
        }

        binding.chipStatus.setOnClickListener { view ->
            (view as Chip).isChecked = checkBoxes.isAnyChecked()
            statusPopupWindow.showAsDropDown(view)
        }

        checkBoxesList.add(checkBoxes)
        openCheckBox = checkBoxes[0]
    }

    private fun Chip.initChipPopup(
        conditionMap: MutableMap<String, Boolean>,
        statusUpdater: (Map<String, Boolean>) -> Unit,
        isSingleSelection: Boolean
    ) {
        val checkBoxLayout = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            background = ResourcesCompat.getDrawable(resources, R.color.white, null)
        }
        val filterPopupWindow = PopupWindow(
            checkBoxLayout,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )

        val checkBoxes = mutableListOf<CheckBox>()

        conditionMap.keys.forEach { conditionKey ->
            val checkBox = CheckBox(context).apply {
                text = conditionKey
                setPaddingRelative(4, 0, 8, 0)

                setOnCheckedChangeListener { _, isChecked ->
                    conditionMap[conditionKey] = isChecked
                    if (isSingleSelection && isChecked) {
                        checkBoxes.filter { it.text != conditionKey }
                            .forEach { it.isChecked = false }
                        filterPopupWindow.dismiss()
                    }
                }
            }
            checkBoxLayout.addView(checkBox)
            checkBoxes.add(checkBox)
        }

        setOnClickListener { chip ->
            (chip as Chip).isChecked = checkBoxes.isAnyChecked()
            filterPopupWindow.showAsDropDown(chip)
        }

        filterPopupWindow.setOnDismissListener {
            statusUpdater(conditionMap)
            isChecked = checkBoxes.isAnyChecked()
            viewmodel.refreshIssues()
        }

        checkBoxesList.add(checkBoxes)
    }

    private fun setSingleSelection(
        checkBoxes: List<CheckBox>,
        statusPopupWindow: PopupWindow
    ) {
        checkBoxes.forEach { checkBox ->
            checkBox.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    checkBoxes.filter { it.id != checkBox.id }.forEach { it.isChecked = false }
                    statusPopupWindow.dismiss()
                }
            }
        }
    }

    private fun initSwipeLayout() {
        binding.slRefresh.setOnRefreshListener {
            viewmodel.refreshIssues()
            binding.slRefresh.isRefreshing = false
        }
    }

    private fun initRecyclerView() {
        issueListAdapter = IssueListAdapter()
        binding.rvIssueList.adapter = issueListAdapter
        binding.rvIssueList.addItemDecoration(
            DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        )
    }

    private fun initFloatingActionButton() {
        binding.fabNewIssue.setOnClickListener {
            findNavController().navigate(R.id.action_issue_fragment_to_edit_fragment)
        }
    }

    private fun observeStatus() {
        // 굳이 == true 를 넣은 이유는 가독성을 위해서
        fun singleStatusString(statusMap: Map<String, Boolean>) =
            if (statusMap.any { statusEntry -> statusEntry.value == true })
                ": ${statusMap.filterValues { statusValue -> statusValue == true }.keys.first()}"
            else ""

        fun multipleStatusString(statusMap: Map<String, Boolean>) =
            if (statusMap.any { statusEntry -> statusEntry.value == true })
                ": ${statusMap.filterValues { statusValue -> statusValue == true }.keys.joinToString(", ")}"
            else ""

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewmodel.issueList.collect { uiState ->
                    when (uiState) {
                        is IssueUiState.Success -> {
                            issueListAdapter.submitList(uiState.issues.toList().map { it.toIssueContentWithText() })
                            binding.pbIssueList.isVisible = false
                            binding.rvIssueList.isVisible = true
                        }

                        is IssueUiState.Failure -> {
                            binding.pbIssueList.isVisible = false
                            binding.rvIssueList.isVisible = true
                            Snackbar.make(binding.root, "네트워크에 연결 실패", Snackbar.LENGTH_SHORT).run {
                                setAction("닫기") {
                                    dismiss()
                                }
                                show()
                            }
                        }
                        is IssueUiState.Loading -> {
                            binding.pbIssueList.isVisible = true
                            binding.rvIssueList.isVisible = false
                        }
                        is IssueUiState.NoConnection -> {
                            issueListAdapter.submitList(uiState.localIssue.toList().map { it.toIssueContentWithText() })
                            binding.pbIssueList.isVisible = false
                            binding.rvIssueList.isVisible = true
                            Snackbar.make(binding.root, "네트워크 연결 없음", Snackbar.LENGTH_SHORT)
                                .setAction("닫기") { it.visibility = View.GONE }
                                .show()
                        }
                    }
                }
            }
        }

        viewmodel.filterStatus.observe(viewLifecycleOwner) {
            binding.chipStatus.text = when {
                it.isStatusOpenChecked -> getString(R.string.str_open_issue)
                it.isStatusClosedChecked -> getString(R.string.str_closed_issue)
                it.isAssignedToMeChecked -> getString(R.string.str_assigned_issue)
                it.isCreatedByMeChecked -> getString(R.string.str_created_issue)
                it.isMentionedToMeChecked -> getString(R.string.str_mentioned_issue)
                else -> ""
            }
        }

        viewmodel.assigneeStatus.observe(viewLifecycleOwner) { newAssigneeStatus ->
            val assigneeChipText =
                getString(R.string.str_assignee) + singleStatusString(newAssigneeStatus)
            binding.chipAsignee.text = assigneeChipText
        }

        viewmodel.labelStatus.observe(viewLifecycleOwner) { newLabelStatus ->
            val labelChipText = getString(R.string.str_label) + multipleStatusString(newLabelStatus)
            binding.chipLabel.text = labelChipText
        }

        viewmodel.milestoneStatus.observe(viewLifecycleOwner) { newMilestoneStatus ->
            val milestoneChipText =
                getString(R.string.str_milestone) + singleStatusString(newMilestoneStatus)
            binding.chipMilestone.text = milestoneChipText
        }

        viewmodel.creatorStatus.observe(viewLifecycleOwner) { newCreatorStatus ->
            val creatorChipText =
                getString(R.string.str_creator) + singleStatusString(newCreatorStatus)
            binding.chipCreator.text = creatorChipText
        }
    }

    override fun onResume() {
        super.onResume()
        viewmodel.refreshIssues()
    }

    private fun resetCheckBoxes() {
        checkBoxesList.forEach { checkBoxes -> checkBoxes.forEach { it.isChecked = false } }
        openCheckBox.isChecked = true
        binding.cgFilter.clearCheck()
        binding.chipStatus.isChecked = true
    }

    private fun List<CheckBox>.isAnyChecked() = this.any { it.isChecked }

    private fun IssueContent.toIssueContentWithText(): IssueContentWithText {
        return IssueContentWithText(
            id = id,
            title = title,
            state = state,
            writer = writer,
            avatarUrl = avatarUrl,
            assignee = assignee,
            labels = labels,
            labelButtons = fromLabels(requireContext()),
            content = content,
            mileStone = mileStone,
            issueNumber = issueNumber,
            createdAt = createdAt,
            onClickNavigatingDetail = {
                viewmodel.moveDataByNavigation(listOf(this@toIssueContentWithText))
                IssueListFragmentIssueDirections.actionIssueFragmentToDetailFragment(issueNumber).run {
                    findNavController().navigate(this)
                }
            },
            onClickPopUpMenu = { showMenu(requireContext(), it, R.menu.close_delete_menu_item, this@toIssueContentWithText) }
        )
    }

    private fun IssueContent.fromLabels(context: Context) =
        labels.map {
            MaterialButton(context).apply {
                val backgroundColor = Color.parseColor("#${it.color}")
                val textColor = if (backgroundColor.luminance > 0.5) Color.BLACK else Color.WHITE
                setBackgroundColor(backgroundColor)
                setTextColor(textColor)
                text = it.name
                cornerRadius = 30
                strokeWidth = 5
                strokeColor = ColorStateList.valueOf(ColorUtils.blendARGB(backgroundColor, Color.BLACK, 0.02f))
            }
        }

    private fun showMenu(
        context: Context,
        v: View,
        @MenuRes menuRes: Int,
        item: IssueContent
    ) {
        val popup = PopupMenu(context, v)
        popup.menuInflater.inflate(menuRes, popup.menu)

        popup.setOnMenuItemClickListener { menuItem: MenuItem ->
            if (menuItem.itemId == R.id.issue_close) {
                viewmodel.closeIssueByList(item.issueNumber)
            } else {
                issueListAdapter.removeItem(item.id)
            }
            true
        }
        popup.setOnDismissListener {

        }
        popup.show()
    }

    private fun IssueContentWithText.toIssueContent(): IssueContent {
        return IssueContent(
            id = id,
            title = title,
            state = state,
            writer = writer,
            avatarUrl = avatarUrl,
            assignee = assignee,
            labels = labels,
            content = content,
            mileStone = mileStone,
            issueNumber = issueNumber,
            createdAt = createdAt
        )
    }
}