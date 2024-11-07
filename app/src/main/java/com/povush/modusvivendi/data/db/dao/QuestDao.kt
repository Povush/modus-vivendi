package com.povush.modusvivendi.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.povush.modusvivendi.data.model.Quest
import com.povush.modusvivendi.data.model.QuestWithTasks
import com.povush.modusvivendi.data.model.Task
import com.povush.modusvivendi.data.model.TaskWithSubtasks
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

@Dao
interface QuestDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertQuest(quest: Quest): Long

    @Update
    suspend fun updateQuest(quest: Quest)

    @Delete
    suspend fun deleteQuest(quest: Quest)

    @Query("DELETE FROM quests WHERE id = :questId")
    suspend fun deleteQuestById(questId: Long)

    @Transaction
    @Query("SELECT * FROM quests")
    fun getAllQuests(): Flow<List<Quest>>

    @Transaction
    @Query("SELECT * FROM quests ORDER BY name ASC")
    fun getAllQuestsByNameUp(): Flow<List<QuestWithTasks>>

    @Transaction
    @Query("SELECT * FROM quests ORDER BY name DESC")
    fun getAllQuestsByNameDown(): Flow<List<QuestWithTasks>>

    @Transaction
    @Query("SELECT * FROM quests ORDER BY difficulty ASC")
    fun getAllQuestsByDifficultyUp(): Flow<List<QuestWithTasks>>

    @Transaction
    @Query("SELECT * FROM quests ORDER BY difficulty DESC")
    fun getAllQuestsByDifficultyDown(): Flow<List<QuestWithTasks>>

    @Query("SELECT * FROM quests WHERE id = :questId")
    fun getQuestStreamById(questId: Long): Flow<Quest>

    @Query("SELECT * FROM quests WHERE id = :questId")
    fun getQuestById(questId: Long): Quest

    @Transaction
    @Query("SELECT * FROM quests")
    fun getAllQuestsWithTasks(): Flow<List<QuestWithTasks>>

    @Transaction
    suspend fun insertQuestAndTasksWithSubtasks(
        oldQuestId: Long,
        quest: Quest,
        tasks: List<TaskWithSubtasks>,
        taskDao: TaskDao
    ) {
        if (oldQuestId != -1L) deleteQuestById(oldQuestId)
        val questId = insertQuest(Quest(
            name = quest.name,
            type = quest.type,
            difficulty = quest.difficulty,
            description = quest.description,
            isCompleted = quest.isCompleted
        ))

        tasks.forEach { taskWithSubtasks ->
            val task = taskWithSubtasks.task
            val subtasks = taskWithSubtasks.subtasks
            val parentTaskId = taskDao.insertTask(Task(
                questId = questId,
                name = task.name,
                isCompleted = task.isCompleted,
                counter = task.counter,
                isAdditional = task.isAdditional,
                orderIndex = task.orderIndex
            ))
            subtasks.forEach { subtask ->
                taskDao.insertTask(Task(
                    questId = questId,
                    parentTaskId = parentTaskId,
                    name = subtask.name,
                    isCompleted = subtask.isCompleted,
                    counter = subtask.counter,
                    isAdditional = subtask.isAdditional,
                    orderIndex = subtask.orderIndex
                ))
            }
        }
    }
}