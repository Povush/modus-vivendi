package com.povush.modusvivendi.ui.questlines.screens

import android.os.Build
import android.view.HapticFeedbackConstants
import android.view.View
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Castle
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.DragIndicator
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.povush.modusvivendi.R
import com.povush.modusvivendi.data.model.Difficulty
import com.povush.modusvivendi.data.model.QuestType
import com.povush.modusvivendi.data.model.Task
import com.povush.modusvivendi.data.model.TaskWithSubtasks
import com.povush.modusvivendi.ui.common.appbar.ModusVivendiAppBar
import com.povush.modusvivendi.ui.navigation.NavigationDestination
import com.povush.modusvivendi.ui.questlines.components.TaskEdit
import com.povush.modusvivendi.ui.questlines.viewmodel.QuestEditViewModel
import sh.calvin.reorderable.ReorderableColumn
import sh.calvin.reorderable.ReorderableScope

object QuestEditDestination : NavigationDestination {
    override val route = "edit_quest"
    override val titleRes = R.string.edit_quest

    val routeWithArgs = { questId: Long, currentQuestSectionNumber: Int ->
        "$route?questId=$questId&currentQuestSectionNumber=$currentQuestSectionNumber"
    }
}

@Composable
fun QuestEditScreen(
    navigateBack: () -> Boolean,
    viewModel: QuestEditViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.name, uiState.description, uiState.tasks) {
        viewModel.validate()
    }

    LaunchedEffect(uiState.tasks) {
        viewModel.checkQuestCompletionStatus()
    }

    Scaffold(
        topBar = {
            ModusVivendiAppBar(
                titleRes = if (viewModel.questId == -1L) R.string.create_quest else QuestEditDestination.titleRes,
                navigation = {
                    IconButton(onClick = { navigateBack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimary,
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { viewModel.saveQuestAndTasks { navigateBack() } },
                        enabled = uiState.isValid
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Done,
                            contentDescription = null,
                            tint = if (uiState.isValid) MaterialTheme.colorScheme.onPrimary
                            else MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f),
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .imePadding()
                .verticalScroll(rememberScrollState()),
        ) {
            Column(
                modifier = Modifier.padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                QuestTitle(
                    value = uiState.name,
                    onValueChange = { input -> viewModel.updateQuestName(input) }
                )
                QuestType(
                    expanded = uiState.typeExpanded,
                    onExpandedChange = { typeExpanded -> viewModel.updateTypeExpanded(typeExpanded) },
                    questType = uiState.type,
                    onQuestTypeChange = { questType -> viewModel.updateType(questType) }
                )
                QuestDifficulty(
                    difficulty = uiState.difficulty,
                    onDifficultyChange = { difficulty -> viewModel.updateDifficulty(difficulty) }
                )
                QuestDescription(
                    value = uiState.description,
                    onValueChange = { input -> viewModel.updateDescription(input) }
                )
                QuestTasks(
                    tasks = uiState.tasks,
                    onCheckedTaskChange = { task, isCompleted -> viewModel.onCheckedTaskChange(task, isCompleted) },
                    onTaskTextChange = { task, input -> viewModel.onTaskTextChange(task, input) },
                    onCreateSubtask = { task -> viewModel.createNewSubtask(task.id) },
                    onTaskDelete = { task -> viewModel.deleteTask(task) },
                    onReorderingTasks = { fromIndex, toIndex -> viewModel.onReorderingTasks(fromIndex, toIndex) },
                    onReorderingSubtasks = { parentTaskId, fromIndex, toIndex -> viewModel.onReorderingSubtasks(parentTaskId, fromIndex, toIndex) },
                    onCreateNewTaskButtonClicked = { viewModel.createNewTask() }
                )
            }
        }
    }
}

@Composable
fun QuestTitle(
    value: String,
    onValueChange: (String) -> Unit
) {
    val isFocused = remember { mutableStateOf(false) }

    TextField(
        value = value,
        onValueChange = { onValueChange(it) },
        placeholder = {
            if (!isFocused.value) {
                Text(
                    text = stringResource(R.string.quest_title),
                    style = MaterialTheme.typography.headlineSmall
                )
            }
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
            unfocusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
            disabledContainerColor = MaterialTheme.colorScheme.primaryContainer,
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .onFocusChanged { focusState ->
                isFocused.value = focusState.isFocused
            },
        textStyle = MaterialTheme.typography.headlineSmall,
        singleLine = true,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestType(
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    questType: QuestType,
    onQuestTypeChange: (QuestType) -> Unit
) {
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { onExpandedChange(it) }
    ) {
        TextField(
            value = stringResource(questType.textResId),
            onValueChange = { },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            readOnly = true,
            label = { Text(text = stringResource(R.string.quest_type)) },
            leadingIcon = {
                Icon(
                    imageVector = questType.iconImageVector,
                    contentDescription = null,
                    modifier = Modifier
                )
            },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
            textStyle = MaterialTheme.typography.headlineSmall
                .copy(fontSize = 16.sp, lineHeight = 18.sp)
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { onExpandedChange(false) },
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primaryContainer)
                .exposedDropdownSize()
        ) {
            QuestType.entries.forEach { currentQuestType ->
                DropdownMenuItem(
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = currentQuestType.iconImageVector,
                                contentDescription = null,
                                modifier = Modifier
                            )
                            Spacer(modifier = Modifier.size(16.dp))
                            Text(
                                text = stringResource(currentQuestType.textResId),
                                style = MaterialTheme.typography.headlineSmall
                                    .copy(fontSize = 16.sp, lineHeight = 18.sp)
                            )
                        }

                    },
                    onClick = {
                        onQuestTypeChange(currentQuestType)
                        onExpandedChange(false)
                    },
                    modifier = Modifier.height(40.dp),
                    trailingIcon = {
                        if (currentQuestType == questType) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                modifier = Modifier
                            )
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun QuestDifficulty(
    difficulty: Difficulty,
    onDifficultyChange: (Difficulty) -> Unit,
) {
    Column(modifier = Modifier.padding(bottom = 8.dp)) {
        Text(
            text = stringResource(R.string.difficulty),
            modifier = Modifier
                .padding(8.dp),
            style = MaterialTheme.typography.bodySmall
        )
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Difficulty.entries.forEach { currentDifficulty ->
                val isSelected = difficulty == currentDifficulty
                val color = if (isSelected) currentDifficulty.color
                else currentDifficulty.color.copy(alpha = 0.3f)

                Icon(
                    imageVector = Icons.Default.Castle,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(4.dp)
                        .clickable { onDifficultyChange(currentDifficulty) }
                        .weight(1f)
                        .fillMaxSize()
                )
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Difficulty.entries.forEach { currentDifficulty ->
                val isSelected = difficulty == currentDifficulty
                val color = if (isSelected) MaterialTheme.colorScheme.onBackground
                else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f)

                Text(
                    text = stringResource(currentDifficulty.textResId),
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.bodySmall.copy(lineHeight = 10.sp),
                    textAlign = TextAlign.Center,
                    color = color
                )
            }
        }
    }
}

@Composable
fun QuestDescription(
    value: String,
    onValueChange: (String) -> Unit
) {
    val isFocused = remember { mutableStateOf(false) }

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier
            .fillMaxWidth()
            .onFocusChanged { focusState ->
                isFocused.value = focusState.isFocused
            },
        textStyle = MaterialTheme.typography.bodyMedium,
        placeholder = {
            if (!isFocused.value) {
                Text(
                    text = stringResource(R.string.description),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        },
        minLines = 7,
        shape = RectangleShape
    )
}

@Composable
fun QuestTasks(
    tasks: List<TaskWithSubtasks>,
    onCheckedTaskChange: (Task, Boolean) -> Unit,
    onTaskTextChange: (Task, String) -> Unit,
    onCreateSubtask: (Task) -> Unit,
    onTaskDelete: (Task) -> Unit,
    onReorderingTasks: (Int, Int) -> Unit,
    onReorderingSubtasks: (Long, Int, Int) -> Unit,
    onCreateNewTaskButtonClicked: () -> Unit
) {
    val numberOfTasks: Int = tasks.size
    val numberOfCompletedTasks: Int = tasks.map { it.task }.filter { it.isCompleted }.size

    val view = LocalView.current

    Column {
        Text(
            text = stringResource(R.string.tasks) + " ($numberOfCompletedTasks/$numberOfTasks)",
            modifier = Modifier.padding(8.dp),
            style = MaterialTheme.typography.bodySmall
        )
        Surface(modifier = Modifier.padding(bottom = 4.dp)) {
            ReorderableColumn(
                list = tasks.map { it.task },
                onSettle = { fromIndex,toIndex ->
                    onReorderingTasks(fromIndex,toIndex)
                },
                onMove = {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                        view.performHapticFeedback(HapticFeedbackConstants.SEGMENT_FREQUENT_TICK)
                    } else {
                        view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
                    }
                }
            ) { taskIndex, task, isDragging ->
                key(task.id) {
                    val taskElevation by animateDpAsState(
                        if (isDragging) 4.dp else 0.dp,
                        label = "Dragging"
                    )
                    val scope = this

                    Surface(shadowElevation = taskElevation) {
                        Column {
                            QuestTask(
                                task = task,
                                onCheckedChange = onCheckedTaskChange,
                                onTaskTextChange = onTaskTextChange,
                                onCreateSubtask = onCreateSubtask,
                                onTaskDelete = onTaskDelete,
                                scope = scope,
                                view = view
                            )
                            Surface {
                                ReorderableColumn(
                                    list = tasks.find { it.task.id == task.id }?.subtasks ?: emptyList(),
                                    onSettle = { fromIndex,toIndex ->
                                        onReorderingSubtasks(task.id, fromIndex,toIndex)
                                    },
                                    onMove = {
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                                            view.performHapticFeedback(HapticFeedbackConstants.SEGMENT_FREQUENT_TICK)
                                        }
                                    }
                                ) { subtaskIndex, subtask, isDragging ->
                                    key(subtask.id) {
                                        val subtaskElevation by animateDpAsState(
                                            if (isDragging) 4.dp else 0.dp,
                                            label = "Dragging"
                                        )

                                        Surface(shadowElevation = subtaskElevation) {
                                            QuestTask(
                                                task = subtask,
                                                onCheckedChange = onCheckedTaskChange,
                                                onTaskTextChange = onTaskTextChange,
                                                onCreateSubtask = onCreateSubtask,
                                                onTaskDelete = onTaskDelete,
                                                scope = this,
                                                view = view
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        Button(
            onClick = { onCreateNewTaskButtonClicked() },
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            Text(text = stringResource(R.string.create_new_task), style = MaterialTheme.typography.bodyLarge)
        }
    }
}

@Composable
fun QuestTask(
    task: Task,
    onCheckedChange: (Task, Boolean) -> Unit,
    onTaskTextChange: (Task, String) -> Unit,
    onCreateSubtask: (Task) -> Unit,
    onTaskDelete: (Task) -> Unit,
    modifier: Modifier = Modifier,
    scope: ReorderableScope? = null,
    view: View? = null
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.DragIndicator,
            contentDescription = null,
            modifier = if (scope != null && view != null) {
                with(scope) {
                    Modifier.draggableHandle(
                        onDragStarted = {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                            view.performHapticFeedback(HapticFeedbackConstants.DRAG_START)
                            }
                        },
                        onDragStopped = {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                                view.performHapticFeedback(HapticFeedbackConstants.GESTURE_END)
                            }
                        },
                    )
                }
            } else Modifier
        )
        Spacer(modifier = Modifier.size(4.dp))
        TaskEdit(
            task = task,
            onCheckedChange = onCheckedChange,
            onTaskTextChange = onTaskTextChange,
            onCreateSubtask = onCreateSubtask,
            onTaskDelete = onTaskDelete
        )
    }
}