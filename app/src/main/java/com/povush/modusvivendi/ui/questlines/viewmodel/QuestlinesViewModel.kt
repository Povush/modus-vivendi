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

data class QuestlinesUiState(
    val allQuestsByType: Map<QuestType, List<Quest>> = emptyMap(),
    val allTasksByQuestId: Map<Long, Flow<List<TaskWithSubtasks>>> = emptyMap(),
    val expandedStates: Map<Long, Boolean> = emptyMap(),
    val selectedQuestSection: QuestType = QuestType.MAIN,
    /*TODO: Remember sortingMethod in repository*/
    val sortingMethod: QuestSortingMethod = QuestSortingMethod.BY_DIFFICULTY_DOWN,
    val collapseEnabled: Boolean = false,
    val expandAll: Boolean? = null
)

@HiltViewModel
class QuestlinesViewModel @Inject constructor(
    private val questlinesRepository: QuestlinesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(QuestlinesUiState())
    val uiState: StateFlow<QuestlinesUiState> = _uiState.asStateFlow()

    init {
        loadQuests()
    }

    fun provideTasksByQuestId(questId: Long): Flow<List<TaskWithSubtasks>> {
        return questlinesRepository.getAllTasksWithSubtasksStreamByQuestId(questId)
    }

    private fun loadQuests() {
        viewModelScope.launch {
            questlinesRepository.getAllQuests().collect { quests ->
                val groupedQuests = quests.groupBy { it.type }
                val currentExpandedStates = uiState.value.expandedStates

                _uiState.update {
                    it.copy(
                        allQuestsByType = groupedQuests,
                        expandedStates = groupedQuests.values.flatten().associate { quest ->
                            quest.id to (currentExpandedStates[quest.id] ?: false)
                        }
                    )
                }

                groupedQuests.values.flatten().forEach { quest ->
                    launch {
                        _uiState.update {
                            it.copy(
                                allTasksByQuestId = it.allTasksByQuestId.toMutableMap().apply {
                                    this[quest.id] = provideTasksByQuestId(quest.id)
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    fun updateCollapseButton() {
        if (uiState.value.expandedStates.any { it.value }) {
            _uiState.update {
                it.copy(collapseEnabled = true)
            }
        } else {
            _uiState.update {
                it.copy(collapseEnabled = false)
            }
        }
    }

    fun toggleExpandButton() {
        val updatedExpandedStates = uiState.value.expandedStates
            .mapValues { !uiState.value.collapseEnabled }

        _uiState.update {
            it.copy(expandedStates = updatedExpandedStates)
        }
    }

    fun checkCompletionStatus(questWithTasks: QuestWithTasks) {
        val quest = questWithTasks.quest
        val tasks = questWithTasks.tasks
        val isCompleted = tasks.isNotEmpty() && tasks.map { it.task }.all { it.isCompleted }

        if (isCompleted != quest.isCompleted) {
            viewModelScope.launch(Dispatchers.IO) {
                questlinesRepository.updateQuest(quest.copy(isCompleted = isCompleted))
            }

            _uiState.update { currentState ->
                currentState.copy(
                    allQuestsByType = currentState.allQuestsByType.toMutableMap().apply {
                        val quests = this[quest.type]?.toMutableList()
                        val questIndex = quests?.indexOfFirst { it.id == quest.id }
                        if (questIndex != null && questIndex >= 0) {
                            quests[questIndex] = quest.copy(isCompleted = isCompleted)
                            this[quest.type] = quests.toList()
                        }
                    }
                )
            }
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
            currentState.copy(
                expandedStates = currentState.expandedStates.mapValues { (id, isExpanded) ->
                    if (id == quest.id) {
                        !isExpanded
                    } else {
                        isExpanded
                    }
                }
            )
        }
    }

    fun switchQuestSection(index: Int) {
        _uiState.update { it.copy(selectedQuestSection = QuestType.entries[index]) }
    }

    fun sectionCounter(index: Int): Int {
        val numberOfQuests = uiState.value.allQuestsByType[QuestType.entries[index]]?.size
        return numberOfQuests ?: 0
    }

    fun changeSortingMethod(sortingMethod: QuestSortingMethod) {
        _uiState.update { it.copy(sortingMethod = sortingMethod) }
    }
}