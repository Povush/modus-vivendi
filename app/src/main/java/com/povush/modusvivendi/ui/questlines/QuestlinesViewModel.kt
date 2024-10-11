package com.povush.modusvivendi.ui.questlines

import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.pager.PagerState
import com.povush.modusvivendi.data.model.Quest
import com.povush.modusvivendi.data.model.QuestType
import com.povush.modusvivendi.data.model.Task
import com.povush.modusvivendi.data.repository.OfflineQuestsRepository
import com.povush.modusvivendi.data.repository.QuestSortingMethod
import com.povush.modusvivendi.ui.AppViewModelProvider
import com.povush.modusvivendi.ui.createQuestViewModelExtras
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class QuestlinesUiState(
    val allQuestsByType: Map<QuestType, List<Quest>> = emptyMap(),
    val selectedQuestSection: QuestType = QuestType.MAIN,
    val sortingMethod: QuestSortingMethod = QuestSortingMethod.BY_NAME_UP
)

class QuestlinesViewModel(private val questsRepository: OfflineQuestsRepository) : ViewModel() {
    /*TODO: Remember sortingMethod in repository*/
    private val _uiState = MutableStateFlow(QuestlinesUiState())
    val uiState: StateFlow<QuestlinesUiState> = _uiState.asStateFlow()

    init {
        loadQuests()
    }

    private fun loadQuests() {
        val allQuestsStream: Flow<List<Quest>> =
            questsRepository.getAllQuestsStream(uiState.value.sortingMethod)

        viewModelScope.launch {
            allQuestsStream.collect { allQuests ->
                val groupedQuests = allQuests.groupBy { it.type }
                _uiState.update { it.copy(allQuestsByType = groupedQuests) }
            }
        }
    }

//    fun createNewQuest(): Int {
//        viewModelScope.launch {
//            questsRepository.insertQuest(Quest())
//        }
//        return
//    }

    fun switchQuestSection(index: Int) {
        _uiState.update {
            it.copy(selectedQuestSection = QuestType.entries[index])
        }
    }

    fun sectionCounter(index: Int): Int {
        val currentType = QuestType.entries[index]
        val numberOfQuests = uiState.value.allQuestsByType[currentType]?.size
        return numberOfQuests ?: 0
    }

    /*TODO: The sorting method according to the current sorting criterion*/
}
