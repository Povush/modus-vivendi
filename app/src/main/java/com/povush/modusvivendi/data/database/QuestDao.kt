package com.povush.modusvivendi.data.database

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
    suspend fun insert(quest: Quest)

    @Update
    suspend fun update(quest: Quest)

    @Delete
    suspend fun delete(quest: Quest)

    @Query("SELECT * from quests WHERE id = :id")
    fun getQuest(id: Int): Flow<Quest>

    @Query("SELECT * from quests ORDER BY name ASC")
    fun getAllQuestsByName(): Flow<List<Quest>>
}