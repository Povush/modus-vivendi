package com.povush.modusvivendi.ui.questlines

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddTask
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Castle
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.DragHandle
import androidx.compose.material.icons.filled.DragIndicator
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Task
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.ListItem
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonColors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.povush.modusvivendi.R
import com.povush.modusvivendi.data.model.Difficulty
import com.povush.modusvivendi.data.model.QuestType
import com.povush.modusvivendi.data.model.Task
import com.povush.modusvivendi.ui.AppViewModelProvider
import com.povush.modusvivendi.ui.appbar.ModusVivendiAppBar
import com.povush.modusvivendi.ui.createQuestCreateViewModelExtras
import com.povush.modusvivendi.ui.navigation.NavigationDestination
import com.povush.modusvivendi.ui.questlines.component.TaskItem
import com.povush.modusvivendi.ui.theme.NationalTheme

object QuestCreateDestination : NavigationDestination {
    override val route = "create_quest"
    override val titleRes = R.string.create_quest
}

@Composable
fun QuestCreateScreen(
    navigateBack: () -> Boolean,
    currentQuestSectionNumber: Int,
    viewModel: QuestCreateViewModel = viewModel(
        factory = AppViewModelProvider.Factory,
        extras = createQuestCreateViewModelExtras(currentQuestSectionNumber, LocalContext.current.applicationContext)
    )
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            ModusVivendiAppBar(
                titleRes = QuestCreateDestination.titleRes,
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
                        onClick = { /*TODO: Save quest*/ },
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
                    onCheckedTaskChange = { task, isCompleted -> viewModel.updateTaskStatus(task, isCompleted) },
                    onTaskTextChange = { input -> }
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
    tasks: Map<Task, List<Task>>,
    onCheckedTaskChange: (Task, Boolean) -> Unit,
    onTaskTextChange: (String) -> Unit
) {
    Column {
        Text(
            text = stringResource(R.string.tasks) + " (0/5)",
            modifier = Modifier.padding(8.dp),
            style = MaterialTheme.typography.bodySmall
        )
        tasks.forEach { (task, subtasks) ->
            QuestTask(
                task = task,
                isEdit = true,
                onCheckedChange = onCheckedTaskChange,
                onTaskTextChange = onTaskTextChange
            )
            subtasks.forEach { subtask ->
                QuestTask(
                    task = subtask,
                    isEdit = true,
                    onCheckedChange = onCheckedTaskChange,
                    onTaskTextChange = onTaskTextChange
                )
            }
        }
    }
}

@Composable
fun QuestTask(
    task: Task,
    isEdit: Boolean,
    onCheckedChange: (Task, Boolean) -> Unit,
    onTaskTextChange: (String) -> Unit,
    modifier: Modifier = Modifier
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
            modifier = Modifier
                .height(32.dp)
//                .clickable(onClick = {})
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = { offset ->

                        },
                        onDrag = { change, dragAmount ->

                        },
                        onDragEnd = {

                        },
                        onDragCancel = {

                        },
                    )
                }
        )
        Spacer(modifier = Modifier.size(4.dp))
        TaskItem(
            task = task,
            isEdit = isEdit,
            onCheckedChange = onCheckedChange,
            onTaskTextChange = onTaskTextChange
        )
    }
}

@Preview(showBackground = true)
@Composable
fun QuestCreateScreenPreview() {
    NationalTheme {
        com.povush.modusvivendi.ui.questlines.QuestTasks(
            tasks = mapOf(
                com.povush.modusvivendi.data.model.Task(id = 11, questId = 0, name = "Veeeeeeery-very-very loooooooooooooooooooooong task 1 and its huge description (add'l)") to emptyList(),
                com.povush.modusvivendi.data.model.Task(id = 12, questId = 0, name = "Task 2") to listOf(com.povush.modusvivendi.data.model.Task(id = 1, parentTaskId = 12, name = "Subtask 1", questId = 0), com.povush.modusvivendi.data.model.Task(id = 2, parentTaskId = 12, name = "Subtask 2", questId = 0)),
                com.povush.modusvivendi.data.model.Task(id = 13, questId = 0, name = "Task 3") to emptyList()
            ),
            onCheckedTaskChange = { _, _ -> },
            onTaskTextChange = {}
        )
    }
}