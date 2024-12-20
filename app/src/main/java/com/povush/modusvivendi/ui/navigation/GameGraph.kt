package com.povush.modusvivendi.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.povush.modusvivendi.R
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

object GameDestination : NavigationDestination {
    override val route = "game"
    override val titleRes = R.string.game
}

fun NavGraphBuilder.gameGraph(navController: NavController, onNavigationClick: () -> Unit) {
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

    navigation(
        route = QuestlinesGraphDestination.route,
        startDestination = QuestlinesDestination.route
    ) {
        questlinesGraph(navController, onNavigationClick)
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