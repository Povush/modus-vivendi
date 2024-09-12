package com.povush.modusvivendi.ui.questlines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.povush.modusvivendi.data.model.Difficulty
import com.povush.modusvivendi.data.model.Quest
import com.povush.modusvivendi.data.model.QuestType
import com.povush.modusvivendi.data.repository.OfflineQuestsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date

data class QuestUiState(
    val quest: Flow<Quest>,
    val canBeCompleted: Boolean = false,
    val expanded: Boolean = false
)

class QuestViewModel(private val questsRepository: OfflineQuestsRepository) : ViewModel() {
//    val _uiState: StateFlow<QuestUiState> =
//        questsRepository.getQuestStreamById(questId)
//            .filterNotNull()
//            .map {
//                ItemDetailsUiState(outOfStock = it.quantity <= 0, itemDetails = it.toItemDetails())
//            }.stateIn(
//                scope = viewModelScope,
//                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
//                initialValue = ItemDetailsUiState()
//            )

    private val _uiState = MutableStateFlow(QuestUiState())
    val uiState: StateFlow<QuestUiState> = _uiState.asStateFlow()

    fun loadQuest(quest: Quest) {
        _uiState.update {
            it.copy(
                quest = questsRepository.getQuestStreamById(quest.id)
            )
        }

//        val _uiState: StateFlow<QuestUiState> =
//            questsRepository.getQuestStreamById(questId)
//                .filterNotNull()
//                .map {
//                    ItemDetailsUiState(outOfStock = it.quantity <= 0, itemDetails = it.toItemDetails())
//                }.stateIn(
//                    scope = viewModelScope,
//                    started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
//                    initialValue = ItemDetailsUiState()
//                )
    }

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