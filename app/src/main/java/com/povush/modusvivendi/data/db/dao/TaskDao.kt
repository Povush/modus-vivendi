package com.povush.modusvivendi.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.povush.modusvivendi.data.model.Task
import com.povush.modusvivendi.data.model.TaskWithSubtasks
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTask(task: Task): Long

    @Update
    suspend fun updateTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)

    @Query("DELETE FROM tasks WHERE questId = :questId")
    suspend fun deleteTasksByQuestId(questId: Long)

    @Query("SELECT * FROM tasks WHERE id = :taskId")
    suspend fun getTaskById(taskId: Long): Task?

    @Query("SELECT * FROM tasks WHERE parentTaskId = :parentTaskId")
    suspend fun getSubtasksByParentId(parentTaskId: Long): List<Task>

    @Query("SELECT * FROM tasks WHERE questId = :questId ORDER BY orderIndex")
    fun getAllTasksByQuestId(questId: Long): Flow<List<Task>>

    @Transaction
    @Query("SELECT * FROM tasks WHERE parentTaskId IS NULL and questId = :questId")
    fun getAllTasksWithSubtasksStreamByQuestId(questId: Long): Flow<List<TaskWithSubtasks>>

    @Transaction
    @Query("SELECT * FROM tasks WHERE parentTaskId IS NULL and questId = :questId ORDER BY orderIndex")
    fun getAllTasksWithSubtasksByQuestId(questId: Long): List<TaskWithSubtasks>

    @Query("SELECT * FROM tasks WHERE id = :taskId")
    fun getTaskStreamById(taskId: Long): Flow<Task>
}