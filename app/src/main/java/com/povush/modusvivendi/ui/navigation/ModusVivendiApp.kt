package com.povush.modusvivendi.ui.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
    Scaffold(
        modifier = Modifier,
        topBar = {
            /*TODO: Ability to disable the MainParametersBar*/
            Column {
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
                QuestlinesScreen()
            }
        }
    }
}

// aaabbbccc