package com.povush.modusvivendi.ui.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.navigation
import com.povush.core.ui.animations.ScaleTransitionDirection
import com.povush.core.ui.animations.scaleIntoContainer
import com.povush.core.ui.animations.scaleOutOfContainer
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
        enterTransition = {
            scaleIntoContainer()
        },
        exitTransition = {
            scaleOutOfContainer(direction = ScaleTransitionDirection.INWARDS)
        },
        popEnterTransition = {
            scaleIntoContainer(direction = ScaleTransitionDirection.OUTWARDS)
        },
        popExitTransition = {
            scaleOutOfContainer()
        }
    ) {
        navigation(
            route = LoginDestination.route,
            startDestination = SignInDestination.route
        ) {
            loginGraph(navController)
        }

        navigation(
            route = GameDestination.route,
            startDestination = QuestlinesGraphDestination.route
        ) {
            gameGraph(navController, onNavigationClick)
        }
    }
}