package com.povush.modusvivendi.data.repository

import androidx.room.Transaction
import com.povush.modusvivendi.data.db.dao.QuestDao
import com.povush.modusvivendi.data.db.dao.TaskDao
import com.povush.modusvivendi.data.model.Quest
import com.povush.modusvivendi.data.model.QuestType
import com.povush.modusvivendi.data.model.QuestWithTasks
import com.povush.modusvivendi.data.model.Task
import com.povush.modusvivendi.data.model.TaskWithSubtasks
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class OfflineQuestsRepository(private val questDao: QuestDao, private val taskDao: TaskDao) {
    suspend fun insertQuest(quest: Quest): Long = questDao.insertQuest(quest)
    suspend fun updateQuest(quest: Quest) = questDao.updateQuest(quest)
    suspend fun deleteQuest(quest: Quest) = questDao.deleteQuest(quest)
    suspend fun deleteQuestById(questId: Long) = questDao.deleteQuestById(questId)

    suspend fun insertTask(task: Task): Long = taskDao.insertTask(task)
    suspend fun updateTask(task: Task) = taskDao.updateTask(task)

    fun getAllQuestsStream(sortingMethod: QuestSortingMethod): Flow<List<QuestWithTasks>> = when(sortingMethod) {
        QuestSortingMethod.BY_NAME_UP -> questDao.getAllQuestsByNameUp()
        QuestSortingMethod.BY_NAME_DOWN -> questDao.getAllQuestsByNameDown()
        QuestSortingMethod.BY_DIFFICULTY_UP -> questDao.getAllQuestsByDifficultyUp()
        QuestSortingMethod.BY_DIFFICULTY_DOWN -> questDao.getAllQuestsByDifficultyDown()
    }
//    fun getAllQuestsWithTasks(questTypeOrdinal: Int): Flow<List<QuestWithTasks>> =
//        questDao.getAllQuestsWithTasks(questTypeOrdinal)
    fun getAllTasksWithSubtasksStreamByQuestId(questId: Long): Flow<List<TaskWithSubtasks>> =
        taskDao.getAllTasksWithSubtasksStreamByQuestId(questId)
    fun getAllTasksWithSubtasksByQuestId(questId: Long): List<TaskWithSubtasks> =
        taskDao.getAllTasksWithSubtasksByQuestId(questId)
    fun getQuestById(questId: Long): Quest = questDao.getQuestById(questId)

    fun getAllQuestsWithTasks(): Flow<List<QuestWithTasks>> =
        questDao.getAllQuestsWithTasks()
    fun getAllQuests(): Flow<List<Quest>> = questDao.getAllQuests()

    suspend fun insertQuestAndTasksWithSubtasks(
        oldQuestId: Long,
        quest: Quest,
        tasks: List<TaskWithSubtasks>
    ) {
        questDao.insertQuestAndTasksWithSubtasks(oldQuestId, quest, tasks, taskDao)
    }


}

enum class QuestSortingMethod {
    BY_NAME_UP,
    BY_NAME_DOWN,
    BY_DIFFICULTY_UP,
    BY_DIFFICULTY_DOWN
}