package com.povush.modusvivendi.ui.screen

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.povush.modusvivendi.R
import com.povush.modusvivendi.data.model.QuestlinesViewModel
import com.povush.modusvivendi.ui.screen.component.AppBarWithSections
import com.povush.modusvivendi.ui.screen.component.QuestCard
import androidx.lifecycle.viewmodel.compose.viewModel
import com.povush.modusvivendi.data.dataclass.Quest
import com.povush.modusvivendi.data.dataclass.Task

@Composable
fun QuestlinesScreen(
    viewModel: QuestlinesViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            AppBarWithSections(
                title = stringResource(R.string.questlines),
                onNavigationClicked = { /*TODO*/ }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding)
        ) {
            items(uiState.quests) { quest ->
                QuestCard(
                    quest = quest,
                    changeTaskStatus = { currentQuest: Quest, currentTask: Task ->
                        viewModel.changeTaskStatus(currentQuest, currentTask)
                    }
                )
            }
        }
    }
}



