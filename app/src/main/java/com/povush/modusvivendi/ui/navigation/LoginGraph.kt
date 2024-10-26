package com.povush.modusvivendi.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.povush.modusvivendi.R
import com.povush.modusvivendi.ui.domain.DomainDestination
import com.povush.modusvivendi.ui.domain.DomainScreen
import com.povush.modusvivendi.ui.login.SignInDestination
import com.povush.modusvivendi.ui.login.SignInScreen
import com.povush.modusvivendi.ui.login.SignUpDestination
import com.povush.modusvivendi.ui.login.SignUpScreen
import com.povush.modusvivendi.ui.thoughtrealm.ThoughtrealmDestination
import com.povush.modusvivendi.ui.thoughtrealm.ThoughtrealmScreen

object LoginDestination : NavigationDestination {
    override val route = "login"
    override val titleRes = R.string.login
}

fun NavGraphBuilder.loginGraph(navController: NavController) {
    composable(route = SignInDestination.route) {
        SignInScreen(
            navigateBack = { navController.popBackStack() },
            navigateToSignUp = { navController.navigate(SignUpDestination.route) }
        )
    }

    composable(route = SignUpDestination.route) {
        SignUpScreen(navigateBack = { navController.popBackStack() })
    }
}