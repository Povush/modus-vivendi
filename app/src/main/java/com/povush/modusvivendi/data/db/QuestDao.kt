package com.povush.modusvivendi.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.povush.modusvivendi.data.model.Quest
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

    @Query("SELECT * FROM quests ORDER BY name ASC")
    fun getAllQuestsByNameUp(): Flow<List<Quest>>

    @Query("SELECT * FROM quests ORDER BY name DESC")
    fun getAllQuestsByNameDown(): Flow<List<Quest>>

    @Query("SELECT * FROM quests ORDER BY difficulty ASC")
    fun getAllQuestsByDifficultyUp(): Flow<List<Quest>>

    @Query("SELECT * FROM quests ORDER BY difficulty DESC")
    fun getAllQuestsByDifficultyDown(): Flow<List<Quest>>

    @Query("SELECT * FROM quests WHERE id = :questId")
    fun getQuestStreamById(questId: Long): Flow<Quest>
}