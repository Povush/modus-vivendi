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
            startDestination = QuestlinesDestination.route
        ) {
            gameGraph(navController, onNavigationClick)
        }
    }
}

enum class ScaleTransitionDirection {
    INWARDS,
    OUTWARDS
}

fun scaleIntoContainer(
    direction: ScaleTransitionDirection = ScaleTransitionDirection.INWARDS,
    initialScale: Float = if (direction == ScaleTransitionDirection.OUTWARDS) 0.9f else 1.1f
): EnterTransition {
    return scaleIn(
        animationSpec = tween(220, delayMillis = 90),
        initialScale = initialScale
    ) + fadeIn(animationSpec = tween(220, delayMillis = 90))
}

fun scaleOutOfContainer(
    direction: ScaleTransitionDirection = ScaleTransitionDirection.OUTWARDS,
    targetScale: Float = if (direction == ScaleTransitionDirection.INWARDS) 0.9f else 1.1f
): ExitTransition {
    return scaleOut(
        animationSpec = tween(
            durationMillis = 220,
            delayMillis = 90
        ), targetScale = targetScale
    ) + fadeOut(tween(delayMillis = 90))
}