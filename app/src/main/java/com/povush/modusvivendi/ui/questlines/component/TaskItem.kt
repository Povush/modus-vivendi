package com.povush.modusvivendi.ui.questlines.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.unit.dp
import com.povush.modusvivendi.data.model.Task

@Composable
fun TaskItem(
    task: Task,
    isEdit: Boolean,
    onCheckedChange: (Task, Boolean) -> Unit,
    onTaskTextChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val isSubtask = task.parentTaskId != null

    var taskHeightPx by remember { mutableIntStateOf(0) }
    val taskHeightDp = with(LocalDensity.current) { taskHeightPx.toDp() }

    Row(
        modifier = modifier
            .onGloballyPositioned { coordinates ->
                taskHeightPx = coordinates.size.height
            }
    ) {
        if (isSubtask) {
            Box(
                modifier = Modifier.size(32.dp,taskHeightDp),
                contentAlignment = Alignment.Center
            ) {
                Canvas(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(2.dp)
                ) {
                    drawLine(
                        color = Color.Black,
                        start = Offset(size.width / 2,0f),
                        end = Offset(size.width / 2,size.height),
                        strokeWidth = size.width
                    )
                }
            }
        }
        Row(
            modifier = Modifier
                .padding(vertical = 2.dp)
                .background(
                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 1f),
                    shape = RoundedCornerShape(8.dp)
                )
        ) {
            Checkbox(
                checked = task.isCompleted,
                onCheckedChange = { onCheckedChange(task,it) },
                modifier = Modifier.size(32.dp)
            )
            if (isEdit) {
                DynamicPaddingBasicTextField(
                    value = task.name,
                    onValueChange = { input -> onTaskTextChange(input) },
                    modifier = Modifier.weight(1f)
                )
            } else {
                DynamicPaddingText(task.name)
            }

            Spacer(modifier = Modifier.size(4.dp))
        }
    }
}

@Composable
fun DynamicPaddingText(text: String, modifier: Modifier = Modifier) {
    var lineCount by remember { mutableIntStateOf(0) }

    val paddingValues = when (lineCount) {
        1 -> PaddingValues(top = 8.dp)
        else -> PaddingValues(top = 4.dp)
    }

    Text(
        text = text,
        modifier = modifier.padding(paddingValues),
        onTextLayout = { textLayoutResult: TextLayoutResult ->
            lineCount = textLayoutResult.lineCount
        },
        style = MaterialTheme.typography.bodyLarge
    )
}

@Composable
fun DynamicPaddingBasicTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var lineCount by remember { mutableIntStateOf(0) }

    val paddingValues = when (lineCount) {
        1 -> PaddingValues(top = 8.dp)
        else -> PaddingValues(top = 4.dp)
    }

    BasicTextField(
        value = value,
        onValueChange = { onValueChange(it) },
        modifier = modifier.padding(paddingValues),
        onTextLayout = { textLayoutResult: TextLayoutResult ->
            lineCount = textLayoutResult.lineCount
        },
        textStyle = MaterialTheme.typography.bodyLarge
    )
}

