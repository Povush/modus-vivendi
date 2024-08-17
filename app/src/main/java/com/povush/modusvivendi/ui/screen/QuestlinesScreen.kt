package com.povush.modusvivendi.ui.screen

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.povush.modusvivendi.R
import com.povush.modusvivendi.data.datasource.povishQuests
import com.povush.modusvivendi.data.model.QuestlinesViewModel
import com.povush.modusvivendi.ui.screen.component.AppBarWithSectionsAndSearch
import com.povush.modusvivendi.ui.screen.component.QuestCard
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun QuestlinesScreen(
    viewModel: QuestlinesViewModel = viewModel()
) {
    Scaffold(
        topBar = {
            AppBarWithSectionsAndSearch(
                title = stringResource(R.string.questlines),
                onNavigationClicked = { /*TODO*/ }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding)
        ) {
            items(povishQuests) { quest ->
                QuestCard(
                    quest = quest,
                    changeTaskStatus = viewModel.changeTaskStatus()
                )
            }
        }
    }
}



