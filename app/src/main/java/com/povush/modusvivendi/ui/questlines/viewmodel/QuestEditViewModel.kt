package com.povush.modusvivendi.ui.questlines.viewmodel

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
    val isCompleted: Boolean = false,
    val tasks: List<TaskWithSubtasks> = emptyList()
)

class QuestEditViewModel(
    private val questsRepository: OfflineQuestsRepository,
    private val questId: Long?,
    private val currentQuestSectionNumber: Int?
) : ViewModel() {

    private val _uiState = MutableStateFlow(QuestEditUiState())
    val uiState: StateFlow<QuestEditUiState> = _uiState.asStateFlow()

    private var lastUnusedTaskId: Long = -1
    private var newQuestId: Long = -1

    init {
        if (questId != null) {
            viewModelScope.launch {
                launch {
                    questsRepository.getQuestStreamById(questId).collect { quest ->
                        _uiState.update { it.copy(
                                name = quest.name,
                                type = quest.type,
                                difficulty = quest.difficulty,
                                description = quest.description,
                                isCompleted = quest.isCompleted
                        ) }
                    }
                }
                launch {
                    questsRepository.getAllTasksWithSubtasksByQuestId(questId).collect { tasks ->
                        _uiState.update { it.copy(tasks = tasks) }
                    }
                }
            }
        } else if (currentQuestSectionNumber != null) {
            _uiState.update { it.copy(type = QuestType.entries[currentQuestSectionNumber]) }
        }
    }

    fun saveQuest() {
        viewModelScope.launch {
           if (questId != null) {
               questsRepository.updateQuest(
                   Quest(
                       id = questId,
                       name = uiState.value.name,
                       type = uiState.value.type,
                       difficulty = uiState.value.difficulty,
                       description = uiState.value.description,
                       isCompleted = uiState.value.isCompleted
                   )
               )
               questsRepository.deleteTasksByQuestId(questId)
               uiState.value.tasks.forEach { taskWithSubtasks ->
                   saveTask(taskWithSubtasks.task, questId)
               }
           } else {
               newQuestId = questsRepository.insertQuest(
                   Quest(
                       name = uiState.value.name,
                       type = uiState.value.type,
                       difficulty = uiState.value.difficulty,
                       description = uiState.value.description,
                       isCompleted = uiState.value.isCompleted
                   )
               )
               uiState.value.tasks.forEach { taskWithSubtasks ->
                   saveTask(taskWithSubtasks.task, newQuestId)
               }
           }
        }
    }

    private fun saveTask(task: Task, questId: Long) {
        viewModelScope.launch {
            if (task.id >= 0) {
                questsRepository.insertTask(
                    Task(
                        id = task.id,
                        questId = questId,
                        parentTaskId = task.parentTaskId,
                        name = task.name,
                        isCompleted = task.isCompleted,
                        counter = task.counter,
                        isAdditional = task.isAdditional,
                        orderIndex = task.orderIndex
                    )
                )
            } else {
                questsRepository.insertTask(
                    Task(
                        questId = questId,
                        parentTaskId = task.parentTaskId,
                        name = task.name,
                        isCompleted = task.isCompleted,
                        counter = task.counter,
                        isAdditional = task.isAdditional,
                        orderIndex = task.orderIndex
                    )
                )
            }
        }
    }

    fun checkCompletionStatus() {
//        if (uiState.value.tasks.filter { !it.isCompleted && it.parentTaskId == null } == emptyList<Task>()) {
//            _uiState.update {
//                it.copy(isCompleted = true)
//            }
//        } else {
//            _uiState.update {
//                it.copy(isCompleted = false)
//            }
//        }
    }

    fun validate() {
//        if (with(uiState.value) {
//            name.isNotBlank()
//            && description.isNotBlank()
//            && tasks.isNotEmpty()
//            && tasks.all { it.name.isNotBlank() }
//            }) {
//            _uiState.update {
//                it.copy(isValid = true)
//            }
//        } else {
//            _uiState.update {
//                it.copy(isValid = false)
//            }
//        }
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
//        val updatedTasks = uiState.value.tasks.map {
//            if (it.id == task.id) it.copy(isCompleted = isCompleted) else it
//        }
//
//        _uiState.update {
//            it.copy(tasks = updatedTasks)
//        }
    }

    fun onTaskTextChange(task: Task, input: String) {
//        val updatedTasks = uiState.value.tasks.map {
//            if (it.id == task.id) it.copy(name = input) else it
//        }
//
//        _uiState.update {
//            it.copy(tasks = updatedTasks)
//        }
    }

    fun createNewTask() {
//        val updatedTasks = uiState.value.tasks.toMutableList().apply {
//            val currentLastOrderIndex = uiState.value.tasks
//                .filter { it.parentTaskId == null }
//                .size
//            add(Task(id = lastUnusedTaskId, questId = questId ?: -1, orderIndex = currentLastOrderIndex))
//            lastUnusedTaskId -= 1
//        }
//
//        _uiState.update {
//            it.copy(tasks = updatedTasks)
//        }
    }

    fun createNewSubtask(parentTaskId: Long) {
//        val updatedTasks = uiState.value.tasks.toMutableList().apply {
//            val currentLastOrderIndex = uiState.value.tasks
//                .filter { it.parentTaskId == parentTaskId }
//                .size
//            add(Task(id = lastUnusedTaskId, questId = questId ?: -1, orderIndex = currentLastOrderIndex, parentTaskId = parentTaskId))
//            lastUnusedTaskId -= 1
//        }
//
//        _uiState.update {
//            it.copy(tasks = updatedTasks)
//        }
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