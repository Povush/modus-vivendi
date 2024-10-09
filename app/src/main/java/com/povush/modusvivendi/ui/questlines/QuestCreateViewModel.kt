package com.povush.modusvivendi.ui.questlines

import androidx.lifecycle.ViewModel
import com.povush.modusvivendi.data.model.Difficulty
import com.povush.modusvivendi.data.model.QuestType
import com.povush.modusvivendi.data.model.Task
import com.povush.modusvivendi.data.repository.OfflineQuestsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class QuestCreateUiState(
    val isValid: Boolean = false,
    val name: String = "",

    /*TODO: From screen that we go here*/
    val type: QuestType = QuestType.ADDITIONAL,
    val typeExpanded: Boolean = false,

    val difficulty: Difficulty = Difficulty.MEDIUM,
    val description: String = "",
    val tasks: Map<Task, List<Task>> = emptyMap()
)

class QuestCreateViewModel(
    private val questsRepository: OfflineQuestsRepository,
    private val currentQuestSectionNumber: Int
) : ViewModel() {
    private val _uiState = MutableStateFlow(QuestCreateUiState(
        type = QuestType.entries[currentQuestSectionNumber],
        tasks = mapOf(
            com.povush.modusvivendi.data.model.Task(id = 11, questId = 0, name = "Veeeeeeery-very-very loooooooooooooooooooooong task 1 and its huge description (add'l)") to emptyList(),
            com.povush.modusvivendi.data.model.Task(id = 12, questId = 0, name = "Task 2") to listOf(com.povush.modusvivendi.data.model.Task(id = 1, parentTaskId = 12, name = "Subtask 1", questId = 0), com.povush.modusvivendi.data.model.Task(id = 2, parentTaskId = 12, name = "Subtask 2", questId = 0)),
            com.povush.modusvivendi.data.model.Task(id = 13, questId = 0, name = "Task 3") to emptyList()
        )
    ))
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

    fun updateTaskStatus(task: Task, isCompleted: Boolean) {
//        val subtasks = uiState.value.tasks[task].orEmpty()
//        val updatedTask = task.copy(isCompleted = isCompleted)
//        val updatedTasks = uiState.value.tasks
//            .minus(task)
//            .plus(updatedTask to subtasks)
//            .toList()
//            .sortedBy { (task, _) -> task.orderIndex }
//            .toMap()
//
//        _uiState.update {
//            it.copy(tasks = updatedTasks)
//        }
    }
}