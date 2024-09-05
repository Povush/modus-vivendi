package com.povush.modusvivendi.data.model

import androidx.lifecycle.ViewModel
import com.povush.modusvivendi.data.dataclass.Quest
import com.povush.modusvivendi.data.dataclass.QuestType
import com.povush.modusvivendi.data.local.LocalQuestsDataProvider
import com.povush.modusvivendi.data.state.QuestlinesUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class QuestlinesViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(QuestlinesUiState())
    val uiState: StateFlow<QuestlinesUiState> = _uiState.asStateFlow()

    init {
        loadQuests()
    }

    private fun loadQuests() {
        val quests: Map<QuestType,List<Quest>> =
            LocalQuestsDataProvider.allQuests.groupBy { it.type }
        _uiState.update {
            it.copy(questsByTypes = quests)
        }
    }

    fun onTabClick(index: Int) {
        _uiState.update {
            it.copy(selectedQuestSection = QuestType.entries[index])
        }
    }

    fun sectionCounter(index: Int): Int {
        val questType = QuestType.entries.getOrNull(index)
        val numberOfQuests = uiState.value.questsByTypes[questType]?.size
        return numberOfQuests ?: 0
    }

    fun changeQuestExpandStatus(questId: Int) {
        val questIndex = LocalQuestsDataProvider.allQuests.indexOfFirst { it.id == questId }
        val quest = LocalQuestsDataProvider.allQuests[questIndex]
        LocalQuestsDataProvider.allQuests[questIndex] = quest.copy(
            expanded = !quest.expanded
        )
        loadQuests()
    }

    /*TODO: The sorting method according to the current sorting criterion*/
}
