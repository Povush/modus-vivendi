package com.povush.modusvivendi.ui.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.navigation
import com.povush.modusvivendi.ui.login.SignInDestination
import com.povush.modusvivendi.ui.questlines.screens.QuestlinesDestination

@Composable
fun ModusVivendiNavHost(
    navController: NavHostController,
    onNavigationClick: () -> Unit,
    hasProfile: Boolean,
    modifier: Modifier = Modifier
) {
    val startDestination = if (hasProfile) GameDestination.route else LoginDestination.route

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None }
    ) {
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