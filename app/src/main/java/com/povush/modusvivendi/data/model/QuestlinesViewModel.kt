package com.povush.modusvivendi.data.model

import androidx.lifecycle.ViewModel
import com.povush.modusvivendi.data.dataclass.Quest
import com.povush.modusvivendi.data.dataclass.QuestType
import com.povush.modusvivendi.data.dataclass.Task
import com.povush.modusvivendi.data.state.QuestlinesUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class QuestlinesViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(QuestlinesUiState())
    val uiState: StateFlow<QuestlinesUiState> = _uiState.asStateFlow()

    fun onTabClick(index: Int) {
        _uiState.update {
            it.copy(selectedQuestSection = index)
        }
    }

    fun changeQuestExpandStatus(quest: Quest) {
        _uiState.update {
            val quests = when (quest.type) {
                QuestType.Main -> it.mainQuests
                QuestType.Additional -> it.additionalQuests
                QuestType.Completed -> it.completedQuests
                QuestType.Failed -> it.failedQuests
            }
            val questIndex = quests.indexOf(quest)
            val expandStatus = quests[questIndex].expanded
            val updatedQuest = quests[questIndex].copy(expanded = !expandStatus)
            val updatedQuests = quests.mapIndexed { index, value ->
                if (index == questIndex) updatedQuest else value
            }

            it.copy(mainQuests = updatedQuests)
        }
    }

    fun changeTaskStatus(
        quest: Quest,
        task: Task
    ) {
        // TODO: I believe it can be made less cumbersome
        _uiState.update {
            // Find necessary task and its status
            val quests = it.mainQuests
            val questIndex = quests.indexOf(quest)
            val tasks = quests[questIndex].tasks
            val taskIndex = tasks.indexOf(task)
            val taskStatus = quests[questIndex].tasks[taskIndex].isCompleted

            // Update task, list of tasks, quest and list of quests
            val updatedTask = tasks[taskIndex].copy(isCompleted = !taskStatus)
            val updatedTasks = tasks.mapIndexed { index, value ->
                if (index == taskIndex) updatedTask else value
            }
            val updatedQuest = quests[questIndex].copy(tasks = updatedTasks)
            val updatedQuests = quests.mapIndexed { index, value ->
                if (index == questIndex) updatedQuest else value
            }

            it.copy(mainQuests = updatedQuests)
        }
    }
}