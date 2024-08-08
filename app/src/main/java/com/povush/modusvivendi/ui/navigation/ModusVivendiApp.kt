package com.povush.modusvivendi.ui.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.povush.modusvivendi.ui.screen.QuestlinesScreen
import com.povush.modusvivendi.ui.screen.component.MainParametersBar

enum class ModusVivendiScreens(val title: String) {
    Questlines(title = "Questlines")
}

@Composable
fun ModusVivendiApp(
    navController: NavHostController = rememberNavController()
) {
    Scaffold(
        modifier = Modifier,
        topBar = {
            Column() {
                // Заменить Спейсер на нормальный отступ
                Spacer(modifier = Modifier.padding(17.dp))
                MainParametersBar(modifier = Modifier)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = ModusVivendiScreens.Questlines.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = ModusVivendiScreens.Questlines.name) {
                QuestlinesScreen()
            }
        }
    }
}
