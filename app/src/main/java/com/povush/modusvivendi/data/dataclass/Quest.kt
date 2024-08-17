package com.povush.modusvivendi.data.dataclass

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.povush.modusvivendi.R
import java.util.Date

data class Quest(
    val title: String = "New Quest",
    val difficulty: Difficulty = Difficulty.Medium,
    val description: String = "Description",
    val tasks: List<Task> = listOf(),
    val isCompleted: Boolean,
    val dateOfCompletion: Date? = null
)

enum class Difficulty(val textResId: Int, val color: Color) {
    VeryLow(R.string.very_low_difficulty, Color(0xFF767171)),
    Low(R.string.low_difficulty, Color(0xFF00B050)),
    Medium(R.string.medium_difficulty, Color(0xFFBF8F00)),
    High(R.string.high_difficulty, Color(0xFFC45911)),
    VeryHigh(R.string.very_high_difficulty, Color(0xFFFF0000))
}

@Composable
fun getDifficultyText(difficulty: Difficulty): String {
    return stringResource(id = difficulty.textResId)
}

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