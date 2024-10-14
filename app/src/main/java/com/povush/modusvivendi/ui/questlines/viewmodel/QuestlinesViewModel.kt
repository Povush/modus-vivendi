package com.povush.modusvivendi.ui.questlines.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.povush.modusvivendi.data.model.Quest
import com.povush.modusvivendi.data.model.QuestType
import com.povush.modusvivendi.data.model.Task
import com.povush.modusvivendi.data.repository.OfflineQuestsRepository
import com.povush.modusvivendi.data.repository.QuestSortingMethod
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class QuestlinesUiState(
    val allQuests: List<Quest> = emptyList(),
    val allTasksByQuestId: Map<Long, List<Task>> = emptyMap(),
    val selectedQuestSection: QuestType = QuestType.MAIN,
    val sortingMethod: QuestSortingMethod = QuestSortingMethod.BY_DIFFICULTY_DOWN
)

class QuestlinesViewModel(private val questsRepository: OfflineQuestsRepository) : ViewModel() {
    /*TODO: Remember sortingMethod in repository*/
    private val _uiState = MutableStateFlow(QuestlinesUiState())
    val uiState: StateFlow<QuestlinesUiState> = _uiState.asStateFlow()

    init {
        loadQuests()
    }

    private fun loadQuests() {
        viewModelScope.launch {
            questsRepository.getAllQuestsStream(uiState.value.sortingMethod).collect { allQuests ->
                _uiState.update {
                    it.copy(allQuests = allQuests)
                }
            }
        }
    }

    fun updateTaskStatus(task: Task, isCompleted: Boolean) {
        viewModelScope.launch {
            questsRepository.updateTask(task.copy(isCompleted = isCompleted))
        }
    }

    fun switchQuestSection(index: Int) {
        _uiState.update {
            it.copy(selectedQuestSection = QuestType.entries[index])
        }
    }

    fun sectionCounter(index: Int): Int {
        val numberOfQuests = uiState.value.allQuests
            .filter { it.type == QuestType.entries[index] }.size
        return numberOfQuests ?: 0
    }

    fun changeSortingMethod(sortingMethod: QuestSortingMethod) {
        _uiState.update {
            it.copy(sortingMethod = sortingMethod)
        }
        loadQuests()
    }
}
