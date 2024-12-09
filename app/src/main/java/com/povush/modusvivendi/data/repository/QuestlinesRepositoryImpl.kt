package com.povush.modusvivendi.data.repository

import android.util.Log
import com.povush.modusvivendi.data.db.offline_repository.OfflineQuestlinesRepository
import com.povush.modusvivendi.data.model.Quest
import com.povush.modusvivendi.data.model.Task
import com.povush.modusvivendi.data.model.TaskWithSubtasks
import com.povush.modusvivendi.domain.QuestlinesRepository
import com.povush.modusvivendi.data.network.firebase.StorageService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class QuestlinesRepositoryImpl @Inject constructor(
    private val offlineQuestlinesRepository: OfflineQuestlinesRepository,
    private val storageService: StorageService
) : QuestlinesRepository {
    override suspend fun updateQuest(quest: Quest) {
        offlineQuestlinesRepository.updateQuest(quest)
    }

    override suspend fun deleteQuest(quest: Quest) {
        offlineQuestlinesRepository.deleteQuest(quest)
    }

    override suspend fun updateTask(task: Task) {
        offlineQuestlinesRepository.updateTask(task)
    }

    override suspend fun insertQuestAndTasksWithSubtasks(
        oldQuestId: Long,
        quest: Quest,
        tasks: List<TaskWithSubtasks>
    ) {
        offlineQuestlinesRepository.insertQuestAndTasksWithSubtasks(oldQuestId, quest, tasks)
    }

    override suspend fun getTaskScope(task: Task): TaskWithSubtasks? {
        return offlineQuestlinesRepository.getTaskScope(task)
    }

    override fun getAllTasksWithSubtasksStreamByQuestId(questId: Long): Flow<List<TaskWithSubtasks>> {
        return offlineQuestlinesRepository.getAllTasksWithSubtasksStreamByQuestId(questId)
    }

    override fun getAllTasksWithSubtasksByQuestId(questId: Long): List<TaskWithSubtasks> {
        return offlineQuestlinesRepository.getAllTasksWithSubtasksByQuestId(questId)
    }

    override fun getAllTasksWithSubtasks(): Flow<List<TaskWithSubtasks>> {
        return offlineQuestlinesRepository.getAllTasksWithSubtasks()
    }

    override fun getQuestById(questId: Long): Quest {
        return offlineQuestlinesRepository.getQuestById(questId)
    }

    override fun getAllQuests(): Flow<List<Quest>> {
        return offlineQuestlinesRepository.getAllQuests()
    }
}