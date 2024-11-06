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
import kotlinx.coroutines.flow.Flow

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
}