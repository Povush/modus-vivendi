package com.povush.modusvivendi.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.povush.modusvivendi.ui.questlines.QuestCreateDestination
import com.povush.modusvivendi.ui.questlines.QuestCreateScreen
import com.povush.modusvivendi.ui.questlines.QuestlinesDestination
import com.povush.modusvivendi.ui.questlines.QuestlinesScreen

@Composable
fun ModusVivendiNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        /*TODO: The first screen should be remembered*/
        startDestination = QuestlinesDestination.route,
        modifier = modifier
    ) {
        composable(route = QuestlinesDestination.route) {
            QuestlinesScreen(
                navigateToQuestCreate = { navController.navigate(QuestCreateDestination.route) }
            )
        }
        composable(route = QuestCreateDestination.route) {
            QuestCreateScreen(
                navigateBack = navController.popBackStack()
            )
        }
    }
}