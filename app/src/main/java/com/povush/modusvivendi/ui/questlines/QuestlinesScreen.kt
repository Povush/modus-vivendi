package com.povush.modusvivendi.ui.questlines

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.unit.dp
import com.povush.modusvivendi.R
import com.povush.modusvivendi.ui.appbar.ModusVivendiAppBar
import androidx.lifecycle.viewmodel.compose.viewModel

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
                onTabClicked = { index: Int -> viewModel.onTabClick(index) },
                tabCounter = { index ->
                    viewModel.sectionCounter(index)
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(modifier = Modifier.padding(innerPadding)) {
            items(LocalQuestsDataProvider.allQuests) { quest ->
                QuestCard(
                    quest = quest,
                    changeQuestExpandStatus = { questId ->
                        viewModel.changeQuestExpandStatus(questId) },
                    changeTaskStatus = { changedQuest: Quest,changedTask: Task -> }
                )
            }
            item {
                Spacer(modifier = Modifier.size(6.dp))
            }
        }
    }
}