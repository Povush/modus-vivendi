package com.povush.modusvivendi.data.model

import androidx.lifecycle.ViewModel
import com.povush.modusvivendi.data.dataclass.Difficulty
import com.povush.modusvivendi.data.dataclass.Quest
import com.povush.modusvivendi.data.dataclass.QuestType
import com.povush.modusvivendi.data.dataclass.Task
import com.povush.modusvivendi.data.state.QuestlinesUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.Date

class QuestlinesViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(QuestlinesUiState())
    val uiState: StateFlow<QuestlinesUiState> = _uiState.asStateFlow()

    fun onTabClick(index: Int) {
        _uiState.update {
            it.copy(selectedQuestSection = index)
        }
    }

    fun questRebuilder(
        quest: Quest,
        updatedTitle: String? = null,
        updatedDifficulty: Difficulty? = null,
        updatedDescription: String? = null,
        updatedTasks: List<Task>? = null,
        updatedIsCompleted: Boolean? = null,
        updatedDateOfCompletion: Date? = null,
        updatedExpanded: Boolean? = null,
        updatedType: QuestType? = null
    ) {
        // Build new quest
        val updatedQuest = quest.copy(
            title = updatedTitle ?: quest.title,
            difficulty = updatedDifficulty ?: quest.difficulty,
            description = updatedDescription ?: quest.description,
            tasks = updatedTasks ?: quest.tasks,
            isCompleted = updatedIsCompleted ?: quest.isCompleted,
            dateOfCompletion = updatedDateOfCompletion ?: quest.dateOfCompletion,
            expanded = updatedExpanded ?: quest.expanded,
            type = updatedType ?: quest.type
        )

        // Find necessary quests lists
        val oldQuestsList = when (quest.type) {
            QuestType.Main -> uiState.value.mainQuests
            QuestType.Additional -> uiState.value.additionalQuests
            QuestType.Completed -> uiState.value.completedQuests
            QuestType.Failed -> uiState.value.failedQuests
        }
        val newQuestList = when (updatedType) {
            QuestType.Main -> uiState.value.mainQuests
            QuestType.Additional -> uiState.value.additionalQuests
            QuestType.Completed -> uiState.value.completedQuests
            QuestType.Failed -> uiState.value.failedQuests
            null -> oldQuestsList
        }

        // Getting new versions of the quests lists
        val updatedOldQuestsList = oldQuestsList.filterNot { it === quest }
        val updatedNewQuestsList = newQuestList + updatedQuest

        // TODO: The sorting method according to the current sorting criterion

        _uiState.update { currentState ->
            when (quest.type) {
                QuestType.Main -> currentState.copy(mainQuests = updatedOldQuestsList)
                QuestType.Additional -> currentState.copy(additionalQuests = updatedOldQuestsList)
                QuestType.Completed -> currentState.copy(completedQuests = updatedOldQuestsList)
                QuestType.Failed -> currentState.copy(failedQuests = updatedOldQuestsList)
            }
        }
        _uiState.update { currentState ->
            when (updatedQuest.type) {
                QuestType.Main -> currentState.copy(mainQuests = updatedNewQuestsList)
                QuestType.Additional -> currentState.copy(additionalQuests = updatedNewQuestsList)
                QuestType.Completed -> currentState.copy(completedQuests = updatedNewQuestsList)
                QuestType.Failed -> currentState.copy(failedQuests = updatedNewQuestsList)
            }
        }
    }

    // TODO: I believe it can be made less cumbersome
    fun changeTaskStatus(
        quest: Quest,
        task: Task
    ) {
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

// help help