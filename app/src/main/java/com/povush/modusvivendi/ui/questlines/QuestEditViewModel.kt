package com.povush.modusvivendi.ui.questlines

import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.povush.modusvivendi.data.model.Difficulty
import com.povush.modusvivendi.data.model.Quest
import com.povush.modusvivendi.data.model.QuestType
import com.povush.modusvivendi.data.model.Task
import com.povush.modusvivendi.data.model.TaskWithSubtasks
import com.povush.modusvivendi.data.repository.OfflineQuestsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class QuestEditUiState(
    val isValid: Boolean = false,
    val name: String = "",
    val type: QuestType = QuestType.ADDITIONAL,
    val typeExpanded: Boolean = false,
    val difficulty: Difficulty = Difficulty.MEDIUM,
    val description: String = "",
    val tasks: List<TaskWithSubtasks> = emptyList()
)

class QuestEditViewModel(
    private val questsRepository: OfflineQuestsRepository,
    private val questId: Int?,
    private val currentQuestSectionNumber: Int?
) : ViewModel() {

    // Mock tasks
    private val mockTasks = listOf(
        TaskWithSubtasks(
            task = Task(
                id = 11,
                questId = 0,
                name = "Veeeeeeery-very-very loooooooooooooooooooooong task 1 and its huge description (add'l)"
            ),
            subtasks = listOf()
        ),
        TaskWithSubtasks(
            task = Task(id = 12,questId = 0,name = "Task 2"),
            subtasks = listOf(
                com.povush.modusvivendi.data.model.Task(
                    id = 1,
                    parentTaskId = 12,
                    name = "Subtask 1",
                    questId = 0
                ),
                com.povush.modusvivendi.data.model.Task(
                    id = 2,
                    parentTaskId = 12,
                    name = "Subtask 2",
                    questId = 0
                )
            )
        ),
        TaskWithSubtasks(
            task = Task(id = 13,questId = 0,name = "Task 3"),
            subtasks = listOf()
        )
    )

    private val _uiState = MutableStateFlow(
        QuestEditUiState(
            tasks = mockTasks
        )
    )
    val uiState: StateFlow<QuestEditUiState> = _uiState.asStateFlow()

//    private val _tasks = emptyList<MutableStateFlow<Task>>()
//    val tasks: List<StateFlow<Task>> = _tasks

    init {
        if (questId != null) {
            viewModelScope.launch {
                questsRepository.getQuestStreamById(questId).collect { quest ->
                    _uiState.update {
                        it.copy(
                            name = quest.name,
                            type = quest.type,
                            difficulty = quest.difficulty,
                            description = quest.description,
                        )
                    }
                }
                questsRepository.getAllTasksWithSubtasksByQuestId(questId).collect { tasks ->
                    _uiState.update {
                        it.copy(
                            tasks = tasks
                        )
                    }
                }
            }
        } else if (currentQuestSectionNumber != null) {
            _uiState.update {
                it.copy(type = QuestType.entries[currentQuestSectionNumber])
            }
        }
    }

    fun validate() {
        if (with(uiState.value) { name.isNotBlank() && description.isNotBlank() && tasks.isNotEmpty() }) {
            _uiState.update {
                it.copy(isValid = true)
            }
        } else {
            _uiState.update {
                it.copy(isValid = false)
            }
        }
    }

    fun updateQuestName(input: String) {
        _uiState.update {
            it.copy(name = input)
        }
    }

    fun updateType(questType: QuestType) {
        _uiState.update {
            it.copy(type = questType)
        }
    }

    fun updateTypeExpanded(typeExpanded: Boolean) {
        _uiState.update {
            it.copy(typeExpanded = typeExpanded)
        }
    }

    fun updateDifficulty(difficulty: Difficulty) {
        _uiState.update {
            it.copy(difficulty = difficulty)
        }
    }

    fun updateDescription(input: String) {
        _uiState.update {
            it.copy(description = input)
        }
    }

    fun onCheckedTaskChange(task: Task, isCompleted: Boolean) {
        updateTask(task = task, isCompleted = isCompleted)

        if (isCompleted && task.id in uiState.value.tasks.map { it.task.id }) {
            uiState.value.tasks.find { taskWithSubtasks ->
                taskWithSubtasks.task.id == task.id
            }?.apply {
                if (subtasks.isNotEmpty()) {
                    subtasks.forEach { subtask ->
                        if (!subtask.isAdditional) {
                            onCheckedTaskChange(task = subtask, isCompleted = true)
                        }
                    }
                }
            }
        }
    }

    fun onTaskTextChange(task: Task, input: String) {
        updateTask(task = task, name = input)
    }

    fun onReorderingTask(fromIndex: Int, toIndex: Int) {
        val updatedTasks = uiState.value.tasks.toMutableList().apply {
            add(toIndex,removeAt(fromIndex))
        }
        _uiState.update {
            it.copy(tasks = updatedTasks)
        }
        updateOrderIndexes()
    }

    fun onReorderingSubtask(taskIndex: Int, fromIndex: Int, toIndex: Int) {
        val updatedTasks = uiState.value.tasks.toMutableList().apply {
             this[taskIndex] = this[taskIndex].copy(
                 subtasks = this[taskIndex].subtasks.toMutableList().apply {
                     add(toIndex,removeAt(fromIndex))
                 }
             )
         }
        _uiState.update {
            it.copy(tasks = updatedTasks)
        }
        updateOrderIndexes()
    }

    private fun updateOrderIndexes() {
        uiState.value.tasks.forEachIndexed { taskWithSubtasksIndex, taskWithSubtasks ->
            updateTask(task = taskWithSubtasks.task, orderIndex = taskWithSubtasksIndex)
            taskWithSubtasks.subtasks.forEachIndexed { subtaskIndex, subtask ->
                updateTask(task = subtask, orderIndex = subtaskIndex)
            }
        }
    }

    private fun updateTask(
        task: Task,
        questId: Int? = null,
        parentTaskId: Int? = null,
        name: String? = null,
        isCompleted: Boolean? = null,
        counter: Pair<Int, Int>? = null,
        isAdditional: Boolean? = null,
        orderIndex: Int? = null,
    ) {
        val updatedTasks =
            uiState.value.tasks.map { taskWithSubtasks ->
                if (taskWithSubtasks.task.id == task.id) {
                    taskWithSubtasks.copy(
                        task = taskWithSubtasks.task.copy(
                            questId = questId ?: taskWithSubtasks.task.questId,
                            parentTaskId = parentTaskId ?: taskWithSubtasks.task.parentTaskId,
                            name = name ?: taskWithSubtasks.task.name,
                            isCompleted = isCompleted ?: taskWithSubtasks.task.isCompleted,
                            counter = counter ?: taskWithSubtasks.task.counter,
                            isAdditional = isAdditional ?: taskWithSubtasks.task.isAdditional,
                            orderIndex = orderIndex ?: taskWithSubtasks.task.orderIndex,
                        )
                    )
                } else if (task in taskWithSubtasks.subtasks) {
                    taskWithSubtasks.copy(
                        subtasks = getUpdatedSubtasks(
                            task = task,
                            subtasks = taskWithSubtasks.subtasks,
                            questId = questId,
                            parentTaskId = parentTaskId,
                            name = name,
                            isCompleted = isCompleted,
                            counter = counter,
                            isAdditional = isAdditional,
                            orderIndex = orderIndex,
                        )
                    )
                } else taskWithSubtasks
            }

        _uiState.update {
            it.copy(tasks = updatedTasks)
        }
    }

    private fun getUpdatedSubtasks(
        task: Task,
        subtasks: List<Task>,
        questId: Int? = null,
        parentTaskId: Int? = null,
        name: String? = null,
        isCompleted: Boolean? = null,
        counter: Pair<Int, Int>? = null,
        isAdditional: Boolean? = null,
        orderIndex: Int? = null,
    ): List<Task> {
        val updatedSubtasks = subtasks.map { subtask ->
            if (subtask.id == task.id) {
                subtask.copy(
                    questId = questId ?: subtask.questId,
                    parentTaskId = parentTaskId ?: subtask.parentTaskId,
                    name = name ?: subtask.name,
                    isCompleted = isCompleted ?: subtask.isCompleted,
                    counter = counter ?: subtask.counter,
                    isAdditional = isAdditional ?: subtask.isAdditional,
                    orderIndex = orderIndex ?: subtask.orderIndex,
                )
            } else subtask
        }

        return updatedSubtasks
    }
}