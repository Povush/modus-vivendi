package com.povush.modusvivendi.ui.questlines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.povush.modusvivendi.data.model.Difficulty
import com.povush.modusvivendi.data.model.Quest
import com.povush.modusvivendi.data.model.QuestType
import com.povush.modusvivendi.data.repository.OfflineQuestsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date

data class QuestUiState(
    val quest: Quest,
    val canBeCompleted: Boolean = false,
    val expanded: Boolean = false
)

class QuestViewModel(private val questsRepository: OfflineQuestsRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(questUiState)
    val uiState: StateFlow<QuestUiState> = _uiState.asStateFlow()

    fun changeQuestExpandStatus() {
        _uiState.update {
            it.copy(expanded = !it.expanded)
        }
    }

    fun changeTaskStatus() {
        viewModelScope.launch {

        }
    }

    fun changeQuestPinStatus() {
        /*TODO: Not yet implemented*/
    }
}