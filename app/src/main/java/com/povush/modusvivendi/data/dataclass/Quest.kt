package com.povush.modusvivendi.data.dataclass

import androidx.compose.ui.graphics.Color
import com.povush.modusvivendi.R
import java.util.Date
import java.util.concurrent.atomic.AtomicInteger

data class Quest(
    val id: Int = QuestIdGenerator.generateId(),
    val title: String = "New Quest",
    val difficulty: Difficulty = Difficulty.MEDIUM,
    val description: String = "Description",
    val tasks: List<Task> = listOf(),
    val isCompleted: Boolean = false,
    val dateOfCompletion: Date? = null,
    val expanded: Boolean = false,
    val type: QuestType = QuestType.Main
)

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

enum class Difficulty(val textResId: Int, val color: Color) {
    VERY_LOW(R.string.very_low_difficulty, Color(0xFF767171)),
    LOW(R.string.low_difficulty, Color(0xFF00B050)),
    MEDIUM(R.string.medium_difficulty, Color(0xFFBF8F00)),
    HIGH(R.string.high_difficulty, Color(0xFFC45911)),
    VERY_HIGH(R.string.very_high_difficulty, Color(0xFFFF0000))
}

enum class QuestType(val textResId: Int) {
    Main(R.string.main_quest_section),
    Additional(R.string.additional_quest_section),
    Completed(R.string.completed_quest_section),
    Failed(R.string.failed_quest_section)
}

object QuestIdGenerator {
    private val idCounter = AtomicInteger(0)
    fun generateId(): Int = idCounter.incrementAndGet()
}