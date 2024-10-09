package com.povush.modusvivendi.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val questId: Int,
    val parentTaskId: Int? = null,
    val name: String = "New Task",
    val isCompleted: Boolean = false,
    val counter: Pair<Int, Int>? = null,
    val isAdditional: Boolean = false,
    val orderIndex: Int = 0
)