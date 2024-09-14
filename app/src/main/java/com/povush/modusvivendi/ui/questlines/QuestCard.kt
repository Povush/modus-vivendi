package com.povush.modusvivendi.ui.questlines

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
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
import com.povush.modusvivendi.data.model.QuestType
import com.povush.modusvivendi.data.model.Task
import com.povush.modusvivendi.ui.AppViewModelProvider
import com.povush.modusvivendi.ui.createQuestViewModelExtras
import com.povush.modusvivendi.ui.theme.NationalTheme

@Composable
fun QuestCard(
    quest: Quest,
    viewModel: QuestViewModel = viewModel(
        factory = AppViewModelProvider.Factory,
        key = "questViewModel_${quest.id}",
        extras = createQuestViewModelExtras(quest, LocalContext.current.applicationContext)
    )
) {
    val uiState by viewModel.uiState.collectAsState()

    Card(
        modifier = Modifier
            .fillMaxWidth()
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
//                .graphicsLayer {
//                    shadowElevation = 2.dp.toPx()
//                    shape = CutCornerShape(8.dp)
//                    clip = true
//                }
                .clickable { viewModel.changeQuestExpandStatus() }
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFFFFFCF2),
                            MaterialTheme.colorScheme.primaryContainer
                        )
                    )
                )
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = uiState.quest.name,
                modifier = Modifier,
                overflow = TextOverflow.Ellipsis,
                maxLines = 2,
                style = MaterialTheme.typography.headlineSmall
            )
            Text(
                text = stringResource(
                    R.string.quest_difficulty,
                    stringResource(id = quest.difficulty.textResId)
                ).uppercase(),
                modifier = Modifier,
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = quest.difficulty.color,
                    fontWeight = FontWeight.SemiBold,
                    shadow = Shadow(
                        color = Color.Black,
                        offset = Offset(0.10f, 0.10f),
                        blurRadius = 0.30f
                    )
                )
            )
        }
        if (uiState.expanded) {
            Text(
                text = quest.description,
                modifier = Modifier.padding(8.dp),
                style = MaterialTheme.typography.bodyMedium
            )
            Tasks(
                quest = quest,
                tasks = uiState.tasks,
                changeTaskStatus = { task: Task -> viewModel.changeTaskStatus(task) }
            )
        }
    }
}

@Composable
fun Tasks(
    quest: Quest,
    tasks: List<Task>,
    changeTaskStatus: (Task) -> Unit
) {
    Column(
        modifier = Modifier.padding(4.dp)
    ) {
        tasks.forEach { task ->
            Row {
                Checkbox(
                    checked = task.isCompleted,
                    onCheckedChange = { changeTaskStatus(task) },
                    modifier = Modifier
                        .size(32.dp),
                    enabled = quest.type !in listOf(QuestType.COMPLETED, QuestType.FAILED)
                )
                DynamicPaddingText(task.name)
            }
        }
    }
}

/*TODO: Replace it with something normal*/
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
        onTextLayout = { textLayoutResult: TextLayoutResult ->
            lineCount = textLayoutResult.lineCount
        },
        style = MaterialTheme.typography.bodyLarge
    )
}