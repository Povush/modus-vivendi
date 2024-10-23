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
import com.povush.modusvivendi.ui.about_universe.AboutUniverseDestination
import com.povush.modusvivendi.ui.about_universe.AboutUniverseScreen
import com.povush.modusvivendi.ui.appearance.AppearanceDestination
import com.povush.modusvivendi.ui.appearance.AppearanceScreen
import com.povush.modusvivendi.ui.domain.DomainDestination
import com.povush.modusvivendi.ui.domain.DomainScreen
import com.povush.modusvivendi.ui.ecumene.EcumeneDestination
import com.povush.modusvivendi.ui.ecumene.EcumeneScreen
import com.povush.modusvivendi.ui.map.MapDestination
import com.povush.modusvivendi.ui.map.MapScreen
import com.povush.modusvivendi.ui.modifiers.ModifiersDestination
import com.povush.modusvivendi.ui.modifiers.ModifiersScreen
import com.povush.modusvivendi.ui.questlines.screens.QuestEditDestination
import com.povush.modusvivendi.ui.questlines.screens.QuestEditScreen
import com.povush.modusvivendi.ui.questlines.screens.QuestlinesDestination
import com.povush.modusvivendi.ui.questlines.screens.QuestlinesScreen
import com.povush.modusvivendi.ui.routine.RoutineDestination
import com.povush.modusvivendi.ui.routine.RoutineScreen
import com.povush.modusvivendi.ui.settings.SettingsDestination
import com.povush.modusvivendi.ui.settings.SettingsScreen
import com.povush.modusvivendi.ui.technologies.TechnologiesDestination
import com.povush.modusvivendi.ui.technologies.TechnologiesScreen
import com.povush.modusvivendi.ui.thoughtrealm.ThoughtrealmDestination
import com.povush.modusvivendi.ui.thoughtrealm.ThoughtrealmScreen
import com.povush.modusvivendi.ui.treasure.TreasureDestination
import com.povush.modusvivendi.ui.treasure.TreasureScreen

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
        composable(route = ThoughtrealmDestination.route) {
            ThoughtrealmScreen(onNavigationClick = onNavigationClick)
        }

        composable(route = DomainDestination.route) {
            DomainScreen(onNavigationClick = onNavigationClick)
        }

        composable(route = TreasureDestination.route) {
            TreasureScreen(onNavigationClick = onNavigationClick)
        }

        composable(route = TechnologiesDestination.route) {
            TechnologiesScreen(onNavigationClick = onNavigationClick)
        }

        composable(route = RoutineDestination.route) {
            RoutineScreen(onNavigationClick = onNavigationClick)
        }

        composable(route = AppearanceDestination.route) {
            AppearanceScreen(onNavigationClick = onNavigationClick)
        }

        composable(route = QuestlinesDestination.route) {
            QuestlinesScreen(
                onNavigationClick = onNavigationClick,
                navigateToQuestEdit = { questId, currentQuestSectionNumber ->
                    navController.currentBackStackEntry?.savedStateHandle?.set("questId", questId)
                    navController.currentBackStackEntry?.savedStateHandle?.set("currentQuestSectionNumber", currentQuestSectionNumber)
                    navController.navigate(QuestEditDestination.route)
                }
            )
        }
        composable(
            route = QuestEditDestination.route,
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

        composable(route = MapDestination.route) {
            MapScreen(onNavigationClick = onNavigationClick)
        }

        composable(route = EcumeneDestination.route) {
            EcumeneScreen(onNavigationClick = onNavigationClick)
        }

        composable(route = ModifiersDestination.route) {
            ModifiersScreen(onNavigationClick = onNavigationClick)
        }

        composable(route = SettingsDestination.route) {
            SettingsScreen(onNavigationClick = onNavigationClick)
        }

        composable(route = AboutUniverseDestination.route) {
            AboutUniverseScreen(onNavigationClick = onNavigationClick)
        }
    }
}