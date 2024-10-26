package com.povush.modusvivendi.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.povush.modusvivendi.ui.about_universe.AboutUniverseDestination
import com.povush.modusvivendi.ui.about_universe.AboutUniverseScreen
import com.povush.modusvivendi.ui.appearance.AppearanceDestination
import com.povush.modusvivendi.ui.appearance.AppearanceScreen
import com.povush.modusvivendi.ui.domain.DomainDestination
import com.povush.modusvivendi.ui.domain.DomainScreen
import com.povush.modusvivendi.ui.ecumene.EcumeneDestination
import com.povush.modusvivendi.ui.ecumene.EcumeneScreen
import com.povush.modusvivendi.ui.login.SignInDestination
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
import com.povush.modusvivendi.ui.splash.SplashDestination
import com.povush.modusvivendi.ui.splash.SplashScreen
import com.povush.modusvivendi.ui.technologies.TechnologiesDestination
import com.povush.modusvivendi.ui.technologies.TechnologiesScreen
import com.povush.modusvivendi.ui.thoughtrealm.ThoughtrealmDestination
import com.povush.modusvivendi.ui.thoughtrealm.ThoughtrealmScreen
import com.povush.modusvivendi.ui.treasure.TreasureDestination
import com.povush.modusvivendi.ui.treasure.TreasureScreen
import kotlinx.coroutines.delay
import kotlinx.serialization.Serializable

//@Serializable
//data object Login {
//    const val route = "login"
//}
//
//@Serializable
//data object Game {
//    const val route = "game"
//}

@Composable
fun ModusVivendiNavHost(
    navController: NavHostController,
    onNavigationClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ModusVivendiNavHostViewModel = hiltViewModel()
) {
    val hasProfile = viewModel.hasProfile

    NavHost(
        navController = navController,
        /*TODO: The first screen should be remembered*/
        startDestination = SplashDestination.route,
        modifier = modifier,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None }
    ) {
        composable(route = SplashDestination.route) {
            LaunchedEffect(hasProfile) {
                if (hasProfile) {
                    navController.navigate(GameDestination.route) {
                        popUpTo(SplashDestination.route) { inclusive = true }
                    }
                } else {
                    navController.navigate(LoginDestination.route) {
                        popUpTo(SplashDestination.route) { inclusive = true }
                    }
                }
            }
            SplashScreen()
        }

        navigation(
            route = LoginDestination.route,
            startDestination = SignInDestination.route
        ) {
            loginGraph(navController)
        }

        navigation(
            route = GameDestination.route,
            startDestination = QuestlinesDestination.route
        ) {
            gameGraph(navController, onNavigationClick)
        }
    }
}