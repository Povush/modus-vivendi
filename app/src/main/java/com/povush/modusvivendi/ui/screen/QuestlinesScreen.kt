package com.povush.modusvivendi.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.povush.modusvivendi.R
import com.povush.modusvivendi.data.model.QuestlinesViewModel
import com.povush.modusvivendi.ui.screen.component.ModusVivendiAppBar
import com.povush.modusvivendi.ui.screen.component.QuestCard
import androidx.lifecycle.viewmodel.compose.viewModel
import com.povush.modusvivendi.data.dataclass.Quest
import com.povush.modusvivendi.data.dataclass.QuestType
import com.povush.modusvivendi.data.dataclass.Task

@Composable
fun QuestlinesScreen(
    viewModel: QuestlinesViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            ModusVivendiAppBar(
                title = stringResource(R.string.questlines),
                onNavigationClicked = { /*TODO*/ },
                actions = {
                    IconButton(
                        onClick = { /*TODO: Search functionality*/ }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                    IconButton(
                        onClick = { /*TODO: Add quest, Collapse/expand, Sort by*/ }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.MoreVert,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
                sections = QuestType.entries.map { it.textResId },
                selectedSection = uiState.selectedQuestSection.ordinal,
                onTabClicked = { index: Int -> viewModel.onTabClick(index) }
            )
        }
    ) { innerPadding ->
        LazyColumn(modifier = Modifier.padding(innerPadding)) {
            items(uiState.quests[uiState.selectedQuestSection]?: emptyList()) { quest ->
                QuestCard(
                    quest = quest,
                    changeQuestExpandStatus = {  },
                    changeTaskStatus = { changedQuest: Quest, changedTask: Task -> }
                )
            }
        }
    }
}