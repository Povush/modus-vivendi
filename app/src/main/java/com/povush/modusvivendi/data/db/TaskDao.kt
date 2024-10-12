package com.povush.modusvivendi.data.db

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
    suspend fun insertTask(task: Task)

    @Update
    suspend fun updateTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)

    @Query("SELECT * FROM tasks WHERE questId = :questId")
    fun getAllTasksByQuestId(questId: Long): Flow<List<Task>>

    @Transaction
    @Query("SELECT * FROM tasks WHERE parentTaskId IS NULL and questId = :questId")
    fun getAllTasksWithSubtasksByQuestId(questId: Long): Flow<List<TaskWithSubtasks>>

    @Query("SELECT * FROM tasks WHERE id = :taskId")
    fun getTaskStreamById(taskId: Long): Flow<Task>
}