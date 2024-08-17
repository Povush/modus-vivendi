package com.povush.modusvivendi.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.povush.modusvivendi.R
import com.povush.modusvivendi.data.model.QuestlinesViewModel
import com.povush.modusvivendi.ui.screen.component.AppBarWithSections
import com.povush.modusvivendi.ui.screen.component.QuestCard
import androidx.lifecycle.viewmodel.compose.viewModel
import com.povush.modusvivendi.data.dataclass.Quest
import com.povush.modusvivendi.data.dataclass.QuestSection
import com.povush.modusvivendi.data.dataclass.Task

@Composable
fun QuestlinesScreen(
    viewModel: QuestlinesViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            Box {
                AppBarWithSections(
                    title = stringResource(R.string.questlines),
                    onNavigationClicked = { /*TODO*/ }
                )
               Column {
                   val questSections = listOf(
                       QuestSection(R.string.main_quest_section, 0),
                       QuestSection(R.string.additional_quest_section, 0),
                       QuestSection(R.string.completed_quest_section, 0),
                       QuestSection(R.string.failed_quest_section, 0)
                   )

                   Spacer(modifier = Modifier.size(60.dp))
                   ScrollableTabRow(
                       selectedTabIndex = uiState.selectedQuestSectionIndex,
                       modifier = Modifier
                           .fillMaxWidth(),
                       containerColor = MaterialTheme.colorScheme.primary,
                       contentColor = MaterialTheme.colorScheme.onPrimary,
                       edgePadding = 4.dp
                   ) {
                       questSections.forEachIndexed { index, questSection ->
                           Tab(
                               selected = uiState.selectedQuestSectionIndex == index,
                               onClick = { viewModel.onTabClick(index) },
                               text = {
                                   Text(
                                       text = stringResource(id = questSection.name),
                                       color = MaterialTheme.colorScheme.onPrimary,
                                       fontSize = 18.sp,
                                       fontWeight = FontWeight.Normal,
                                       fontFamily = FontFamily(
                                           Font(R.font.carima)
                                       ),
                                       maxLines = 1,
                                       overflow = TextOverflow.Ellipsis,
                                       style = TextStyle(
                                           shadow = Shadow(
                                               color = Color.Black,
                                               offset = Offset(1f, 1f) ,
                                               blurRadius = 2f
                                           )
                                       )
                                   )
                               }
                           )
                       }
                   }

               }
            }
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {




            val questList = when (uiState.selectedQuestSectionIndex) {
                0 -> uiState.mainQuests
                1 -> uiState.additionalQuests
                2 -> uiState.completedQuests
                else -> uiState.failedQuests
            }

            LazyColumn {
                items(questList) { quest ->
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



