package com.povush.modusvivendi.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.povush.modusvivendi.data.model.Task
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
    fun getAllTasksByQuestId(questId: Int): Flow<List<Task>>

    @Query("SELECT * FROM tasks WHERE id = :taskId")
    fun getTaskStreamById(taskId: Int): Flow<Task>
}