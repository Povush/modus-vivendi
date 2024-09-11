package com.povush.modusvivendi.data.repository

import com.povush.modusvivendi.data.model.Quest
import kotlinx.coroutines.flow.Flow

class OfflineQuestsRepository : QuestsRepository {
    override suspend fun insertQuest(item: Quest) {
        TODO("Not yet implemented")
    }

    override suspend fun updateQuest(item: Quest) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteQuest(item: Quest) {
        TODO("Not yet implemented")
    }

    override fun getQuestStream(id: Int): Flow<Quest?> {
        TODO("Not yet implemented")
    }

    override fun getAllQuestsStream(): Flow<List<Quest>> {
        TODO("Not yet implemented")
    }
}