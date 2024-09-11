package com.povush.modusvivendi.data.model.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.povush.modusvivendi.data.model.Quest
import com.povush.modusvivendi.data.model.Subtask
import com.povush.modusvivendi.data.model.Task

data class QuestWithTasks(
    @Embedded val quest: Quest,
    @Relation(
        parentColumn = "id",
        entityColumn = "questId"
    )
    val tasks: List<Task>
)

data class TaskWithSubtasks(
    @Embedded val task: Task,
    @Relation(
        parentColumn = "id",
        entityColumn = "taskId"
    )
    val subtasks: List<Subtask>
)
