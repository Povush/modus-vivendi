package com.povush.modusvivendi.ui.questlines.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.povush.modusvivendi.data.model.Quest
import com.povush.modusvivendi.data.model.QuestType
import com.povush.modusvivendi.data.model.QuestWithTasks
import com.povush.modusvivendi.data.model.Task
import com.povush.modusvivendi.data.model.TaskWithSubtasks
import com.povush.modusvivendi.data.repository.OfflineQuestsRepository
import com.povush.modusvivendi.data.repository.QuestSortingMethod
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

data class QuestlinesUiState(
    val allQuestsByType: Map<QuestType, List<Quest>> = emptyMap(),
    val allTasksByQuestId: Map<Long, List<TaskWithSubtasks>> = emptyMap(),
    val expandedStates: Map<Long, Boolean> = emptyMap(),
    val selectedQuestSection: QuestType = QuestType.MAIN,
    /*TODO: Remember sortingMethod in repository*/
    val sortingMethod: QuestSortingMethod = QuestSortingMethod.BY_DIFFICULTY_DOWN,
    val collapseEnabled: Boolean = false,
    val expandAll: Boolean? = null
)

@HiltViewModel
class QuestlinesViewModel @Inject constructor(
    private val questsRepository: OfflineQuestsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(QuestlinesUiState())
    val uiState: StateFlow<QuestlinesUiState> = _uiState.asStateFlow()


    init {
        loadQuests()
    }

    private fun loadQuests() {
        viewModelScope.launch {
            questsRepository.getAllQuests().collect { quests ->
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
                        questsRepository.getAllTasksWithSubtasksStreamByQuestId(quest.id).collect { tasks ->
                            _uiState.update {
                                it.copy(
                                    allTasksByQuestId = it.allTasksByQuestId.toMutableMap().apply {
                                        this[quest.id] = tasks
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    fun checkCompletionStatus(questWithTasks: QuestWithTasks) {
        viewModelScope.launch(Dispatchers.Default) {
            val quest = questWithTasks.quest
            val tasks = questWithTasks.tasks
            val isCompleted = tasks.isNotEmpty() && tasks.map { it.task }.all { it.isCompleted }

            if (isCompleted != quest.isCompleted) {
                withContext(Dispatchers.IO) {
                    questsRepository.updateQuest(quest.copy(isCompleted = isCompleted))
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
    }

    fun completeQuest(quest: Quest) {
        viewModelScope.launch {
            questsRepository.updateQuest(quest.copy(type = QuestType.COMPLETED))
        }
    }

    fun updateTaskStatus(task: Task, isCompleted: Boolean): Boolean {
        val taskScope = uiState.value.allTasksByQuestId[task.questId]
            ?.find { it.task.id == task.id || task.id in it.subtasks.map { subtask -> subtask.id } }

        if (taskScope == null) return true
        if (task.parentTaskId == null &&                                                                        // Task with uncompleted non-additional subtasks
            taskScope.subtasks.any { subtask -> !subtask.isCompleted && !subtask.isAdditional }) {
            return false
        }
        if (task.parentTaskId != null && !task.isAdditional && taskScope.task.isCompleted && !isCompleted) {    // Non-additional subtask with completed parent task
            viewModelScope.launch {
                questsRepository.updateTask(taskScope.task.copy(isCompleted = false))
            }
        }

        viewModelScope.launch {
            questsRepository.updateTask(task.copy(isCompleted = isCompleted))
        }
        return true
    }

    fun deleteQuest(quest: Quest) {
        viewModelScope.launch {
            questsRepository.deleteQuest(quest)
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

    fun collapseAll() {
        viewModelScope.launch {
            _uiState.update { it.copy(expandAll = false) }
            delay(1000)
            _uiState.update { it.copy(expandAll = null) }
        }
    }

    fun expandAll() {
        viewModelScope.launch {
            _uiState.update { it.copy(expandAll = true) }
            delay(1000)
            _uiState.update { it.copy(expandAll = null) }
        }
    }

//    fun onExpandToggle(questId: Long, isExpanded: Boolean) {
//        _expandedQuests.update { expandedQuests ->
//            if (isExpanded) expandedQuests + questId
//            else expandedQuests - questId
//        }
//    }

    fun changeCollapseEnabled(isEnabled: Boolean) {
        _uiState.update { it.copy(collapseEnabled = isEnabled) }
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