package com.povush.modusvivendi

import android.content.res.Configuration
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.povush.modusvivendi.ui.appbar.MainParametersBar
import com.povush.modusvivendi.ui.navigation.ModusVivendiNavHost
import kotlinx.coroutines.launch

@Composable
fun ModusVivendiApp(
    navController: NavHostController = rememberNavController()
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet { /* Drawer content */ }
        },
    ) {
        Scaffold(
            modifier = Modifier,
            topBar = {
                if (LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT) {
                    /*TODO: Ability to disable the MainParametersBar*/
                    MainParametersBar()
                }
            }
        ) { innerPadding ->
            ModusVivendiNavHost(
                navController = navController,
                onNavigationClick = {
                    coroutineScope.launch {
                        drawerState.apply {
                            if (isClosed) open() else close()
                        }
                    }
                },
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}
