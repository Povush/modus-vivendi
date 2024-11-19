package com.povush.modusvivendi.ui.questlines.screens

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.povush.modusvivendi.R
import com.povush.modusvivendi.data.model.Quest
import com.povush.modusvivendi.data.model.QuestType
import com.povush.modusvivendi.data.model.QuestWithTasks
import com.povush.modusvivendi.data.model.Task
import com.povush.modusvivendi.data.model.TaskWithSubtasks
import com.povush.modusvivendi.ui.common.appbar.ModusVivendiAppBar
import com.povush.modusvivendi.ui.navigation.NavigationDestination
import com.povush.modusvivendi.ui.questlines.components.QuestCard
import com.povush.modusvivendi.ui.questlines.viewmodel.QuestlinesViewModel
import kotlinx.coroutines.launch

object QuestlinesDestination : NavigationDestination {
    override val route = "questlines"
    override val titleRes = R.string.questlines
}

@Composable
fun QuestlinesScreen(
    onNavigationClick: () -> Unit,
    navigateToQuestEdit: (Long?, Int?) -> Unit,
    viewModel: QuestlinesViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val pagerState = rememberPagerState(
        initialPage = uiState.selectedQuestSection.ordinal,
        pageCount = { QuestType.entries.size }
    )
    val coroutineScope = rememberCoroutineScope()

    var menuExpanded by remember { mutableStateOf(false) }

    LaunchedEffect(pagerState.currentPage) {
        if (uiState.selectedQuestSection.ordinal != pagerState.currentPage) {
            viewModel.switchQuestSection(pagerState.currentPage)
        }
    }

    LaunchedEffect(uiState.expandedStates) {
        viewModel.updateCollapseButton()
    }

    Scaffold(
        modifier = Modifier,
        topBar = {
            ModusVivendiAppBar(
                titleRes = QuestlinesDestination.titleRes,
                sections = QuestType.entries.map { it.textResId },
                navigation = {
                    IconButton(onClick = { onNavigationClick() }) {
                        Icon(
                            imageVector = Icons.Filled.Menu,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimary,
                        )
                    }
                },
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
                        onClick = viewModel::toggleExpandButton
                    ) {
                        Icon(
                            painter = if (uiState.collapseEnabled) {
                                painterResource(R.drawable.ic_collapse_all)
                            } else {
                                painterResource(R.drawable.ic_expand_all)
                            },
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                    IconButton(
                        onClick = { menuExpanded = true }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.MoreVert,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
                selectedSection = uiState.selectedQuestSection.ordinal,
                onTabClicked = { index: Int ->
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                },
                tabCounter = viewModel::sectionCounter
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navigateToQuestEdit(-1L, uiState.selectedQuestSection.ordinal) },
                modifier = Modifier.padding(8.dp),
                shape = CircleShape,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(
                    imageVector = Icons.Filled.Create,
                    contentDescription = null
                )
            }
        }
    ) { innerPadding ->
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            beyondViewportPageCount = 3,
        ) { page ->
            val currentPageQuests = uiState.allQuestsByType[QuestType.entries[page]] ?: emptyList()

            if (currentPageQuests.isNotEmpty()) {
                QuestSection(
                    quests = currentPageQuests,
                    allTasks = uiState.allTasksByQuestId,
                    expandedStates = uiState.expandedStates,
                    navigateToQuestEdit = navigateToQuestEdit,
                    changeQuestExpandStatus = viewModel::changeQuestExpandStatus,
                    deleteQuest = viewModel::deleteQuest,
                    updateTaskStatus = viewModel::updateTaskStatus,
                    completeQuest = viewModel::completeQuest,
                    checkCompletionStatus = viewModel::checkCompletionStatus
                )
            } else {
                EmptyQuestSection()
            }
        }
    }
}

@Composable
fun QuestSection(
    quests: List<Quest>,
    allTasks: Map<Long, List<TaskWithSubtasks>>,
    expandedStates: Map<Long, Boolean>,
    navigateToQuestEdit: (Long?, Int?) -> Unit,
    changeQuestExpandStatus: (Quest) -> Unit,
    deleteQuest: (Quest) -> Unit,
    updateTaskStatus: (Task, Boolean) -> Boolean,
    completeQuest: (Quest) -> Unit,
    checkCompletionStatus: (QuestWithTasks) -> Unit,
    modifier: Modifier = Modifier
) {
    val lazyListState = rememberLazyListState()

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        state = lazyListState,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        item { Spacer(modifier = Modifier.size(0.dp)) }                                             // Need for paddings by Arrangement.spacedBy
        items(quests) { quest ->
            key(quest.id) {
                QuestCard(
                    quest = quest,
                    tasks = allTasks[quest.id] ?: emptyList(),
                    isExpanded = expandedStates[quest.id] ?: false,
                    navigateToQuestEdit = navigateToQuestEdit,
                    changeQuestExpandStatus = changeQuestExpandStatus,
                    deleteQuest = deleteQuest,
                    updateTaskStatus = updateTaskStatus,
                    completeQuest = completeQuest,
                    checkCompletionStatus = checkCompletionStatus
                )
            }
        }
        item { Spacer(modifier = Modifier.size(0.dp)) }                                             // Need for paddings by Arrangement.spacedBy
    }
}

@Composable
fun EmptyQuestSection(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                val infiniteTransition = rememberInfiniteTransition(label = "Eeyore and his shadow")

                val offsetY by infiniteTransition.animateFloat(
                    initialValue = -5f,
                    targetValue = 5f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(3000, easing = LinearEasing),
                        repeatMode = RepeatMode.Reverse
                    ),
                    label = "Eeyore"
                )
                val scaleX by infiniteTransition.animateFloat(
                    initialValue = 0.95f,
                    targetValue = 1.05f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(3000, easing = LinearEasing),
                        repeatMode = RepeatMode.Reverse
                    ),
                    label = "Shadow"
                )

                Image(
                    painter = painterResource(R.drawable.img_empty_quest_section_9_eeyore),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .offset(y = offsetY.dp)
                        .width(200.dp)
                        .aspectRatio(1.18f)
                )
                Image(
                    painter = painterResource(R.drawable.img_empty_quest_section_9_shadow),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .graphicsLayer(scaleX = scaleX)
                        .width(200.dp)
                        .aspectRatio(7.27f)

                )
                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    text = stringResource(R.string.section_is_empty),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.headlineSmall.copy(fontSize = 28.sp)
                )
                Spacer(modifier = Modifier.size(4.dp))
                Text(
                    text = stringResource(R.string.no_quests_message),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp)
                )
                Spacer(modifier = Modifier.size(50.dp))
            }
        }
    }
}