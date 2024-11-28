package com.povush.modusvivendi

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.povush.modusvivendi.ui.common.appbar.MainParameterData
import com.povush.modusvivendi.ui.common.appbar.MainParametersBar
import com.povush.modusvivendi.ui.domain.DomainDestination
import com.povush.modusvivendi.ui.login.SignInDestination
import com.povush.modusvivendi.ui.login.SignUpDestination
import com.povush.modusvivendi.ui.map.MapDestination
import com.povush.modusvivendi.ui.navigation.LoginDestination
import com.povush.modusvivendi.ui.navigation.ModusVivendiModalDrawerSheet
import com.povush.modusvivendi.ui.navigation.ModusVivendiNavHost
import com.povush.modusvivendi.ui.technologies.TechnologiesDestination
import com.povush.modusvivendi.ui.thoughtrealm.ThoughtrealmDestination
import com.povush.modusvivendi.ui.treasure.TreasureDestination
import kotlinx.coroutines.launch

@Composable
fun ModusVivendiApp(
    windowSize: WindowSizeClass,
    hasProfile: Boolean,
    navController: NavHostController = rememberNavController()
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        modifier = Modifier,
        topBar = {
            if (LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT &&
                windowSize.widthSizeClass == WindowWidthSizeClass.Compact &&
                isMainParametersBarDisplayed(navController)) {
                /*TODO: Remove the screen size check and simply deactivate the switch button
                   to turn on MainParametersBar in settings*/
                /*TODO: Ability to disable the MainParametersBar*/
                /*TODO: Make parameters interactive*/
                MainParametersBar(
                    mainParameters = listOf(
                        MainParameterData(value = "4820",iconIdRes = R.drawable.ic_money),
                        MainParameterData(value = "240",iconIdRes = R.drawable.ic_development_5),
                        MainParameterData(value = "105",iconIdRes = R.drawable.ic_willpower),
                        MainParameterData(value = "+2",iconIdRes = R.drawable.ic_stability),
                        MainParameterData(value = "86",iconIdRes = R.drawable.ic_crown_2),
                        MainParameterData(value = "37",iconIdRes = R.drawable.ic_innovativeness)
                    ),
                    onParameterClicked = { navigationDestination ->
                        coroutineScope.launch { drawerState.close() }
                        navController.navigate(navigationDestination.route)
                    }
                )
            }
        }
    ) { innerPadding ->
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = { ModusVivendiModalDrawerSheet(
                navController = navController,
                closeDrawerState = { coroutineScope.launch { drawerState.close() } },
                navigateToLogin = {
                    coroutineScope.launch {
                        drawerState.close()
                    }
                    navController.navigate(LoginDestination.route) {
                        popUpTo(LoginDestination.route) { inclusive = true }
                    }
                },
                currentDestination = navController.currentDestination?.route
            ) },
            modifier = Modifier.padding(innerPadding),
            gesturesEnabled = isModalNavigationGesturesEnabled(navController) || drawerState.isOpen
        ) {
            Box {
                Spacer(                                                                             // For more natural animation transitions between navigation
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .background(MaterialTheme.colorScheme.primary)
                )
                ModusVivendiNavHost(
                    navController = navController,
                    onNavigationClick = {
                        coroutineScope.launch {
                            drawerState.apply { if (isClosed) open() else close() }
                        }
                    },
                    hasProfile = hasProfile,
                    modifier = Modifier
                )
            }

        }
    }
}

@Composable
private fun isModalNavigationGesturesEnabled(navController: NavHostController): Boolean {
    val currentBackStackEntry = navController.currentBackStackEntryAsState().value
    val currentDestination = currentBackStackEntry?.destination?.route

    val isGesturesEnabled = mapOf(
        "edit_quest?questId={questId}&currentQuestSectionNumber={currentQuestSectionNumber}" to false,
        SignInDestination.route to false,
        SignUpDestination.route to false,
        MapDestination.route to false
    )

    return isGesturesEnabled[currentDestination] ?: true
}

@Composable
private fun isMainParametersBarDisplayed(navController: NavHostController): Boolean {
    val currentBackStackEntry = navController.currentBackStackEntryAsState().value
    val currentDestination = currentBackStackEntry?.destination?.route

    val isDisplayed = mapOf(
        "edit_quest?questId={questId}&currentQuestSectionNumber={currentQuestSectionNumber}" to false,
        SignInDestination.route to false,
        SignUpDestination.route to false
    )

    return isDisplayed[currentDestination] ?: true
}