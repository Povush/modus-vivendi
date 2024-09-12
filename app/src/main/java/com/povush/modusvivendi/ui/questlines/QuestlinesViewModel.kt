package com.povush.modusvivendi.ui.questlines

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class QuestlinesViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(QuestlinesUiState(0))
    val uiState: StateFlow<QuestlinesUiState> = _uiState.asStateFlow()

    init {
        loadQuests()
    }

    private fun loadQuests() {
        /*TODO: Not yet implemented*/
    }

    fun onTabClick(index: Int) {
//        _uiState.update {
//            it.copy(selectedQuestSection = QuestType.entries[index])
//        }
    }

    fun sectionCounter(index: Int): Int {
        /*TODO: Not yet implemented*/
        return 0
    }

    fun changeQuestExpandStatus(questId: Int) {
        /*TODO: Not yet implemented*/
    }

    /*TODO: The sorting method according to the current sorting criterion*/
}
