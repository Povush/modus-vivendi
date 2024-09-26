package com.povush.modusvivendi

import android.content.res.Configuration
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.povush.modusvivendi.ui.appbar.MainParametersBar
import com.povush.modusvivendi.ui.navigation.ModusVivendiNavHost
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
                MainParametersBar()
            }
        }
    ) { innerPadding ->
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet(
                    modifier = Modifier.width(300.dp),
                    drawerShape = RectangleShape,
                    drawerContainerColor = MaterialTheme.colorScheme.primary
                ) {
                /*TODO: Drawer content */
                }
            },
            modifier = Modifier.padding(innerPadding),
            gesturesEnabled = true
        ) {
            ModusVivendiNavHost(
                navController = navController,
                onNavigationClick = {
                    coroutineScope.launch {
                        drawerState.apply {
                            if (isClosed) open() else close()
                        }
                    }
                },
                modifier = Modifier
            )
        }
    }
}