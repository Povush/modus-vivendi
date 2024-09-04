package com.povush.modusvivendi.ui.navigation

import android.content.res.Configuration
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.povush.modusvivendi.ui.screen.QuestlinesScreen
import com.povush.modusvivendi.ui.screen.component.MainParametersBar

enum class ModusVivendiScreens {
    QUESTLINES
}

@Composable
fun ModusVivendiApp(
    navController: NavHostController = rememberNavController()
) {
    val mainParametersBarOn = true

    Scaffold(
        modifier = Modifier,
        topBar = {
            if (LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT &&
                mainParametersBarOn) {
                /*TODO: Ability to disable the MainParametersBar*/
                MainParametersBar()
            }
        },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            /*TODO: The first screen should be remembered*/
            startDestination = ModusVivendiScreens.QUESTLINES.name,
            modifier = Modifier
                .padding(innerPadding)
        ) {
            composable(route = ModusVivendiScreens.QUESTLINES.name) {
                QuestlinesScreen(mainParametersBarOn = mainParametersBarOn)
            }
        }
    }
}
