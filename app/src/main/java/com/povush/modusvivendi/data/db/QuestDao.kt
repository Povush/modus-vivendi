package com.povush.modusvivendi.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.povush.modusvivendi.data.model.Quest
import com.povush.modusvivendi.data.model.Subtask
import com.povush.modusvivendi.data.model.Task
import kotlinx.coroutines.flow.Flow

@Dao
interface QuestDao {
    /**
     * Functions for working with Quests.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertQuest(quest: Quest)

    @Update
    suspend fun updateQuest(quest: Quest)

    @Delete
    suspend fun deleteQuest(quest: Quest)

    @Query("SELECT * FROM quests ORDER BY name ASC")
    fun getAllQuestsByNameUp(): Flow<List<Quest>>

    @Query("SELECT * FROM quests ORDER BY name DESC")
    fun getAllQuestsByNameDown(): Flow<List<Quest>>

    @Query("SELECT * FROM quests ORDER BY difficulty ASC")
    fun getAllQuestsByDifficultyUp(): Flow<List<Quest>>

    @Query("SELECT * FROM quests ORDER BY difficulty DESC")
    fun getAllQuestsByDifficultyDown(): Flow<List<Quest>>

    @Query("SELECT * FROM quests WHERE id = :questId")
    fun getQuestStreamById(questId: Int): Flow<Quest>

    /**
     * Functions for working with Tasks.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTask(task: Task)

    @Update
    suspend fun updateTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)

    @Query("SELECT * FROM tasks WHERE questId = :questId")
    fun getAllTasksByQuestId(questId: Int): Flow<List<Task>>

    /**
     * Functions for working with Subtasks.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSubtask(subtask: Subtask)

    @Update
    suspend fun updateSubtask(subtask: Subtask)

    @Delete
    suspend fun deleteSubtask(subtask: Subtask)

    @Query("SELECT * FROM subtasks WHERE taskId = :taskId")
    fun getAllSubtasksByTaskId(taskId: Int): Flow<List<Subtask>>
}