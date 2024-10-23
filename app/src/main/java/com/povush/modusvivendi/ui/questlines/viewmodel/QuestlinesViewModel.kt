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
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class QuestlinesUiState(
    val allQuestsByType: Map<QuestType, List<QuestWithTasks>> = emptyMap(),
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
            questsRepository.getAllQuestsWithTasks().collect { quests ->
                val groupedQuests = quests.groupBy { it.quest.type }
                val expandedStates = quests.associate { it.quest.id to false }

                _uiState.update {
                    it.copy(
                        allQuestsByType = groupedQuests,
                        expandedStates = expandedStates
                    )
                }
            }
        }
    }

    fun updateTaskStatus(task: Task, isCompleted: Boolean): Boolean {

//        uiState.value.allQuestsByType.mapValues { (questType, quests) ->
//            if (task.id in quests.map { it.tasks })
//        }
//
//
//        if (isCompleted && task.parentTaskId == null) {                                             // For task with uncompleted non-additional subtasks
//            uiState.value.tasks.find { it.task.id == task.id }.also {
//                val canBeCompleted = it?.subtasks
//                    ?.none { subtask -> !subtask.isCompleted && !subtask.isAdditional } ?: true
//                if (!canBeCompleted) return false
//            }
//        } else if (!isCompleted && task.parentTaskId != null && !task.isAdditional) {               // For non-additional subtask with completed parent task
//            uiState.value.tasks
//                .find { taskWithSubtasks -> task.id in taskWithSubtasks.subtasks.map { it.id } }
//                .also { val parentTask = it?.task
//                    val parentTaskCompleted = parentTask?.isCompleted ?: false
//                    if (parentTaskCompleted) {
//                        viewModelScope.launch {
//                            if (parentTask != null) {
//                                questsRepository.updateTask(parentTask.copy(isCompleted = false))
//                            }
//                        }
//                    }
//                }
//        }
//        viewModelScope.launch {
//            questsRepository.updateTask(task.copy(isCompleted = isCompleted))
//        }
//        return true

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
