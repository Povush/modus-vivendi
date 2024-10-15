package com.povush.modusvivendi

import android.content.res.Configuration
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.povush.modusvivendi.ui.common.appbar.MainParameterData
import com.povush.modusvivendi.ui.common.appbar.MainParametersBar
import com.povush.modusvivendi.ui.domain.DomainDestination
import com.povush.modusvivendi.ui.navigation.ModusVivendiModalDrawerSheet
import com.povush.modusvivendi.ui.navigation.ModusVivendiNavHost
import com.povush.modusvivendi.ui.questlines.screens.QuestEditDestination
import com.povush.modusvivendi.ui.questlines.screens.QuestlinesDestination
import com.povush.modusvivendi.ui.skills.SkillsDestination
import com.povush.modusvivendi.ui.thoughtrealm.ThoughtrealmDestination
import com.povush.modusvivendi.ui.treasure.TreasureDestination
import kotlinx.coroutines.launch

@Composable
fun ModusVivendiApp(
    windowSize: WindowSizeClass,
    navController: NavHostController = rememberNavController()
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        modifier = Modifier,
        topBar = {
            if (LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT &&
                windowSize.widthSizeClass == WindowWidthSizeClass.Compact) {
                /*TODO: Remove the screen size check and simply deactivate the switch button
                   to turn on MainParametersBar in settings*/
                /*TODO: Ability to disable the MainParametersBar*/
                /*TODO: Make parameters interactive*/
                MainParametersBar(listOf(
                    MainParameterData(value = "4820",iconIdRes = R.drawable.ic_money)
                    { navController.navigate(TreasureDestination.route) },

                    MainParameterData(value = "240",iconIdRes = R.drawable.ic_development_5)
                    { navController.navigate(DomainDestination.route) },

                    MainParameterData(value = "105",iconIdRes = R.drawable.ic_willpower)
                    { navController.navigate(ThoughtrealmDestination.route) },

                    MainParameterData(value = "+2",iconIdRes = R.drawable.ic_stability)
                    { navController.navigate(ThoughtrealmDestination.route) },

                    MainParameterData(value = "86",iconIdRes = R.drawable.ic_crown_2)
                    { navController.navigate(ThoughtrealmDestination.route) },

                    MainParameterData(value = "37",iconIdRes = R.drawable.ic_innovativeness)
                    { navController.navigate(SkillsDestination.route) }
                ))
            }
        }
    ) { innerPadding ->
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = { ModusVivendiModalDrawerSheet(
                navController = navController,
                closeDrawerState = { coroutineScope.launch { drawerState.close() } }
            ) },
            modifier = Modifier.padding(innerPadding),
            gesturesEnabled = isNavigationGesturesEnabled(navController)
        ) {
            ModusVivendiNavHost(
                navController = navController,
                onNavigationClick = {
                    coroutineScope.launch {
                        drawerState.apply { if (isClosed) open() else close() }
                    }
                },
                modifier = Modifier
            )
        }
    }
}

@Composable
private fun isNavigationGesturesEnabled(navController: NavHostController): Boolean {
    val currentBackStackEntry = navController.currentBackStackEntryAsState().value
    val currentDestination = currentBackStackEntry?.destination?.route

    val isGesturesEnabled = mapOf(
        QuestlinesDestination.route to true,
        QuestEditDestination.route to false
    )

    return isGesturesEnabled[currentDestination] ?: true
}