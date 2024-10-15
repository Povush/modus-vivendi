package com.povush.modusvivendi.ui.questlines.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.povush.modusvivendi.data.model.Quest
import com.povush.modusvivendi.data.model.QuestType
import com.povush.modusvivendi.data.repository.OfflineQuestsRepository
import com.povush.modusvivendi.data.repository.QuestSortingMethod
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class QuestlinesUiState(
    val allQuestsByType: Map<QuestType, List<Quest>> = emptyMap(),
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

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun loadQuests() {
        viewModelScope.launch {
            uiState.flatMapLatest { uiState ->
                questsRepository.getAllQuestsStream(uiState.sortingMethod)
            }.collect { quests ->
                val groupedQuests = quests.groupBy { it.type }
                _uiState.update { it.copy(allQuestsByType = groupedQuests) }
            }
        }
    }

    fun switchQuestSection(index: Int) {
        _uiState.update { it.copy(selectedQuestSection = QuestType.entries[index]) }
    }

    fun sectionCounter(index: Int): Int {
        val numberOfQuests = uiState.value.allQuestsByType[QuestType.entries[index]]?.size
        return numberOfQuests ?: 0
    }

    fun changeSortingMethod(sortingMethod: QuestSortingMethod) {
        _uiState.update { it.copy(sortingMethod = sortingMethod) }
    }
}
