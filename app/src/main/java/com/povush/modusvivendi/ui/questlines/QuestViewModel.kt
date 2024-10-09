package com.povush.modusvivendi.ui.questlines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.povush.modusvivendi.data.model.Quest
import com.povush.modusvivendi.data.model.Task
import com.povush.modusvivendi.data.repository.OfflineQuestsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class QuestUiState(
    val quest: Quest = Quest(),
    val tasks: List<Task> = emptyList(),
    val canBeCompleted: Boolean = false,
    val expanded: Boolean = false
)

class QuestViewModel(
    private val questsRepository: OfflineQuestsRepository,
    private val quest: Quest
) : ViewModel() {

    private val _uiState = MutableStateFlow(QuestUiState(quest = quest))
    val uiState: StateFlow<QuestUiState> = _uiState.asStateFlow()

    init {
        loadQuest()
        loadTasks()
    }

    private fun loadQuest() {
        viewModelScope.launch {
            questsRepository.getQuestStreamById(uiState.value.quest.id).collect { quest ->
                _uiState.value = _uiState.value.copy(quest = quest)
            }
        }
    }

    private fun loadTasks() {
        viewModelScope.launch {
            questsRepository.getAllTasksStream(uiState.value.quest.id).collect { tasks ->
                _uiState.value = _uiState.value.copy(tasks = tasks)
            }
        }
    }

    fun changeQuestExpandStatus() {
        _uiState.update {
            it.copy(expanded = !it.expanded)
        }
    }

    fun updateTaskStatus(task: Task, isCompleted: Boolean) {
//        viewModelScope.launch {
//            questsRepository.updateTask(task.copy(isCompleted = !task.isCompleted))
//        }
    }

    fun changeQuestPinStatus() {
        /*TODO: Not yet implemented*/
    }
}