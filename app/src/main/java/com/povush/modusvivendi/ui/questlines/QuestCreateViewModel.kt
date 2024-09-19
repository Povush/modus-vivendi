package com.povush.modusvivendi.ui.questlines

import androidx.lifecycle.ViewModel
import com.povush.modusvivendi.data.model.Difficulty
import com.povush.modusvivendi.data.model.Quest
import com.povush.modusvivendi.data.model.QuestType
import com.povush.modusvivendi.data.model.Subtask
import com.povush.modusvivendi.data.model.Task
import com.povush.modusvivendi.data.repository.OfflineQuestsRepository
import com.povush.modusvivendi.data.repository.QuestSortingMethod
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class QuestCreateUiState(
    val name: String = "",
    /*TODO: From screen that we go here*/
    val type: QuestType = QuestType.ADDITIONAL,
    val difficulty: Difficulty = Difficulty.MEDIUM,
    val description: String = "Sample description.",
    val tasks: Map<Task, List<Subtask>> = emptyMap()
)

class QuestCreateViewModel(private val questsRepository: OfflineQuestsRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(QuestCreateUiState())
    val uiState: StateFlow<QuestCreateUiState> = _uiState.asStateFlow()

    fun changeQuestName(input: String) {
        _uiState.update {
            uiState.value.copy(name = input)
        }
    }
}