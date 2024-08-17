package com.povush.modusvivendi.data.model

import androidx.lifecycle.ViewModel
import com.povush.modusvivendi.data.dataclass.Quest
import com.povush.modusvivendi.data.dataclass.Task
import com.povush.modusvivendi.data.state.QuestlinesUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class QuestlinesViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(QuestlinesUiState())
    val uiState: StateFlow<QuestlinesUiState> = _uiState.asStateFlow()

    fun changeTaskStatus(
        quest: Quest,
        task: Task
    ) {
        _uiState.update {
            var quests = it.quests

            val questIndex = quests.indexOf(quest)
            var tasks = quests[questIndex].tasks
            val taskIndex = tasks.indexOf(task)
            val taskStatus = quests[questIndex].tasks[taskIndex].isCompleted
            val updatedQuests = quests[questIndex]

            

            it.copy()

            // Code
//            Find out task in QuestlinesUiState
//            val quests = mutableListOf(it.quests)
//            val questIndex = quests.indexOf(quest)
//            val tasks = quests[questIndex].tasks
//            val taskIndex = tasks.indexOf(task)
//            val taskStatus = quests[questIndex].tasks[taskIndex].isCompleted
//            val newQuests = null
//
//            it.copy(
//                quests = listOf()
//            )
        }
    }
}