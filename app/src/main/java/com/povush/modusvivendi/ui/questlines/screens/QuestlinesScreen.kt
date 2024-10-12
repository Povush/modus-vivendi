package com.povush.modusvivendi.ui.questlines.screens

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.povush.modusvivendi.R
import com.povush.modusvivendi.data.model.Quest
import com.povush.modusvivendi.data.model.QuestType
import com.povush.modusvivendi.ui.AppViewModelProvider
import com.povush.modusvivendi.ui.appbar.ModusVivendiAppBar
import com.povush.modusvivendi.ui.navigation.NavigationDestination
import com.povush.modusvivendi.ui.questlines.components.QuestCard
import com.povush.modusvivendi.ui.questlines.viewmodel.QuestlinesViewModel
import kotlinx.coroutines.launch

object QuestlinesDestination : NavigationDestination {
    override val route = "questlines"
    override val titleRes = R.string.questlines
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun QuestlinesScreen(
    onNavigationClick: () -> Unit,
    navigateToQuestEdit: (Long?, Int?) -> Unit,
    viewModel: QuestlinesViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    var menuExpanded by remember { mutableStateOf(false) }

    val uiState by viewModel.uiState.collectAsState()
    val pagerState = rememberPagerState(
        initialPage = uiState.selectedQuestSection.ordinal,
        pageCount = { QuestType.entries.size }
    )
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(pagerState.currentPage) {
        if (uiState.selectedQuestSection.ordinal != pagerState.currentPage) {
            viewModel.switchQuestSection(pagerState.currentPage)
        }
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
                        onClick = { menuExpanded = true }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.MoreVert,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
//                    DropdownMenu(
//                        expanded = menuExpanded,
//                        onDismissRequest = { menuExpanded = false }
//                    ) {
//                        DropdownMenuItem(
//                            text = { Text("Option 1") },
//                            onClick = { menuExpanded = false }
//                        )
//                    }
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
                onClick = { navigateToQuestEdit(null, uiState.selectedQuestSection.ordinal) },
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
//        var userScrollEnabled by remember { mutableStateOf(true) }
//
//        val nestedScrollConnection = remember {
//            object : NestedScrollConnection {
//                override fun onPreScroll(available: Offset,source: NestedScrollSource): Offset {
//                    if (available.x > 0f && pagerState.currentPage == 0) {
//                        return Offset.Zero
//                    } else {
//                        return super.onPreScroll(available, source)
//                    }
//                }
//            }
//        }
//
//        LaunchedEffect(pagerState.isScrollInProgress) {
//            if (!pagerState.isScrollInProgress) {
//                userScrollEnabled = true
//            }
//        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            beyondBoundsPageCount = 3,
            userScrollEnabled = true
        ) { page ->
            QuestContent(
                quests = uiState.allQuestsByType[QuestType.entries[page]] ?: emptyList(),
                navigateToQuestEdit = navigateToQuestEdit,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
fun QuestContent(
    quests: List<Quest>,
    navigateToQuestEdit: (Long?, Int?) -> Unit,
    modifier: Modifier = Modifier
) {
    if (quests.isNotEmpty()) {
        QuestSection(quests = quests, navigateToQuestEdit = navigateToQuestEdit, modifier = modifier)
    } else {
        EmptyQuestSection(modifier = modifier)
    }
}

@Composable
fun EmptyQuestSection(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier,
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

@Composable
fun QuestSection(
    quests: List<Quest>,
    navigateToQuestEdit: (Long?, Int?) -> Unit,
    modifier: Modifier = Modifier
) {
    val lazyListState = rememberLazyListState()

    LazyColumn(
        modifier = modifier,
        state = lazyListState,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(8.dp)
    ) {
        items(quests) { quest ->
            key(quest.id) {
                QuestCard(quest = quest, navigateToQuestEdit = navigateToQuestEdit)
            }
        }
    }
}