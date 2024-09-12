package com.povush.modusvivendi.data.repository

import com.povush.modusvivendi.data.db.QuestDao
import com.povush.modusvivendi.data.model.Quest
import com.povush.modusvivendi.data.model.Subtask
import com.povush.modusvivendi.data.model.Task
import kotlinx.coroutines.flow.Flow

class OfflineQuestsRepository(private val questDao: QuestDao) {
    suspend fun insertQuest(quest: Quest) = questDao.insertQuest(quest)
    suspend fun updateQuest(quest: Quest) = questDao.updateQuest(quest)
    suspend fun deleteQuest(quest: Quest) = questDao.deleteQuest(quest)
    suspend fun insertTask(task: Task) = questDao.insertTask(task)
    suspend fun updateTask(task: Task) = questDao.updateTask(task)
    suspend fun deleteTask(task: Task) = questDao.deleteTask(task)
    suspend fun insertSubtask(subtask: Subtask) = questDao.insertSubtask(subtask)
    suspend fun updateSubtask(subtask: Subtask) = questDao.updateSubtask(subtask)
    suspend fun deleteSubtask(subtask: Subtask) = questDao.deleteSubtask(subtask)

    fun getAllQuestsStream(sortingMethod: QuestSortingMethod): Flow<List<Quest>> = when(sortingMethod) {
        QuestSortingMethod.BY_NAME_UP -> questDao.getAllQuestsByNameUp()
        QuestSortingMethod.BY_NAME_DOWN -> questDao.getAllQuestsByNameDown()
        QuestSortingMethod.BY_DIFFICULTY_UP -> questDao.getAllQuestsByDifficultyUp()
        QuestSortingMethod.BY_DIFFICULTY_DOWN -> questDao.getAllQuestsByDifficultyDown()
    }

    fun getAllTasksStream(questId: Int): Flow<List<Task>> =
        questDao.getAllTasksByQuestId(questId)

    fun getAllSubtasksStream(taskId: Int): Flow<List<Subtask>> =
        questDao.getAllSubtasksByTaskId(taskId)

    fun getQuestStreamById(questId: Int): Flow<Quest> = questDao.getQuestStreamById(questId)
    fun getTaskStreamById(taskId: Int): Flow<Task> = questDao.getTaskStreamById(taskId)
    fun getSubtaskStreamById(subtaskId: Int): Flow<Subtask> = questDao.getSubtaskStreamById(subtaskId)
}

enum class QuestSortingMethod {
    BY_NAME_UP,
    BY_NAME_DOWN,
    BY_DIFFICULTY_UP,
    BY_DIFFICULTY_DOWN
}