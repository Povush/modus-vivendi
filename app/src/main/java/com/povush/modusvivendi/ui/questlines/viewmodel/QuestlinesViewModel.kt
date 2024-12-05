package com.povush.modusvivendi.ui.questlines.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.povush.modusvivendi.data.model.Quest
import com.povush.modusvivendi.data.model.QuestType
import com.povush.modusvivendi.data.model.QuestWithTasks
import com.povush.modusvivendi.data.model.Task
import com.povush.modusvivendi.data.model.TaskWithSubtasks
import com.povush.modusvivendi.data.db.offline_repository.QuestSortingMethod
import com.povush.modusvivendi.domain.QuestlinesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface QuestlinesUiState {
    data class Success(
        val allQuestsByType: Map<QuestType, List<Quest>> = emptyMap(),
        val allTasksByQuestId: Map<Long, Flow<List<TaskWithSubtasks>>> = emptyMap(),
        val expandedStates: Map<Long, Boolean> = emptyMap(),
        /*TODO: Remember sortingMethod in repository*/
        val sortingMethod: QuestSortingMethod = QuestSortingMethod.BY_DIFFICULTY_DOWN,
        val collapseEnabled: Boolean = false,
        val expandAll: Boolean? = null
    ) : QuestlinesUiState
    data object Loading : QuestlinesUiState
}

@HiltViewModel
class QuestlinesViewModel @Inject constructor(
    private val questlinesRepository: QuestlinesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<QuestlinesUiState>(QuestlinesUiState.Loading)
    val uiState: StateFlow<QuestlinesUiState> = _uiState.asStateFlow()

    var selectedQuestSection = MutableStateFlow(QuestType.MAIN)
        private set

    init {
        loadQuests()
    }

    fun provideTasksByQuestId(questId: Long): Flow<List<TaskWithSubtasks>> {
        return questlinesRepository.getAllTasksWithSubtasksStreamByQuestId(questId)
    }

    private fun loadQuests() {
        viewModelScope.launch {
            delay(750)
            questlinesRepository.getAllQuests().collect { quests ->
                val groupedQuests = quests.groupBy { it.type }
                val currentExpandedStates = (uiState.value as? QuestlinesUiState.Success)?.expandedStates ?: emptyMap()
                val newExpandedStates = groupedQuests.values.flatten().associate { quest ->
                    quest.id to (currentExpandedStates[quest.id] ?: false)
                }

                _uiState.update {
                    QuestlinesUiState.Success(
                        allQuestsByType = groupedQuests,
                        expandedStates = newExpandedStates,
                        allTasksByQuestId = (uiState.value as? QuestlinesUiState.Success)?.allTasksByQuestId
                            ?: emptyMap()
                    )
                }

                groupedQuests.values.flatten().forEach { quest ->
                    loadTasksForQuest(quest.id)
                }
            }
        }
    }

    private fun loadTasksForQuest(questId: Long) {
        viewModelScope.launch {
            val tasksFlow = provideTasksByQuestId(questId)
            _uiState.update { currentState ->
                if (currentState is QuestlinesUiState.Success) {
                    currentState.copy(
                        allTasksByQuestId = currentState.allTasksByQuestId.toMutableMap().apply {
                            this[questId] = tasksFlow
                        }
                    )
                } else currentState
            }
        }
    }

    fun updateCollapseButton() {
        _uiState.update { currentState ->
            if (currentState is QuestlinesUiState.Success) {
                val collapseEnabled = currentState.expandedStates.any { it.value }
                currentState.copy(collapseEnabled = collapseEnabled)
            } else currentState
        }
    }

    fun toggleExpandButton() {
        _uiState.update { currentState ->
            if (currentState is QuestlinesUiState.Success) {
                val updatedExpandedStates = currentState.expandedStates.mapValues {
                    !currentState.collapseEnabled
                }
                currentState.copy(expandedStates = updatedExpandedStates)
            } else currentState
        }
    }

    fun checkCompletionStatus(questWithTasks: QuestWithTasks) {
        val quest = questWithTasks.quest
        val tasks = questWithTasks.tasks
        val isCompleted = tasks.isNotEmpty() && tasks.all {
            (!it.task.isAdditional && it.task.isCompleted) || it.task.isAdditional
        }

        if (isCompleted != quest.isCompleted) {
            viewModelScope.launch(Dispatchers.IO) {
                questlinesRepository.updateQuest(quest.copy(isCompleted = isCompleted))
            }

            _uiState.update { currentState ->
                if (currentState is QuestlinesUiState.Success) {
                    currentState.copy(
                        allQuestsByType = updateQuestCompletionStatus(
                            currentState.allQuestsByType, quest, isCompleted
                        )
                    )
                } else currentState
            }
        }
    }

    private fun updateQuestCompletionStatus(
        questsByType: Map<QuestType, List<Quest>>,
        quest: Quest,
        isCompleted: Boolean
    ): Map<QuestType, List<Quest>> {
        return questsByType.toMutableMap().apply {
            val updatedQuests = this[quest.type]?.map {
                if (it.id == quest.id) it.copy(isCompleted = isCompleted) else it
            } ?: emptyList()
            this[quest.type] = updatedQuests
        }
    }


    fun completeQuest(quest: Quest) {
        viewModelScope.launch {
            questlinesRepository.updateQuest(quest.copy(type = QuestType.COMPLETED))
        }
    }

    fun updateTaskStatus(task: Task, isCompleted: Boolean, subtasks: List<Task>? = null): Boolean {
        subtasks?.let {
            if (subtasks.any { subtask -> !subtask.isCompleted && !subtask.isAdditional }) {
                return false
            }
        }

        viewModelScope.launch(Dispatchers.IO) {
            val taskScope = questlinesRepository.getTaskScope(task)

            questlinesRepository.updateTask(task.copy(isCompleted = isCompleted))

            taskScope?.let {
                if (task.parentTaskId != null && !task.isAdditional && taskScope.task.isCompleted && !isCompleted) {
                    questlinesRepository.updateTask(taskScope.task.copy(isCompleted = false))
                }
            }

        }
        return true
    }

    fun deleteQuest(quest: Quest) {
        viewModelScope.launch {
            questlinesRepository.deleteQuest(quest)
        }
    }

    fun changeQuestExpandStatus(quest: Quest) {
        _uiState.update { currentState ->
            if (currentState is QuestlinesUiState.Success) {
                val updatedExpandedStates = currentState.expandedStates.toMutableMap().apply {
                    this[quest.id] = !(this[quest.id] ?: false)
                }
                currentState.copy(expandedStates = updatedExpandedStates)
            } else currentState
        }
    }

    fun switchQuestSection(index: Int) {
        if (index in QuestType.entries.indices) {
            selectedQuestSection.value = QuestType.entries[index]
        }
    }

    fun sectionCounter(index: Int): Int {
        val currentState = uiState.value
        return if (currentState is QuestlinesUiState.Success) {
            currentState.allQuestsByType[QuestType.entries.getOrNull(index)]?.size ?: 0
        } else 0
    }

    fun changeSortingMethod(sortingMethod: QuestSortingMethod) {
        _uiState.update { currentState ->
            if (currentState is QuestlinesUiState.Success) {
                currentState.copy(sortingMethod = sortingMethod)
            } else currentState
        }
    }
}