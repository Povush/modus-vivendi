package com.povush.modusvivendi.data.repository

import com.povush.modusvivendi.data.model.Quest
import kotlinx.coroutines.flow.Flow

interface QuestsRepository {
    suspend fun insertQuest(item: Quest)

    suspend fun updateQuest(item: Quest)

    suspend fun deleteQuest(item: Quest)

    fun getQuestStream(id: Int): Flow<Quest?>

    fun getAllQuestsStream(): Flow<List<Quest>>
}
