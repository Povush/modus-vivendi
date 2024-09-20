package com.povush.modusvivendi.data.model

import androidx.compose.ui.graphics.Color
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.povush.modusvivendi.R

@Entity(tableName = "quests")
data class Quest(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String = "New Quest",
    val type: QuestType = QuestType.ADDITIONAL,
    /*TODO: Need conversation*/
    val difficulty: Difficulty = Difficulty.MEDIUM,
    val description: String = "Sample description.",
    val isCompleted: Boolean = false,
    /*TODO: Need conversation*/
//    val dateOfCompletion: Date? = null,
    val pinned: Boolean = false
)

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val questId: Int,
    val name: String = "New Task",
    val isCompleted: Boolean = false,
    /*TODO: Need conversation*/
//    val counter: Pair<Int, Int>? = null,
    val isAdditional: Boolean = false,
    val orderIndex: Int = 0
)

@Entity(tableName = "subtasks")
data class Subtask(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val taskId: Int,
    val name: String = "New Task",
    val isCompleted: Boolean = false,
    /*TODO: Need conversation*/
//    val counter: Pair<Int, Int>? = null,
    val isAdditional: Boolean = false,
    val orderIndex: Int = 0
)

enum class QuestType(val textResId: Int) {
    MAIN(R.string.main_quest_section),
    ADDITIONAL(R.string.additional_quest_section),
    COMPLETED(R.string.completed_quest_section),
    FAILED(R.string.failed_quest_section)
}

enum class Difficulty(val textResId: Int, val color: Color, val imgResId: Int) {
    VERY_LOW(R.string.very_low_difficulty, Color(0xFF767171), R.drawable.img_difficulty_very_low),
    LOW(R.string.low_difficulty, Color(0xFF00B050), R.drawable.img_difficulty_low),
    MEDIUM(R.string.medium_difficulty, Color(0xFFBF8F00), R.drawable.img_difficulty_medium),
    HIGH(R.string.high_difficulty, Color(0xFFC45911), R.drawable.img_difficulty_high),
    VERY_HIGH(R.string.very_high_difficulty, Color(0xFFFF0000), R.drawable.img_difficulty_very_high)
}