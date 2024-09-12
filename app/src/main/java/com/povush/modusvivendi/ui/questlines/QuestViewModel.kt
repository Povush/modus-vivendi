package com.povush.modusvivendi.ui.questlines

import androidx.lifecycle.ViewModel
import com.povush.modusvivendi.data.model.Quest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class QuestUiState(
    val questData: Quest,
    val canBeCompleted: Boolean = false,
    val expanded: Boolean = false,
    val pinned: Boolean = false
)

class QuestViewModel(questUiState: QuestUiState) : ViewModel() {
    private val _uiState = MutableStateFlow(questUiState)
    val uiState: StateFlow<QuestUiState> = _uiState.asStateFlow()

    fun changeQuestExpandStatus() {
        _uiState.update {
            it.copy(expanded = !it.expanded)
        }
    }

    fun changeQuestPinStatus() {
        _uiState.update {
            it.copy(pinned = !it.pinned)
        }
    }
}