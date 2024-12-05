package com.povush.modusvivendi.ui.questlines.components

import android.view.HapticFeedbackConstants
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.povush.modusvivendi.R
import com.povush.modusvivendi.data.model.Difficulty
import com.povush.modusvivendi.data.model.Quest
import com.povush.modusvivendi.data.model.QuestType
import com.povush.modusvivendi.data.model.QuestWithTasks
import com.povush.modusvivendi.data.model.Task
import com.povush.modusvivendi.data.model.TaskWithSubtasks
import com.povush.modusvivendi.ui.common.components.ModusVivendiDropdownMenuItem
import com.povush.modusvivendi.ui.shimmerEffect
import kotlinx.coroutines.delay

@Composable
fun QuestCard(
    quest: Quest,
    tasks: List<TaskWithSubtasks>,
    isExpanded: Boolean,
    navigateToQuestEdit: (Long?, Int?) -> Unit,
    changeQuestExpandStatus: (Quest) -> Unit,
    deleteQuest: (Quest) -> Unit,
    updateTaskStatus: (Task, Boolean, List<Task>?) -> Boolean,
    completeQuest: (Quest) -> Unit,
    checkCompletionStatus: (QuestWithTasks) -> Unit,
    modifier: Modifier = Modifier
) {
    val view = LocalView.current
    val canBeCompleted = quest.isCompleted && quest.type != QuestType.COMPLETED && quest.type != QuestType.FAILED

    var menuExpanded by remember { mutableStateOf(false) }

    LaunchedEffect(tasks) {
        checkCompletionStatus(QuestWithTasks(quest, tasks))
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize()
    ) {
        Surface(
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .wrapContentSize()
        ) {
            Card(
                shape = RoundedCornerShape(8.dp),
                colors = CardColors(
                    containerColor = Color.Transparent,
                    contentColor = MaterialTheme.colorScheme.onBackground,
                    disabledContainerColor = Color.Transparent,
                    disabledContentColor = MaterialTheme.colorScheme.onBackground
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .combinedClickable(
                        onClick = { changeQuestExpandStatus(quest) },
                        onLongClick = {
                            view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
                            menuExpanded = true
                        }
                    )
                    .graphicsLayer {
                        shadowElevation = 4.dp.toPx()
                        shape = RoundedCornerShape(8.dp)
                        clip = true
                        translationX = -4f
                        translationY = -4f
                    }
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xFFFFFCF2),
                                MaterialTheme.colorScheme.primaryContainer
                            )
                        )
                    )
            ) {
                Column(modifier = Modifier.padding(top = 10.dp, bottom = 6.dp, start = 8.dp, end = 8.dp)) {
                    Text(
                        text = quest.name,
                        modifier = Modifier,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 2,
                        style = MaterialTheme.typography.headlineSmall.copy(
//                            fontFamily = FontFamily(
//                                Font(R.font.freeride)
//                            ),
//                            fontWeight = FontWeight.Bold
                        )
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
        }
        DropdownMenu(
            expanded = menuExpanded,
            onDismissRequest = { menuExpanded = false },
            modifier = Modifier
                .background(Color.White)
                .wrapContentSize(),
            shape = RoundedCornerShape(8.dp),
            shadowElevation = 8.dp,
            offset = DpOffset(x = 8.dp, y = 0.dp)
        ) {
            ModusVivendiDropdownMenuItem(
                textRes = R.string.edit,
                onClick = {
                    menuExpanded = false
                    navigateToQuestEdit(quest.id, -1)
                },
                leadingIcon = Icons.Outlined.Edit
            )
            ModusVivendiDropdownMenuItem(
                textRes = R.string.delete,
                onClick = {
                    menuExpanded = false
                    deleteQuest(quest)
                },
                leadingIcon = Icons.Outlined.DeleteOutline,
                isDangerous = true
            )
        }
        if (isExpanded) {
            Text(
                text = quest.description,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                style = MaterialTheme.typography.bodyMedium
            )
            tasks.sortedBy { it.task.orderIndex }.forEach { taskWithSubtasks ->
                key(taskWithSubtasks.task.id) {
                    TaskDisplay(
                        taskWithSubtasks = taskWithSubtasks,
                        onCheckedChange = { task, isCompleted, subtasks -> updateTaskStatus(task, isCompleted, subtasks) },
                        isEnabled = quest.type != QuestType.FAILED && quest.type != QuestType.COMPLETED
                    )
                }
            }
            if (canBeCompleted) {
                Spacer(modifier = Modifier.size(8.dp))
                Button(
                    onClick = { completeQuest(quest) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(horizontal = 8.dp)
                        .shimmerEffect(
                            brighterColor = Color(0xFF00C95A),
                            darkerColor = Color(0xFF00B050),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .height(40.dp),
                    colors = ButtonColors(
                        containerColor = Color.Transparent,
                        contentColor = Color.White,
                        disabledContainerColor = Color.Transparent,
                        disabledContentColor = Color.White
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(text = stringResource(R.string.complete_quest), style = MaterialTheme.typography.bodyLarge)
                }
            }
            Spacer(modifier = Modifier.size(4.dp))
        }
    }
}