package com.povush.modusvivendi.ui.questlines

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.povush.modusvivendi.R
import com.povush.modusvivendi.data.model.Quest
import com.povush.modusvivendi.ui.AppViewModelProvider
import com.povush.modusvivendi.ui.modusVivendiApplication
import com.povush.modusvivendi.ui.theme.NationalTheme

@Composable
fun QuestCard(
    quest: Quest,
    viewModel: QuestViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState by viewModel.uiState.collectAsState()
    viewModel.loadQuest(quest)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp, end = 8.dp, top = 8.dp)
            .graphicsLayer {
                shadowElevation = 1.dp.toPx()
                shape = RoundedCornerShape(8.dp)
                clip = false
                translationX = -12f
                translationY = -12f
            }
            .animateContentSize(),
        shape = RoundedCornerShape(8.dp),
        colors = CardColors(
            containerColor = Color.Transparent,
            contentColor = Color.Black,
            disabledContainerColor = Color.Transparent,
            disabledContentColor = Color.Black
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { viewModel.changeQuestExpandStatus() }
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFFFFFCF2),
                            MaterialTheme.colorScheme.primaryContainer
                        )
                    ),
                    shape = RoundedCornerShape(8.dp)
                )
        ) {
            Text(
                text = uiState.quest.name,
                modifier = Modifier
                    .padding(top = 12.dp,start = 8.dp,end = 8.dp),
                fontSize = 28.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = FontFamily(
                    Font(R.font.moyenage)
                ),
                overflow = TextOverflow.Ellipsis,
                maxLines = 2,
                style = TextStyle(
                    letterSpacing = 0.5.sp,
                    shadow = Shadow(
                        color = Color.Black,
                        offset = Offset(0.10f,0.10f),
                        blurRadius = 0.30f
                    )
                )
            )
            Text(
                text = stringResource(
                    R.string.quest_difficulty,
                    stringResource(id = quest.difficulty.textResId)
                ).uppercase(),
                modifier = Modifier
                    .padding(start = 9.5.dp,end = 8.dp,top = 4.dp,bottom = 6.dp),
                color = quest.difficulty.color,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = FontFamily(
                    Font(R.font.blender_pro_heavy)
                ),
                style = TextStyle(
                    shadow = Shadow(
                        color = quest.difficulty.color,
                        offset = Offset(0.10f,0.10f),
                        blurRadius = 0.30f
                    )
                )
            )
        }
        if (uiState.expanded) {
            QuestExpand(
                quest = quest,
                changeTaskStatus = changeTaskStatus
            )
        }
    }
}

@Composable
fun QuestExpand(
    quest: Quest,
    changeTaskStatus: (Quest,Task) -> Unit
) {
    Column(
        modifier = Modifier.padding(horizontal = 0.dp)
    ) {
        Text(
            text = quest.description,
            modifier = Modifier.padding(start = 8.dp, end = 8.dp, top = 6.dp, bottom = 12.dp),
            fontSize = 14.sp,
            fontWeight = FontWeight.Thin,
            fontFamily = FontFamily(
                Font(R.font.roboto_regular)
            ),
            lineHeight = 17.sp,
            letterSpacing = 0.2.sp,
            style = TextStyle(
                shadow = Shadow(
                    color = Color.Black,
                    offset = Offset(0.10f, 0.10f),
                    blurRadius = 0.30f
                )
            )
        )
        Tasks(quest = quest, changeTaskStatus = changeTaskStatus)
    }
}

@Composable
fun Tasks(
    quest: Quest,
    changeTaskStatus: (Quest,Task) -> Unit
) {
    val tasks = quest.tasks

    Column(
        modifier = Modifier.padding(start = 4.dp, end = 4.dp, bottom = 8.dp)
    ) {
        tasks.forEach { task ->
            Row {
                Checkbox(
                    checked = task.isCompleted,
                    onCheckedChange = { changeTaskStatus(quest, task) },
                    modifier = Modifier
                        .size(32.dp)
                )
                DynamicPaddingText(task.text)
            }
        }
    }
}

@Composable
fun DynamicPaddingText(text: String) {
    var lineCount by remember { mutableIntStateOf(0) }

    val paddingValues = when (lineCount) {
        1 -> PaddingValues(top = 8.dp)
        else -> PaddingValues(top = 4.dp)
    }

    Text(
        text = text,
        modifier = Modifier.padding(paddingValues),
        fontSize = 14.sp,
        fontFamily = FontFamily(
            Font(R.font.blender_pro_heavy)
        ),
        lineHeight = 14.sp,
        style = TextStyle(fontSize = 16.sp),
        onTextLayout = { textLayoutResult: TextLayoutResult ->
            lineCount = textLayoutResult.lineCount
        }
    )
}

@Preview
@Composable
fun QuestPreview() {
    val sampleQuest = Quest(
        title = "Code of reality II",
        difficulty = Difficulty.HIGH,
        description = "The outcome of lengthy parliamentary debates of the Direction to take in the IT field was the decision to focus on mobile application development. The main advantages of this choice include higher demand compared to frontend development, greater impact on the immediately visible result compared to backend development, the ability to port game mechanics easily, and local compatibility with the current demands of programmers. But most importantly, we believe that the future lies in mobile development.",
        tasks = listOf(
            Task(
                text = "Take a short primary course on Android development on Kotlin",
                isCompleted = true
            ),
            Task(text = "Go through Android"), // Basics with Compose
            Task(text = "Create an application for linguistic simulation"),
            Task(text = "Create an application for Ilya's diploma"),
            Task(
                text = "To dissect the entire Play Market",
                isAdditional = true
            )
        ),
        isCompleted = false,
        dateOfCompletion = null,
        expanded = true
    )

    NationalTheme {
        QuestCard(
            quest = sampleQuest,
            changeQuestExpandStatus = { _: Int -> },
            changeTaskStatus = { _: Quest,_: Task -> }
        )
    }
}