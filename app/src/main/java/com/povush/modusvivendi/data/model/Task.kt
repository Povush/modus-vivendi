package com.povush.modusvivendi.data.model

import androidx.room.DatabaseView
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(
    tableName = "tasks",
    foreignKeys = [
        ForeignKey(
            entity = Quest::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("questId"),
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Task::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("parentTaskId"),
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["questId"]),
        Index(value = ["parentTaskId"])
    ]
)
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val questId: Long,
    val parentTaskId: Long? = null,
    val name: String = "",
    val isCompleted: Boolean = false,
    val counter: Pair<Int, Int>? = null,
    val isAdditional: Boolean = false,
    val orderIndex: Int = 0
)

data class TaskWithSubtasks(
    @Embedded val task: Task,
    @Relation(
        parentColumn = "id",
        entityColumn = "parentTaskId"
    )
    val subtasks: List<Task>
)