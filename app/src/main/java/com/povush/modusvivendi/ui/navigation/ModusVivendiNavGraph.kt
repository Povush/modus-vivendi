package com.povush.modusvivendi.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.povush.modusvivendi.ui.questlines.screens.QuestCreateDestination
import com.povush.modusvivendi.ui.questlines.screens.QuestEditScreen
import com.povush.modusvivendi.ui.questlines.screens.QuestlinesDestination
import com.povush.modusvivendi.ui.questlines.screens.QuestlinesScreen

@Composable
fun ModusVivendiNavHost(
    navController: NavHostController,
    onNavigationClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        /*TODO: The first screen should be remembered*/
        startDestination = QuestlinesDestination.route,
        modifier = modifier,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None }
    ) {
        composable(route = QuestlinesDestination.route) {
            QuestlinesScreen(
                onNavigationClick = onNavigationClick,
                navigateToQuestEdit = { questId, currentQuestSectionNumber ->
                    navController.currentBackStackEntry?.savedStateHandle?.set("questId", questId)
                    navController.currentBackStackEntry?.savedStateHandle?.set("currentQuestSectionNumber", currentQuestSectionNumber)
                    navController.navigate(QuestCreateDestination.route)
                }
            )
        }
        composable(
            route = QuestCreateDestination.route,
            exitTransition = {
                fadeOut(
                    animationSpec = tween(
                        300, easing = LinearEasing
                    )
                ) + slideOutOfContainer(
                    animationSpec = tween(300, easing = EaseOut),
                    towards = AnimatedContentTransitionScope.SlideDirection.End
                )
            }
        ) {
            QuestEditScreen(
                navigateBack = { navController.popBackStack() },
                questId = navController.previousBackStackEntry?.savedStateHandle?.get<Long?>("questId"),
                currentQuestSectionNumber = navController.previousBackStackEntry?.savedStateHandle?.get<Int?>("currentQuestSectionNumber"),
            )
        }
    }
}