package com.boost.issueTracker.ui.milestone.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.boost.issueTracker.ui.milestone.screen.MileStoneScreen
import com.boost.issueTracker.ui.milestone.screen.MilestoneCreateScreen
import com.boost.issueTracker.ui.milestone.viewmodel.MilestoneViewModel
import com.boost.issueTracker.ui.milestone.listener.HideBotNavListener

@Composable
fun MilestoneNavigation(viewModel: MilestoneViewModel, showBotNavListener: () -> Unit, hideBotNavListener: HideBotNavListener) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "milestone_list") {
        composable("milestone_list") {
            MileStoneScreen(navController, viewModel, hideBotNavListener)
        }
        composable("milestone_create") {
            MilestoneCreateScreen(navController, viewModel, showBotNavListener)
        }
    }
}