package com.povush.modusvivendi.data.model

import android.graphics.drawable.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.FactCheck
import androidx.compose.material.icons.filled.CreateNewFolder
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.FactCheck
import androidx.compose.material.icons.filled.FolderOff
import androidx.compose.material.icons.filled.FolderSpecial
import androidx.compose.material.icons.filled.Task
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.povush.modusvivendi.R
import java.util.Date

@Entity(tableName = "quests")
data class Quest(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String = "New Quest",
    val type: QuestType = QuestType.ADDITIONAL,
    val difficulty: Difficulty = Difficulty.MEDIUM,
    val description: String = "Sample description.",
    val isCompleted: Boolean = false,
    val dateOfCompletion: Date? = null,
    val pinned: Boolean = false
)

//data class QuestWithTasks(
//    @Embedded val quest: Quest,
//    @Relation(
//        parentColumn = "id",
//        entityColumn = "questId"
//    )
//    val tasks: List<TaskWithSubtasks>
//)

enum class QuestType(val textResId: Int, val iconImageVector: ImageVector) {
    MAIN(R.string.main_quest_section, Icons.Default.FolderSpecial),
    ADDITIONAL(R.string.additional_quest_section, Icons.Default.CreateNewFolder),
    COMPLETED(R.string.completed_quest_section,Icons.Default.Task),
    FAILED(R.string.failed_quest_section, Icons.Default.FolderOff)
}

enum class Difficulty(val textResId: Int, val color: Color) {
    VERY_LOW(R.string.very_low_difficulty, Color(0xFF767171)),
    LOW(R.string.low_difficulty, Color(0xFF00B050)),
    MEDIUM(R.string.medium_difficulty, Color(0xFFBF8F00)),
    HIGH(R.string.high_difficulty, Color(0xFFC45911)),
    VERY_HIGH(R.string.very_high_difficulty, Color(0xFFFF0000))
}