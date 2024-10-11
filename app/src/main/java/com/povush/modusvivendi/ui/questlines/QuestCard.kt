package com.povush.modusvivendi.ui.questlines

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.povush.modusvivendi.R
import com.povush.modusvivendi.data.model.Quest
import com.povush.modusvivendi.data.model.Task
import com.povush.modusvivendi.ui.AppViewModelProvider
import com.povush.modusvivendi.ui.createQuestViewModelExtras
import com.povush.modusvivendi.ui.questlines.components.TaskItem

@Composable
fun QuestCard(
    quest: Quest,
    viewModel: QuestViewModel = viewModel(
        factory = AppViewModelProvider.Factory,
        key = "questViewModel_${quest.id}",
        extras = createQuestViewModelExtras(quest, LocalContext.current.applicationContext)
    )
) {
    val uiState by viewModel.uiState.collectAsState()

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
        Column(
            modifier = Modifier
                .fillMaxWidth()
//                .graphicsLayer {
//                    shadowElevation = 2.dp.toPx()
//                    shape = CutCornerShape(8.dp)
//                    clip = true
//                }
                .clickable { viewModel.changeQuestExpandStatus() }
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
                text = uiState.quest.name,
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
                        offset = Offset(0.10f, 0.10f),
                        blurRadius = 0.30f
                    )
                )
            )
        }
        if (uiState.expanded) {
            Text(
                text = quest.description,
                modifier = Modifier.padding(8.dp),
                style = MaterialTheme.typography.bodyMedium
            )
            Tasks(
                tasks = uiState.tasks,
                onCheckedChange = { task, isCompleted -> viewModel.updateTaskStatus(task, isCompleted) }
            )
        }
    }
}

@Composable
fun Tasks(
    tasks: List<Task>,
    onCheckedChange: (Task, Boolean) -> Unit
) {
    Column(
        modifier = Modifier.padding(4.dp)
    ) {
        tasks.forEach { task ->
            TaskItem(
                task = task,
                isEdit = false,
                onCheckedChange = onCheckedChange,
                onTaskTextChange = { _, _ -> }
            )
        }
    }
}