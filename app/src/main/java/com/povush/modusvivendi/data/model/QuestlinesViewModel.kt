package com.povush.modusvivendi.data.model

import androidx.lifecycle.ViewModel
import com.povush.modusvivendi.data.dataclass.Difficulty
import com.povush.modusvivendi.data.dataclass.Quest
import com.povush.modusvivendi.data.dataclass.QuestType
import com.povush.modusvivendi.data.dataclass.Task
import com.povush.modusvivendi.data.local.LocalQuestsDataProvider
import com.povush.modusvivendi.data.state.QuestlinesUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.Date

class QuestlinesViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(QuestlinesUiState())
    val uiState: StateFlow<QuestlinesUiState> = _uiState.asStateFlow()

    init {
        loadQuests()
    }

    private fun loadQuests() {
        val quests: Map<QuestType, List<Quest>> =
            LocalQuestsDataProvider.allQuests.groupBy { it.type }
        _uiState.value = QuestlinesUiState(quests = quests)
    }

    fun onTabClick(index: Int) {
        _uiState.update {
            it.copy(selectedQuestSection = index)
        }
    }

    // TODO: The sorting method according to the current sorting criterion
}
