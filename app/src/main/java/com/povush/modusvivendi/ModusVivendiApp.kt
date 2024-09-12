package com.povush.modusvivendi

import android.content.res.Configuration
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.povush.modusvivendi.ui.appbar.MainParametersBar
import com.povush.modusvivendi.ui.navigation.ModusVivendiNavHost

@Composable
fun ModusVivendiApp(
    navController: NavHostController = rememberNavController()
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
            modifier = Modifier.padding(innerPadding)
        )
    }
}
