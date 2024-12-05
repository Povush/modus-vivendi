package com.povush.modusvivendi.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.povush.modusvivendi.R
import com.povush.modusvivendi.ui.questlines.screens.QuestEditScreen
import com.povush.modusvivendi.ui.questlines.screens.QuestlinesDestination
import com.povush.modusvivendi.ui.questlines.screens.QuestlinesScreen

object QuestlinesGraphDestination : NavigationDestination {
    override val route = "questlines_graph"
    override val titleRes = R.string.questlines_graph
}

fun NavGraphBuilder.questlinesGraph(navController: NavController, onNavigationClick: () -> Unit) {
    composable(route = QuestlinesDestination.route) {
        QuestlinesScreen(
            onNavigationClick = onNavigationClick,
            navigateToQuestEdit = { questId, currentQuestSectionNumber ->
                navController.navigate("edit_quest?questId=$questId&currentQuestSectionNumber=$currentQuestSectionNumber")
            }
        )
    }

    composable(
        route = "edit_quest?questId={questId}&currentQuestSectionNumber={currentQuestSectionNumber}",
        arguments = listOf(
            navArgument("questId") { type = NavType.LongType },
            navArgument("currentQuestSectionNumber") { type = NavType.IntType }
        ),
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
            navigateBack = { navController.popBackStack() }
        )
    }
}