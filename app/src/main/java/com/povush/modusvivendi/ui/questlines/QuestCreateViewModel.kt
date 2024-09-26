package com.povush.modusvivendi.ui.questlines

import androidx.lifecycle.ViewModel
import com.povush.modusvivendi.data.model.Difficulty
import com.povush.modusvivendi.data.model.Quest
import com.povush.modusvivendi.data.model.QuestType
import com.povush.modusvivendi.data.model.Subtask
import com.povush.modusvivendi.data.model.Task
import com.povush.modusvivendi.data.repository.OfflineQuestsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class QuestCreateUiState(
    val name: String = "",

    /*TODO: From screen that we go here*/
    val type: QuestType = QuestType.ADDITIONAL,
    val typeExpanded: Boolean = false,

    val difficulty: Difficulty = Difficulty.MEDIUM,
    val description: String = "",
    val tasks: Map<Task, List<Subtask>> = emptyMap()
)

class QuestCreateViewModel(
    private val questsRepository: OfflineQuestsRepository,
    private val currentQuestSectionNumber: Int
) : ViewModel() {
    private val _uiState = MutableStateFlow(QuestCreateUiState(type = QuestType.entries[currentQuestSectionNumber]))
    val uiState: StateFlow<QuestCreateUiState> = _uiState.asStateFlow()

    fun updateQuestName(input: String) {
        _uiState.update {
            uiState.value.copy(name = input)
        }
    }

    fun updateType(questType: QuestType) {
        _uiState.update {
            uiState.value.copy(type = questType)
        }
    }

    fun updateTypeExpanded(typeExpanded: Boolean) {
        _uiState.update {
            uiState.value.copy(typeExpanded = typeExpanded)
        }
    }

    fun updateDifficulty(difficulty: Difficulty) {
        _uiState.update {
            uiState.value.copy(difficulty = difficulty)
        }
    }

    fun updateDescription(input: String) {
        _uiState.update {
            uiState.value.copy(description = input)
        }
    }

    fun updateTaskStatus(task: Task) {
        val subtasksOfThisTask = uiState.value.tasks[task] ?: emptyList()
        val updatedTask = task.copy(isCompleted = !task.isCompleted)
        val updatedTasks = uiState.value.tasks
            .minus(task)
            .plus(updatedTask to subtasksOfThisTask)

        _uiState.update {
            uiState.value.copy(
                tasks = updatedTasks
            )
        }
    }

    /*TODO: CURSED CURSED CURSED!*/
    fun updateSubtaskStatus(subtask: Subtask) {
        val taskOfThisSubtask: Task = uiState.value.tasks.keys.firstOrNull {
            subtask in (uiState.value.tasks[it] ?: emptyList())
        } ?: Task(questId = 0)
        val otherSubtasks: List<Subtask> = uiState.value.tasks[taskOfThisSubtask] ?: emptyList()
        val updatedSubtask = subtask.copy(isCompleted = !subtask.isCompleted)
        val updatedSubtasks = otherSubtasks.map { item ->
            if (item == subtask) updatedSubtask else item
        }
        val updatedTasks = uiState.value.tasks
            .plus(taskOfThisSubtask to updatedSubtasks)

        _uiState.update {
            uiState.value.copy(
                tasks = updatedTasks
            )
        }
    }
}