package com.povush.modusvivendi.ui.questlines.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.povush.modusvivendi.data.model.Difficulty
import com.povush.modusvivendi.data.model.Quest
import com.povush.modusvivendi.data.model.QuestType
import com.povush.modusvivendi.data.model.Task
import com.povush.modusvivendi.data.model.TaskWithSubtasks
import com.povush.modusvivendi.data.repository.OfflineQuestsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class QuestEditUiState(
    // Get from quest data if questId received
    val name: String = "",
    val type: QuestType = QuestType.ADDITIONAL,
    val difficulty: Difficulty = Difficulty.MEDIUM,
    val description: String = "",
    val isCompleted: Boolean = false,
    val tasks: List<TaskWithSubtasks> = emptyList(),
    // Other state values
    val isValid: Boolean = false,
    val typeExpanded: Boolean = false
)

class QuestEditViewModel(
    private val questsRepository: OfflineQuestsRepository,
    private val questId: Long?,
    private val currentQuestSectionNumber: Int?
) : ViewModel() {

    private val _uiState = MutableStateFlow(QuestEditUiState())
    val uiState: StateFlow<QuestEditUiState> = _uiState.asStateFlow()

    private var _lastUnusedTaskId: Long = -1

    init {
        loadData()
    }

    private fun loadData() {
        if (questId != null) {
            viewModelScope.launch(Dispatchers.IO) {
                questsRepository.getQuestById(questId).also { quest ->
                    _uiState.update { it.copy(
                        name = quest.name,
                        type = quest.type,
                        difficulty = quest.difficulty,
                        description = quest.description,
                        isCompleted = quest.isCompleted
                        ) }
                }
                questsRepository.getAllTasksWithSubtasksByQuestId(questId).also { tasks ->
                    val sortedTasks = tasks.map {
                        it.copy(subtasks = it.subtasks.sortedBy { subtask -> subtask.orderIndex })
                    }
                    _uiState.update { it.copy(tasks = sortedTasks) }
                }
            }
        } else if (currentQuestSectionNumber != null) {
            _uiState.update { it.copy(type = QuestType.entries[currentQuestSectionNumber]) }
        }
    }

    fun saveQuestAndTasks(navigateBack: () -> Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            if (questId != null) { questsRepository.deleteQuestById(questId) }

            val questId = questsRepository.insertQuest(Quest(
                name = uiState.value.name,
                type = uiState.value.type,
                difficulty = uiState.value.difficulty,
                description = uiState.value.description,
                isCompleted = uiState.value.isCompleted
            ))

            uiState.value.tasks.forEach { taskWithSubtasks ->
                val task = taskWithSubtasks.task
                val subtasks = taskWithSubtasks.subtasks

                val taskId = questsRepository.insertTask(Task(
                    questId = questId,
                    name = task.name,
                    isCompleted = task.isCompleted,
                    counter = task.counter,
                    isAdditional = task.isAdditional,
                    orderIndex = task.orderIndex
                ))

                subtasks.forEach { subtask ->
                    questsRepository.insertTask(Task(
                        questId = questId,
                        parentTaskId = taskId,
                        name = subtask.name,
                        isCompleted = subtask.isCompleted,
                        counter = subtask.counter,
                        isAdditional = subtask.isAdditional,
                        orderIndex = subtask.orderIndex
                    ))
                }
            }
        }
        navigateBack()
    }

    fun checkQuestCompletionStatus() {
        val allParentTasksCompleted = uiState.value.tasks.map { it.task }.all { it.isCompleted }

        if (allParentTasksCompleted) _uiState.update { it.copy(isCompleted = true) }
        else _uiState.update { it.copy(isCompleted = false) }
    }

    fun validate() {
        if (with(uiState.value) {
            name.isNotBlank()
            && description.isNotBlank()
            && tasks.isNotEmpty()
            && tasks.map { it.task }.all { it.name.isNotBlank() }                                   // All tasks have name
            && tasks.map { it.subtasks }.all { it.all { subtask -> subtask.name.isNotBlank() } }    // All subtasks have name
            }) { _uiState.update { it.copy(isValid = true) } }
        else { _uiState.update { it.copy(isValid = false) } }
    }

    fun updateQuestName(input: String) {
        _uiState.update { it.copy(name = input) }
    }

    fun updateType(questType: QuestType) {
        _uiState.update { it.copy(type = questType) }
    }

    fun updateTypeExpanded(typeExpanded: Boolean) {
        _uiState.update { it.copy(typeExpanded = typeExpanded) }
    }

    fun updateDifficulty(difficulty: Difficulty) {
        _uiState.update { it.copy(difficulty = difficulty) }
    }

    fun updateDescription(input: String) {
        _uiState.update { it.copy(description = input) }
    }

    fun onCheckedTaskChange(task: Task, isCompleted: Boolean) {
        val updatedTasks = uiState.value.tasks.map { taskWithSubtasks ->
            if (task.id == taskWithSubtasks.task.id) {
                taskWithSubtasks.copy(task = taskWithSubtasks.task.copy(isCompleted = isCompleted))
            } else if (task.id in taskWithSubtasks.subtasks.map { it.id }) {
                taskWithSubtasks.copy(subtasks = taskWithSubtasks.subtasks.map {
                    if (task.id == it.id) it.copy(isCompleted = isCompleted)
                    else it
                })
            } else taskWithSubtasks
        }

        _uiState.update { it.copy(tasks = updatedTasks) }
    }

    fun onTaskTextChange(task: Task, input: String) {
        val updatedTasks = uiState.value.tasks.map { taskWithSubtasks ->
            if (task.id == taskWithSubtasks.task.id) {
                taskWithSubtasks.copy(task = taskWithSubtasks.task.copy(name = input))
            } else if (task.id in taskWithSubtasks.subtasks.map { it.id }) {
                taskWithSubtasks.copy(subtasks = taskWithSubtasks.subtasks.map {
                    if (task.id == it.id) it.copy(name = input)
                    else it
                })
            } else taskWithSubtasks
        }

        _uiState.update { it.copy(tasks = updatedTasks) }
    }

    fun createNewTask() {
        val updatedTasks = uiState.value.tasks.toMutableList().apply {
            val currentLastOrderIndex = uiState.value.tasks.map { it.task }.size
            add(TaskWithSubtasks(
                task = Task(id = _lastUnusedTaskId, questId = -1, orderIndex = currentLastOrderIndex),
                subtasks = emptyList()
            ))
            _lastUnusedTaskId -= 1
        }

        _uiState.update { it.copy(tasks = updatedTasks) }
    }

    fun createNewSubtask(parentTaskId: Long) {
        val updatedTasks = uiState.value.tasks.map {
            if (parentTaskId == it.task.id) {
                val currentLastOrderIndex = it.subtasks.size
                it.copy(
                    subtasks = it.subtasks.toMutableList().apply {
                        add(Task(
                            id = _lastUnusedTaskId,
                            questId = -1,
                            parentTaskId = parentTaskId,
                            orderIndex = currentLastOrderIndex
                        ))
                        _lastUnusedTaskId -= 1
                    }
                )
            } else it
        }

        _uiState.update { it.copy(tasks = updatedTasks) }
    }

    fun deleteTask(task: Task) {
        val updatedTasks =
            if (task.parentTaskId == null) uiState.value.tasks.filterNot { task.id == it.task.id }
            else uiState.value.tasks.map {
                if (it.subtasks.any { subtask -> task.id == subtask.id }) {
                    it.copy(subtasks = it.subtasks.filterNot { subtask -> subtask.id == task.id })
                } else it
            }

        _uiState.update { it.copy(tasks = updatedTasks) }
    }

    fun onReorderingTasks(fromIndex: Int, toIndex: Int) {
        val updatedTasks =
            if (fromIndex < toIndex) { // Moving down
                uiState.value.tasks.map {
                    if (it.task.orderIndex in fromIndex..toIndex) {
                        if (it.task.orderIndex == fromIndex) it.copy(
                            task = it.task.copy(orderIndex = toIndex)
                        ) else it.copy(
                            task = it.task.copy(orderIndex = it.task.orderIndex - 1)
                        )
                    } else it
                }
            } else if (fromIndex > toIndex) { // Moving upward
                uiState.value.tasks.map {
                    if (it.task.orderIndex in toIndex..fromIndex) {
                        if (it.task.orderIndex == fromIndex) it.copy(
                            task = it.task.copy(orderIndex = toIndex)
                        ) else it.copy(
                            task = it.task.copy(orderIndex = it.task.orderIndex + 1)
                        )
                    } else it
                }
            } else uiState.value.tasks

        val sortedUpdatedTasks = updatedTasks.sortedBy { it.task.orderIndex }

        _uiState.update { it.copy(tasks = sortedUpdatedTasks) }
    }

    fun onReorderingSubtasks(parentTaskId: Long, fromIndex: Int, toIndex: Int) {
        val updatedTasks =
            if (fromIndex < toIndex) { // Moving down
                uiState.value.tasks.map {
                    if (it.task.id == parentTaskId) {
                        it.copy(subtasks = it.subtasks.map { subtask ->
                            if (subtask.orderIndex in fromIndex..toIndex) {
                                if (subtask.orderIndex == fromIndex) subtask.copy(orderIndex = toIndex)
                                else subtask.copy(orderIndex = subtask.orderIndex - 1)
                            } else subtask
                        })
                    } else it
                }
            } else if (fromIndex > toIndex) { // Moving upward
                uiState.value.tasks.map {
                    if (it.task.id == parentTaskId) {
                        it.copy(subtasks = it.subtasks.map { subtask ->
                            if (subtask.orderIndex in toIndex..fromIndex) {
                                if (subtask.orderIndex == fromIndex) subtask.copy(orderIndex = toIndex)
                                else subtask.copy(orderIndex = subtask.orderIndex + 1)
                            } else subtask
                        })
                    } else it
                }
            } else uiState.value.tasks

        val sortedUpdatedTasks = updatedTasks.map {
            it.copy(subtasks = it.subtasks.sortedBy { subtask -> subtask.orderIndex })
        }

        _uiState.update { it.copy(tasks = sortedUpdatedTasks) }
    }
}