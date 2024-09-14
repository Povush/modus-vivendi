package com.povush.modusvivendi.ui.questlines

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.povush.modusvivendi.R
import com.povush.modusvivendi.data.model.QuestType
import com.povush.modusvivendi.ui.AppViewModelProvider
import com.povush.modusvivendi.ui.appbar.ModusVivendiAppBar
import com.povush.modusvivendi.ui.navigation.NavigationDestination
import kotlinx.coroutines.launch

object QuestlinesDestination : NavigationDestination {
    override val route = "questlines"
    override val titleRes = R.string.questlines
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun QuestlinesScreen(
    navigateToQuestCreate: () -> Unit,
    viewModel: QuestlinesViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState by viewModel.uiState.collectAsState()

    val pagerState = rememberPagerState(
        initialPage = uiState.selectedQuestSection.ordinal,
        pageCount = { QuestType.entries.size }
    )
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            ModusVivendiAppBar(
                titleRes = QuestlinesDestination.titleRes,
                sections = QuestType.entries.map { it.textResId },
                navigation = {
                    IconButton(onClick = { /*TODO: Navigation*/ }) {
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
                        onClick = { /*TODO: Add quest, Collapse/expand, Sort by*/ }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.MoreVert,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
                selectedSection = pagerState.currentPage,
                onTabClicked = { index: Int ->
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                },
                tabCounter = { index -> viewModel.sectionCounter(index) }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {  },
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

        LaunchedEffect(pagerState.currentPage) {
            viewModel.switchQuestSection(pagerState.currentPage) // Update ViewModel when page changes
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) { page ->
            QuestSection(
                uiState = uiState.copy(selectedQuestSection = QuestType.entries[page]),
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
fun QuestSection(
    uiState: QuestlinesUiState,
    modifier: Modifier = Modifier
) {
    if (uiState.allQuests.none { it.type == uiState.selectedQuestSection }) {
        EmptyQuestSection(modifier = modifier)
    } else {
        NotEmptyQuestSection(uiState = uiState, modifier = modifier)
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
                Image(
                    painter = painterResource(R.drawable.img_empty_quest_section),
                    contentDescription = null,
                    modifier = Modifier
                        .size(200.dp)
                )
                Spacer(modifier = Modifier.size(16.dp))
                Text(
                    text = "Section is empty",
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = 26.sp,
                    fontFamily = FontFamily(
                        Font(R.font.avenir_next_bold)
                    ),
                    textAlign = TextAlign.Center,
                )
                Text(
                    text = "There is no quests in this section!",
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = 18.sp,
                    fontFamily = FontFamily(
                        Font(R.font.avenir_next_regular)
                    ),
                    textAlign = TextAlign.Center,
                )
                Spacer(modifier = Modifier.size(50.dp))
            }
        }
    }
}

@Composable
fun NotEmptyQuestSection(
    uiState: QuestlinesUiState,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(8.dp)
    ) {
        items(uiState.allQuests.filter { it.type == uiState.selectedQuestSection }) { quest ->
            QuestCard(quest = quest)
        }
    }
}