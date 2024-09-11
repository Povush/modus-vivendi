package com.povush.modusvivendi.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.povush.modusvivendi.data.model.Quest
import com.povush.modusvivendi.data.model.Task
import com.povush.modusvivendi.data.model.relations.QuestWithTasks
import com.povush.modusvivendi.data.model.relations.TaskWithSubTasks
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

    /**
     * Functions for working with Tasks.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTask(task: Task)

    @Update
    suspend fun updateTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)

    @Query("SELECT * FROM tasks")
    fun getAllTasks(questId: Int): Flow<List<Quest>>
}





