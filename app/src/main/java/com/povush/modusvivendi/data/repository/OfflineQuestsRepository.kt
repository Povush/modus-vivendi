package com.povush.modusvivendi.data.repository

import com.povush.modusvivendi.data.db.QuestDao
import com.povush.modusvivendi.data.db.TaskDao
import com.povush.modusvivendi.data.model.Quest
import com.povush.modusvivendi.data.model.Task
import com.povush.modusvivendi.data.model.TaskWithSubtasks
import kotlinx.coroutines.flow.Flow

class OfflineQuestsRepository(private val questDao: QuestDao, private val taskDao: TaskDao) {
    suspend fun insertQuest(quest: Quest) = questDao.insertQuest(quest)
    suspend fun updateQuest(quest: Quest) = questDao.updateQuest(quest)
    suspend fun deleteQuest(quest: Quest) = questDao.deleteQuest(quest)
    suspend fun deleteQuestById(questId: Long) = questDao.deleteQuestById(questId)

    suspend fun insertTask(task: Task) = taskDao.insertTask(task)
    suspend fun updateTask(task: Task) = taskDao.updateTask(task)
    suspend fun deleteTask(task: Task) = taskDao.deleteTask(task)

    fun getAllQuestsStream(sortingMethod: QuestSortingMethod): Flow<List<Quest>> = when(sortingMethod) {
        QuestSortingMethod.BY_NAME_UP -> questDao.getAllQuestsByNameUp()
        QuestSortingMethod.BY_NAME_DOWN -> questDao.getAllQuestsByNameDown()
        QuestSortingMethod.BY_DIFFICULTY_UP -> questDao.getAllQuestsByDifficultyUp()
        QuestSortingMethod.BY_DIFFICULTY_DOWN -> questDao.getAllQuestsByDifficultyDown()
    }
    fun getAllTasksStream(questId: Long): Flow<List<Task>> =
        taskDao.getAllTasksByQuestId(questId)

    fun getAllTasksWithSubtasksByQuestId(questId: Long): Flow<List<TaskWithSubtasks>> =
        taskDao.getAllTasksWithSubtasksByQuestId(questId)

    fun getQuestStreamById(questId: Long): Flow<Quest> = questDao.getQuestStreamById(questId)
    fun getTaskStreamById(taskId: Long): Flow<Task> = taskDao.getTaskStreamById(taskId)

    suspend fun deleteTasksByQuestId(questId: Long) = taskDao.deleteTasksByQuestId(questId)
}

enum class QuestSortingMethod {
    BY_NAME_UP,
    BY_NAME_DOWN,
    BY_DIFFICULTY_UP,
    BY_DIFFICULTY_DOWN
}