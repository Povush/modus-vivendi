package com.povush.modusvivendi.ui.screen.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.povush.modusvivendi.data.dataclass.Difficulty
import com.povush.modusvivendi.data.dataclass.Quest
import com.povush.modusvivendi.data.dataclass.Task
import com.povush.modusvivendi.data.dataclass.highDifficulty
import com.povush.modusvivendi.data.dataclass.mediumDifficulty
import com.povush.modusvivendi.ui.theme.NationalTheme
import java.util.Date

@Composable
fun QuestCard(
    questTitle: String
) {
    Card(
        onClick = { /*TODO: Opening and closing the quest*/ },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 6.dp),
        shape = RoundedCornerShape(3.dp),
        colors = CardColors(
            containerColor = Color(0xFFFCF0C8),
            contentColor = Color.Black,
            disabledContainerColor = Color(0xFFFCF0C8),
            disabledContentColor = Color.Black
        )
    ) {
        Text(
            text = questTitle
        )
    }
}

@Preview
@Composable
fun QuestPreview() {
    val sampleQuest = Quest(
        title = "Code of reality II",
        difficulty = highDifficulty,
        description = "The outcome of lengthy parliamentary debates of the Direction to take in the IT field was the decision to focus on mobile application development. The main advantages of this choice include higher demand compared to frontend development, greater impact on the immediately visible result compared to backend development, the ability to port game mechanics easily, and local compatibility with the current demands of programmers. But most importantly, we believe that the future lies in mobile development.",
        tasks = listOf(
            Task(
                text = "Take a short primary course on Android development on Kotlin",
                isCompleted = true
            )
        ),
        isCompleted = false,
        dateOfCompletion = null
    )

    NationalTheme {
        QuestCard(
            questTitle = "Code of reality II"
        )
    }
}