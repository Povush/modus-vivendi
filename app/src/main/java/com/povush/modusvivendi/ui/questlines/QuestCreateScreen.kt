package com.povush.modusvivendi.ui.questlines

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.DragHandle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.povush.modusvivendi.R
import com.povush.modusvivendi.data.model.Difficulty
import com.povush.modusvivendi.data.model.QuestType
import com.povush.modusvivendi.data.model.Subtask
import com.povush.modusvivendi.data.model.Task
import com.povush.modusvivendi.ui.AppViewModelProvider
import com.povush.modusvivendi.ui.appbar.ModusVivendiAppBar
import com.povush.modusvivendi.ui.createQuestCreateViewModelExtras
import com.povush.modusvivendi.ui.navigation.NavigationDestination
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
                    IconButton(onClick = { /*TODO: Save quest*/ }) {
                        Icon(
                            imageVector = Icons.Filled.Done,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimary,
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(8.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(8.dp)
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
                tasks = mapOf(
                    com.povush.modusvivendi.data.model.Task(id = 11, questId = 0, name = "Veeeeeeery loooooooooooooooooooooong task 1") to emptyList(),
                    com.povush.modusvivendi.data.model.Task(id = 12, questId = 0, name = "Task 2") to listOf(Subtask(id = 1, taskId = 12, name = "Subtask 1"), Subtask(id = 2, taskId = 12, name = "Subtask 2"))
                ),
                onCheckedTaskChange = { task -> viewModel.updateTaskStatus(task) },
                onCheckedSubtaskChange = { subtask -> viewModel.updateSubtaskStatus(subtask) },
                onTaskTextChange = { input -> }
            )
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
        label = {
            Text(
                text = stringResource(R.string.quest_title),
                style = if (value.isEmpty() && !isFocused.value) {
                    MaterialTheme.typography.headlineSmall
                } else {
                    LocalTextStyle.current
                }
            )
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
            unfocusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
            disabledContainerColor = MaterialTheme.colorScheme.primaryContainer,
        ),
        modifier = Modifier
            .fillMaxWidth()
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
        onExpandedChange = { onExpandedChange(it) },
        modifier = Modifier.padding(horizontal = 4.dp)
    ) {
        TextField(
            value = stringResource(questType.textResId),
            onValueChange = { },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            readOnly = true,
            label = { Text(stringResource(R.string.quest_type)) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Checklist,
                    contentDescription = null,
                    modifier = Modifier
                )
            },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = ExposedDropdownMenuDefaults.textFieldColors()
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
                    text = { Text(stringResource(currentQuestType.textResId)) },
                    onClick = {
                        onQuestTypeChange(currentQuestType)
                        onExpandedChange(false)
                    },
                    modifier = Modifier,
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
    Column {
        Text(
            text = stringResource(R.string.difficulty),
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .padding(top = 8.dp),
            style = MaterialTheme.typography.bodySmall
        )
        Row(modifier = Modifier.fillMaxWidth()) {
            Difficulty.entries.forEach { currentDifficulty ->
                RadioButton(
                    selected = difficulty == currentDifficulty,
                    onClick = { onDifficultyChange(currentDifficulty) },
                    modifier = Modifier
                        .size(36.dp)
                        .weight(1f),
                    colors = RadioButtonColors(
                        selectedColor = currentDifficulty.color,
                        unselectedColor = currentDifficulty.color.copy(alpha = 0.5f),
                        disabledSelectedColor = currentDifficulty.color,
                        disabledUnselectedColor = currentDifficulty.color.copy(alpha = 0.5f)
                    )
                )
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Difficulty.entries.forEach { currentDifficulty ->
                Text(
                    text = stringResource(currentDifficulty.textResId),
                    modifier = Modifier.weight(1f),
                    color = if (difficulty == currentDifficulty) currentDifficulty.color
                    else currentDifficulty.color.copy(alpha = 0.5f),
                    fontSize = 14.sp,
                    lineHeight = 14.sp,
                    textAlign = TextAlign.Center
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
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth(),
        textStyle = MaterialTheme.typography.bodyMedium,
        label = { Text(text = stringResource(R.string.description)) },
        minLines = 7
    )
}

@Composable
fun QuestTasks(
    tasks: Map<Task, List<Subtask>>,
    onCheckedTaskChange: (Task) -> Unit,
    onCheckedSubtaskChange: (Subtask) -> Unit,
    onTaskTextChange: (String) -> Unit
) {
    Column {
        Text(
            text = stringResource(R.string.tasks),
            modifier = Modifier.padding(8.dp),
            style = MaterialTheme.typography.bodySmall
        )
        tasks.forEach { (task, subtasks) ->
            QuestTask(
                task = task,
                onCheckedChange = onCheckedTaskChange,
                onTaskTextChange = onTaskTextChange
            )
            subtasks.forEach { subtask ->
                QuestSubtask(
                    subtask = subtask,
                    onCheckedChange = onCheckedSubtaskChange,
                    onTaskTextChange = onTaskTextChange,
                    modifier = Modifier.padding(start = 48.dp)
                )
            }
        }
    }
}

@Composable
fun QuestTask(
    task: Task,
    onCheckedChange: (Task) -> Unit,
    onTaskTextChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth()
    ) {
        Icon(
            imageVector = Icons.Default.DragHandle,
            contentDescription = null,
            modifier = Modifier
                .height(32.dp)
                .clickable(onClick = {})
        )
        Checkbox(
            checked = task.isCompleted,
            onCheckedChange = { onCheckedChange(task) },
            modifier = Modifier.size(32.dp)
        )
        DynamicPaddingBasicTextField(
            value = task.name,
            onValueChange = { input -> onTaskTextChange(input) },
            modifier = Modifier.weight(1f)
        )
        Icon(
            imageVector = Icons.Default.MoreVert,
            contentDescription = null,
            modifier = Modifier
                .height(32.dp)
                .clickable(onClick = {})
        )
    }
}

@Composable
fun QuestSubtask(
    subtask: Subtask,
    onCheckedChange: (Subtask) -> Unit,
    onTaskTextChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth()
    ) {
        Icon(
            imageVector = Icons.Default.DragHandle,
            contentDescription = null,
            modifier = Modifier
                .height(32.dp)
                .clickable(onClick = {})
        )
        Checkbox(
            checked = subtask.isCompleted,
            onCheckedChange = { onCheckedChange(subtask) },
            modifier = Modifier.size(32.dp)
        )
        DynamicPaddingBasicTextField(
            value = subtask.name,
            onValueChange = { input -> onTaskTextChange(input) },
            modifier = Modifier.weight(1f)
        )
        Icon(
            imageVector = Icons.Default.MoreVert,
            contentDescription = null,
            modifier = Modifier
                .height(32.dp)
                .clickable(onClick = {})
        )
    }
}

@Composable
fun DynamicPaddingBasicTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var lineCount by remember { mutableIntStateOf(0) }

    val paddingValues = when (lineCount) {
        1 -> PaddingValues(top = 8.dp)
        else -> PaddingValues(top = 4.dp)
    }

    BasicTextField(
        value = value,
        onValueChange = { onValueChange(it) },
        modifier = modifier.padding(paddingValues),
        onTextLayout = { textLayoutResult: TextLayoutResult ->
            lineCount = textLayoutResult.lineCount
        },
        textStyle = MaterialTheme.typography.bodyLarge
    )
}

@Preview(showBackground = true)
@Composable
fun QuestCreateScreenPreview() {
    NationalTheme {
        com.povush.modusvivendi.ui.questlines.QuestTasks(
            tasks = mapOf(
                com.povush.modusvivendi.data.model.Task(id = 11, questId = 0, name = "Veeeeeeery loooooooooooooooooooooong task 1") to emptyList(),
                com.povush.modusvivendi.data.model.Task(id = 12, questId = 0, name = "Task 2") to listOf(Subtask(id = 1, taskId = 12, name = "Subtask 1"), Subtask(id = 2, taskId = 12, name = "Subtask 2"))
            ),
            onCheckedTaskChange = {},
            onCheckedSubtaskChange = {},
            onTaskTextChange = {}
        )
    }
}