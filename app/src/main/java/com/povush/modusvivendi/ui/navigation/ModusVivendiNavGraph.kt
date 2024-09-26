package com.povush.modusvivendi.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
                navigateToQuestCreate = { currentQuestSectionNumber ->
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
            QuestCreateScreen(
                navigateBack = { navController.popBackStack() },
                currentQuestSectionNumber = navController.previousBackStackEntry?.savedStateHandle?.get<Int>("currentQuestSectionNumber") ?: 1
            )
        }
    }
}