package com.povush.modusvivendi.domain

import com.povush.modusvivendi.data.model.Quest
import com.povush.modusvivendi.data.model.Task
import com.povush.modusvivendi.data.model.TaskWithSubtasks
import kotlinx.coroutines.flow.Flow

interface QuestlinesRepository {
    suspend fun updateQuest(quest: Quest)
    suspend fun deleteQuest(quest: Quest)
    suspend fun updateTask(task: Task)
    suspend fun insertQuestAndTasksWithSubtasks(
        oldQuestId: Long,
        quest: Quest,
        tasks: List<TaskWithSubtasks>
    )
    suspend fun getTaskScope(task: Task): TaskWithSubtasks?
    fun getAllTasksWithSubtasksStreamByQuestId(questId: Long): Flow<List<TaskWithSubtasks>>
    fun getAllTasksWithSubtasksByQuestId(questId: Long): List<TaskWithSubtasks>
    fun getQuestById(questId: Long): Quest
    fun getAllQuests(): Flow<List<Quest>>
}