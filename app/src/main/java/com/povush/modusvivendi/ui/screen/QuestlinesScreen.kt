package com.povush.modusvivendi.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
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
        Column {
            ScrollableTabRow(
                selectedTabIndex = selectedTabIndex,
                modifier = Modifier.fillMaxWidth(),
                edgePadding = 16.dp
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = { Text(text = title) }
                    )
                }
            }

            // Content of the selected tab
            when (selectedTabIndex) {
                0 -> BasicText("Все чаты")
                1 -> BasicText("Личные чаты")
                2 -> BasicText("Рабочие чаты")
                3 -> BasicText("Избранные чаты")
                4 -> BasicText("Каналы")
            }

            LazyColumn(
                modifier = Modifier.padding(innerPadding)
            ) {
                items(uiState.quests) { quest ->
                    QuestCard(
                        quest = quest,
                        changeQuestExpandStatus = { currentQuest: Quest ->
                            viewModel.changeQuestExpandStatus(currentQuest)
                        },
                        changeTaskStatus = { currentQuest: Quest, currentTask: Task ->
                            viewModel.changeTaskStatus(currentQuest, currentTask)
                        }
                    )
                }
            }
        }
    }
}



