package com.povush.modusvivendi.ui.questlines.components

import android.view.HapticFeedbackConstants
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.povush.modusvivendi.R
import com.povush.modusvivendi.data.model.Quest
import com.povush.modusvivendi.ui.AppViewModelProvider
import com.povush.modusvivendi.ui.common.components.ModusVivendiDropdownMenuItem
import com.povush.modusvivendi.ui.createQuestViewModelExtras
import com.povush.modusvivendi.ui.questlines.viewmodel.QuestViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun QuestCard(
    quest: Quest,
    navigateToQuestEdit: (Long?, Int?) -> Unit,
    viewModel: QuestViewModel = viewModel(
        factory = AppViewModelProvider.Factory,
        key = "questViewModel_${quest.id}",
        extras = createQuestViewModelExtras(quest, LocalContext.current.applicationContext)
    )
) {
    val uiState by viewModel.uiState.collectAsState()
    val view = LocalView.current

    var menuExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(),
        shape = RoundedCornerShape(8.dp),
        colors = CardColors(
            containerColor = Color.Transparent,
            contentColor = Color.Black,
            disabledContainerColor = Color.Transparent,
            disabledContentColor = Color.Black
        ),
    ) {
        Surface(
            modifier = Modifier
                .wrapContentSize()
                .graphicsLayer {
                    shadowElevation = 4.dp.toPx()
                    shape = shape
                    clip = false
                    translationX = -4f
                    translationY = -4f
                },
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .combinedClickable(
                        onClick = { viewModel.changeQuestExpandStatus() },
                        onLongClick = {
                            view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
                            menuExpanded = true
                        }
                    )
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xFFFFFCF2),
                                MaterialTheme.colorScheme.primaryContainer
                            )
                        )
                    )
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(
                    text = quest.name,
                    modifier = Modifier,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 2,
                    style = MaterialTheme.typography.headlineSmall
                )
                Text(
                    text = stringResource(
                        R.string.quest_difficulty,
                        stringResource(id = quest.difficulty.textResId)
                    ).uppercase(),
                    modifier = Modifier,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = quest.difficulty.color,
                        fontWeight = FontWeight.SemiBold,
                        shadow = Shadow(
                            color = Color.Black,
                            offset = Offset(0.05f, 0.05f),
                            blurRadius = 0.15f
                        )
                    )
                )
            }
        }
        DropdownMenu(
            expanded = menuExpanded,
            onDismissRequest = { menuExpanded = false },
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primaryContainer)
                .wrapContentHeight()
        ) {
            ModusVivendiDropdownMenuItem(R.string.edit) {
                menuExpanded = false
                navigateToQuestEdit(quest.id, null)
            }
            ModusVivendiDropdownMenuItem(R.string.delete) {
                menuExpanded = false
                viewModel.deleteQuest()
            }
        }
        if (uiState.isExpanded) {
            Text(
                text = quest.description,
                modifier = Modifier.padding(8.dp),
                style = MaterialTheme.typography.bodyMedium
            )
            uiState.tasks.forEach { taskWithSubtasks ->
                TaskDisplay(taskWithSubtasks)
                { task, isCompleted -> viewModel.updateTaskStatus(task, isCompleted) }
            }
            Spacer(modifier = Modifier.size(8.dp))
        }
    }
}