package com.boost.issueTracker.ui.issue.edit

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.MenuProvider
import androidx.core.widget.doOnTextChanged
import androidx.navigation.fragment.findNavController
import com.boost.issueTracker.R
import com.boost.issueTracker.databinding.FragmentEditBinding
import com.boost.issueTracker.ui.issue.IssueBaseFragment

class EditFragmentIssue : IssueBaseFragment<FragmentEditBinding>(FragmentEditBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTopBar()
    }

    private fun setTopBar() {
        binding.tbIssueEdit.apply {
            title = "새로운 이슈 작성"
            isTitleCentered = false
            setNavigationIcon(R.drawable.baseline_arrow_back_24)
            setNavigationOnClickListener {
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }
            addMenuProvider(setMenuProvider(), viewLifecycleOwner)
        }
    }

    // setTopBar()에서 호출되는 함수
    private fun setMenuProvider(): MenuProvider {
        return object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.edit_item, menu)

                // 초기 상태를 회색의 비활성화로 설정
                val createBtn = menu.findItem(R.id.complete_btn)
                createBtn.iconTintList =
                    ContextCompat.getColorStateList(requireContext(), R.color.gray)

                // EditText의 텍스트 상태를 관찰하여 메뉴 아이템 활성화/비활성화
                binding.titleInput.doOnTextChanged(onTitleAndBodyTextChanged(createBtn))
                binding.bodyInput.doOnTextChanged(onTitleAndBodyTextChanged(createBtn))
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    R.id.complete_btn -> {
                        viewmodel.createIssue(
                            binding.titleInput.text.toString(),
                            binding.bodyInput.text.toString(),
                            binding.chipGroupAssignees.checkedChipIds.map { chipId ->
                                binding.chipGroupAssignees.resources.getResourceEntryName(chipId)
                            }
                        )
                        findNavController().popBackStack(R.id.issue_fragment, false)
                    }
                }
                return true
            }
        }
    }

    // setMenuProvider()에서 호출되는 함수
    private fun onTitleAndBodyTextChanged(createBtn: MenuItem): (s: CharSequence?, start: Int, before: Int, count: Int) -> Unit =
        { _, _, _, _ ->
            if (binding.titleInput.text.isNullOrBlank() || binding.bodyInput.text.isNullOrBlank()) {
                createBtn.isEnabled = false
                createBtn.iconTintList =
                    ContextCompat.getColorStateList(requireContext(), R.color.gray)
            } else {
                createBtn.isEnabled = true
                createBtn.iconTintList =
                    ContextCompat.getColorStateList(requireContext(), R.color.black)
            }
        }
}