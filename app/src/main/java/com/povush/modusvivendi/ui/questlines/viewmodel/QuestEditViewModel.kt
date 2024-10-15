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

    fun saveQuestAndTasks() {
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
//        val updatedTasks = uiState.value.tasks.toMutableList().apply {
//            remove(task)
//            uiState.value.tasks.filter { it.parentTaskId == task.id }.forEach { subtask ->
//                remove(subtask)
//            }
//        }
//
//        _uiState.update {
//            it.copy(tasks = updatedTasks)
//        }
    }

    fun onReorderingTasks(fromIndex: Int, toIndex: Int) {
//        val updatedTasks =
//            if (fromIndex < toIndex) { // Moving down
//                uiState.value.tasks.map { task ->
//                    if (task.parentTaskId == null && task.orderIndex in fromIndex..toIndex) {
//                        if (task.orderIndex == fromIndex) task.copy(orderIndex = toIndex)
//                        else task.copy(orderIndex = task.orderIndex - 1)
//                    } else task
//                }
//            } else if (fromIndex > toIndex) { // Moving upward
//                uiState.value.tasks.map { task ->
//                    if (task.parentTaskId == null && task.orderIndex in toIndex..fromIndex) {
//                        if (task.orderIndex == fromIndex) task.copy(orderIndex = toIndex)
//                        else task.copy(orderIndex = task.orderIndex + 1)
//                    } else task
//                }
//            } else uiState.value.tasks
//
//        _uiState.update {
//            it.copy(tasks = updatedTasks)
//        }
    }

    fun onReorderingSubtasks(parentTaskId: Long, fromIndex: Int, toIndex: Int) {
//        val updatedTasks =
//            if (fromIndex < toIndex) { // Moving down
//                uiState.value.tasks.map { task ->
//                    if (task.parentTaskId == parentTaskId && task.orderIndex in fromIndex..toIndex) {
//                        if (task.orderIndex == fromIndex) task.copy(orderIndex = toIndex)
//                        else task.copy(orderIndex = task.orderIndex - 1)
//                    } else task
//                }
//            } else if (fromIndex > toIndex) { // Moving upward
//                uiState.value.tasks.map { task ->
//                    if (task.parentTaskId == parentTaskId && task.orderIndex in toIndex..fromIndex) {
//                        if (task.orderIndex == fromIndex) task.copy(orderIndex = toIndex)
//                        else task.copy(orderIndex = task.orderIndex + 1)
//                    } else task
//                }
//            } else uiState.value.tasks
//
//        _uiState.update {
//            it.copy(tasks = updatedTasks)
//        }
    }
}