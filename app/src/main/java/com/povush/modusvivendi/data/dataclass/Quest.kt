package com.povush.modusvivendi.data.dataclass

import androidx.compose.ui.graphics.Color
import java.util.Date

data class Quest(
    val title: String = "New Quest",
    val difficulty: Difficulty = mediumDifficulty,
    val description: String = "Description",
    val tasks: List<Task> = listOf(),
    val isCompleted: Boolean,
    val dateOfCompletion: Date? = null
)

data class Difficulty(
    val text: String,
    val color: Color
)

val veryLowDifficulty = Difficulty("Very low", color = Color(0xFF767171))
val lowDifficulty = Difficulty("Low", color = Color(0xFF00B050))
val mediumDifficulty = Difficulty("Medium", color = Color(0xFFBF8F00))
val highDifficulty = Difficulty("High", color = Color(0xFFC45911))
val veryHighDifficulty = Difficulty("Very high", color = Color(0xFFFF0000))

data class Task(
    val text: String = "New Task",
    val isCompleted: Boolean = false,
    val counter: Pair<Int, Int>? = null,
    val isAdditional: Boolean = false,
    val subTasks: List<SubTask> = listOf()
)

data class SubTask(
    val text: String,
    val isCompleted: Boolean,
    val counter: Pair<Int, Int>,
    val isAdditional: Boolean
)