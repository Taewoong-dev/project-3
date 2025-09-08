package com.boost.issueTracker.ui.milestone.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.boost.issueTracker.R
import com.boost.issueTracker.ui.milestone.navigation.MilestoneNavigation
import com.boost.issueTracker.ui.milestone.viewmodel.MilestoneViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class MilestoneListFragment : Fragment() {

    private val viewmodel: MilestoneViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                MilestoneNavigation(viewmodel, showBotNavListener = {
                    showBottomNav()
                }, hideBotNavListener = {
                    hideBottomNav()
                })
            }
        }
    }

    private fun hideBottomNav() {
        val bottomNav = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNav.animate()
            .translationY(bottomNav.height.toFloat()) // 아래로 이동
            .alpha(0.5f) // 투명도 0
            .setDuration(500)
            .withEndAction {
                bottomNav.visibility = View.GONE  // 인스턴스는 살아있음
            }
    }

    private fun showBottomNav() {
        val bottomNav = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNav.visibility = View.VISIBLE
        bottomNav.alpha = 0f
        bottomNav.translationY = bottomNav.height.toFloat() // 아래에서 시작
        bottomNav.animate()
            .translationY(0f) // 원래 위치
            .alpha(1f)
            .setDuration(500)
            .start()
    }
}