package com.povush.modusvivendi.data.db.offline_repository

import com.povush.modusvivendi.data.db.dao.QuestDao
import com.povush.modusvivendi.data.db.dao.TaskDao
import com.povush.modusvivendi.data.model.Quest
import com.povush.modusvivendi.data.model.Task
import com.povush.modusvivendi.data.model.TaskWithSubtasks
import kotlinx.coroutines.flow.Flow

class OfflineQuestlinesRepository(private val questDao: QuestDao, private val taskDao: TaskDao) {
    suspend fun updateQuest(quest: Quest) = questDao.updateQuest(quest)
    suspend fun deleteQuest(quest: Quest) = questDao.deleteQuest(quest)
    suspend fun updateTask(task: Task) = taskDao.updateTask(task)

    suspend fun insertQuestAndTasksWithSubtasks(
        oldQuestId: Long,
        quest: Quest,
        tasks: List<TaskWithSubtasks>
    ) {
        questDao.insertQuestAndTasksWithSubtasks(oldQuestId, quest, tasks, taskDao)
    }

    fun getAllTasksWithSubtasksStreamByQuestId(questId: Long): Flow<List<TaskWithSubtasks>> =
        taskDao.getAllTasksWithSubtasksStreamByQuestId(questId)

    fun getAllTasksWithSubtasksByQuestId(questId: Long): List<TaskWithSubtasks> =
        taskDao.getAllTasksWithSubtasksByQuestId(questId)

    fun getQuestById(questId: Long): Quest = questDao.getQuestById(questId)
    fun getAllQuests(): Flow<List<Quest>> = questDao.getAllQuests()
}

enum class QuestSortingMethod {
    BY_NAME_UP,
    BY_NAME_DOWN,
    BY_DIFFICULTY_UP,
    BY_DIFFICULTY_DOWN
}