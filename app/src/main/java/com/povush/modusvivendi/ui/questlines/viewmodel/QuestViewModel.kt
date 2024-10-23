package com.povush.modusvivendi.ui.questlines.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.povush.modusvivendi.data.model.Difficulty
import com.povush.modusvivendi.data.model.Quest
import com.povush.modusvivendi.data.model.Task
import com.povush.modusvivendi.data.model.TaskWithSubtasks
import com.povush.modusvivendi.data.repository.OfflineQuestsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

//data class QuestUiState(
//    val name: String = "",
//    val difficulty: Difficulty = Difficulty.MEDIUM,
//    val description: String = "",
//    val isCompleted: Boolean = false,
//    val isExpanded: Boolean = false,
//    val tasks: List<TaskWithSubtasks> = emptyList()
//)

//@HiltViewModel
//class QuestViewModel @Inject constructor(
//    private val questsRepository: OfflineQuestsRepository,
//    private val quest: Quest
//) : ViewModel() {
//
//    private val _uiState = MutableStateFlow(QuestUiState(
//        name = quest.name,
//        difficulty = quest.difficulty,
//        description = quest.description,
//        isCompleted = quest.isCompleted
//    ))
//    val uiState: StateFlow<QuestUiState> = _uiState.asStateFlow()
//
//    private val _toastMessage = MutableSharedFlow<Int>()
//    val toastMessage: SharedFlow<Int> = _toastMessage
//
//    init {
//        loadTasks()
//    }
//
//    private fun loadTasks() {
//        viewModelScope.launch {
//            questsRepository.getAllTasksWithSubtasksStreamByQuestId(quest.id).collect { tasks ->
//                _uiState.update { it.copy(tasks = tasks) }
//            }
//        }
//    }
//
//    fun deleteQuest() {
//        viewModelScope.launch {
//            questsRepository.deleteQuest(quest)
//        }
//    }
//
//    fun changeQuestExpandStatus() {
//        _uiState.update { it.copy(isExpanded = !it.isExpanded) }
//    }
//
//    fun updateTaskStatus(task: Task, isCompleted: Boolean): Boolean {
//        if (isCompleted && task.parentTaskId == null) {                                             // For task with uncompleted non-additional subtasks
//            uiState.value.tasks.find { it.task.id == task.id }.also {
//                val canBeCompleted = it?.subtasks
//                    ?.none { subtask -> !subtask.isCompleted && !subtask.isAdditional } ?: true
//                if (!canBeCompleted) return false
//            }
//        } else if (!isCompleted && task.parentTaskId != null && !task.isAdditional) {               // For non-additional subtask with completed parent task
//            uiState.value.tasks
//                .find { taskWithSubtasks -> task.id in taskWithSubtasks.subtasks.map { it.id } }
//                .also { val parentTask = it?.task
//                    val parentTaskCompleted = parentTask?.isCompleted ?: false
//                    if (parentTaskCompleted) {
//                        viewModelScope.launch {
//                            if (parentTask != null) {
//                                questsRepository.updateTask(parentTask.copy(isCompleted = false))
//                            }
//                        }
//                    }
//                }
//        }
//        viewModelScope.launch {
//            questsRepository.updateTask(task.copy(isCompleted = isCompleted))
//        }
//        return true
//    }
//}