package com.boost.issueTracker.ui.issue.multi.fragment

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.boost.issueTracker.R
import com.boost.issueTracker.data.model.VO.IssueContent
import com.boost.issueTracker.databinding.FragmentIssueMultiBinding
import com.boost.issueTracker.ui.issue.multi.adapter.MultiIssueAdapter
import com.boost.issueTracker.ui.issue.IssueBaseFragment
import com.boost.issueTracker.ui.issue.multi.model.IssueContentForAdapter
import com.boost.issueTracker.ui.issue.uistate.IssueUiState
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class IssueMultiFragmentIssue :
    IssueBaseFragment<FragmentIssueMultiBinding>(FragmentIssueMultiBinding::inflate) {
    private lateinit var adapter: MultiIssueAdapter
    private var isChecked = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapter()
        setObserver()
        setTopBar()
        setBottomBar()
    }

    private fun setTopBar(){
        binding.tbIssueMulti.apply {
            setNavigationIcon(R.drawable.baseline_crop_square_24)
            title = "이슈 다중 선택"
            setNavigationOnClickListener {
                isChecked = !isChecked
                handleEntireCheckbox(isChecked)
                if (isChecked) {
                    setNavigationIcon(R.drawable.outline_check_box_24)
                } else {
                    setNavigationIcon(R.drawable.baseline_crop_square_24)
                }
            }
            addMenuProvider(object : MenuProvider {
                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                    menuInflater.inflate(R.menu.multi_item, menu)
                }

                override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                    return when (menuItem.itemId) {
                        R.id.cancel_button -> {
                            viewmodel.moveDataByNavigation(adapter.getItems().map { it.toIssueContent() })
                            requireActivity().onBackPressedDispatcher.onBackPressed()
                            true
                        }

                        else -> false
                    }
                }
            }, viewLifecycleOwner)  // this@IssueMultiFragment 시 다음 fragment에서 메뉴버튼 스택이 쌓임
        }
    }

    private fun setAdapter() {
        adapter = MultiIssueAdapter()
        binding.issueRecyclerview.adapter = adapter
        adapter.setNavigateToListener { findNavController().navigate(R.id.action_issue_multi_fragment_to_detail_fragment) }
        adapter.setCloseIssueListener { issueNumber ->
            viewmodel.closeIssue(issueNumber, adapter.getItems().map { it.toIssueContent() })
        }
    }

    private fun setObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewmodel.issueList.collect { uiState ->
                    when (uiState) {
                        is IssueUiState.Success -> {
                            adapter.add(uiState.issues.map { it.toIssueContentForAdapter() })
                        }
                        is IssueUiState.Loading -> {
                            Snackbar.make(binding.root, "${uiState.exception.message}", Snackbar.LENGTH_SHORT)
                                .setAction("닫기") { it.visibility = View.GONE }
                                .show()
                        }

                        is IssueUiState.Failure -> {
                            Snackbar.make(binding.root, "${uiState.exception.message}", Snackbar.LENGTH_SHORT)
                                .setAction("닫기") { it.visibility = View.GONE }
                                .show()
                        }
                        is IssueUiState.NoConnection -> {
                            adapter.add(uiState.localIssue.map { it.toIssueContentForAdapter() })
                            Snackbar.make(binding.root, "네트워크 연결 없음", Snackbar.LENGTH_SHORT)
                                .setAction("닫기") { it.visibility = View.GONE }
                                .show()
                        }
                    }
                }
            }
        }
    }

    private fun setBottomBar() {
        binding.bottomAppBar.setNavigationOnClickListener {
            setCloseButton()
        }
        binding.bottomAppBar.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.bottom_app_bar, menu)
            }
            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.issue_delete -> {
                        setDeleteButton()
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner)
    }

    private fun handleEntireCheckbox(selected: Boolean) {
        adapter.selectAllItems(selected)
    }

    private fun setCloseButton() {
        adapter.closeMultiple()
    }

    private fun setDeleteButton() {
        adapter.deleteMultiple()
    }

    private fun IssueContent.toIssueContentForAdapter(): IssueContentForAdapter =
        IssueContentForAdapter(
            id = this.id,
            title = this.title,
            state = this.state,
            writer = this.writer,
            avatarUrl = this.avatarUrl,
            assignee = this.assignee,
            labels = this.labels,
            content = this.content,
            mileStone = this.mileStone,
            issueNumber = this.issueNumber,
            createdAt = this.createdAt
        )
    private fun IssueContentForAdapter.toIssueContent(): IssueContent =
        IssueContent(
            id = this.id,
            title = this.title,
            state = this.state,
            writer = this.writer,
            avatarUrl = this.avatarUrl,
            assignee = this.assignee,
            labels = this.labels,
            content = this.content,
            mileStone = this.mileStone,
            issueNumber = this.issueNumber,
            createdAt = this.createdAt
        )
}