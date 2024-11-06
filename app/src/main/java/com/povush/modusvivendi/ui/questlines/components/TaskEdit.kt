package com.povush.modusvivendi.ui.questlines.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.unit.dp
import com.povush.modusvivendi.R
import com.povush.modusvivendi.data.model.Task
import com.povush.modusvivendi.ui.common.components.ModusVivendiDropdownMenuItem
import com.povush.modusvivendi.ui.theme.VerticalSubtaskLine

@Composable
fun TaskEdit(
    task: Task,
    onCheckedChange: (Task, Boolean) -> Unit,
    onTaskTextChange: (Task, String) -> Unit,
    onCreateSubtask: (Task) -> Unit,
    onTaskDelete: (Task) -> Unit
) {
    var menuExpanded by remember { mutableStateOf(false) }
    val isSubtask = task.parentTaskId != null

    var taskHeightPx by remember { mutableIntStateOf(0) }
    val taskHeightDp = with(LocalDensity.current) { taskHeightPx.toDp() }

    Row {
        if (isSubtask) {
            Box(
                modifier = Modifier.size(17.dp,taskHeightDp),
                contentAlignment = Alignment.CenterEnd
            ) {
                VerticalSubtaskLine()
            }
            Spacer(modifier = Modifier.size(4.dp))
        }
        Column(
            modifier = Modifier
                .onGloballyPositioned { coordinates ->
                    taskHeightPx = coordinates.size.height
                }
        ) {
            Spacer(modifier = Modifier.size(2.dp))
            Row(
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = RoundedCornerShape(8.dp)
                    )
            ) {
                Checkbox(
                    checked = task.isCompleted,
                    onCheckedChange = { onCheckedChange(task,it) },
                    modifier = Modifier.size(32.dp)
                )
                DynamicPaddingBasicTextField(
                    value = task.name,
                    onValueChange = { input -> onTaskTextChange(task, input) },
                    modifier = Modifier.weight(1f)
                )
                Box(
                    modifier = Modifier
                        .wrapContentSize(Alignment.TopEnd)
                ) {
                    DropdownMenu(
                        expanded = menuExpanded,
                        onDismissRequest = { menuExpanded = false },
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.primaryContainer)
                            .wrapContentHeight()
                            .padding(4.dp)
                    ) {
                        if (task.parentTaskId == null) {
                            ModusVivendiDropdownMenuItem(R.string.create_subtask) {
                                menuExpanded = false
                                onCreateSubtask(task)
                            }
                        }
                        ModusVivendiDropdownMenuItem(
                            if (task.parentTaskId == null) R.string.delete_task
                            else R.string.delete_subtask
                        ) {
                            menuExpanded = false
                            onTaskDelete(task)
                        }
                    }
                }
                TaskEditButton(
                    onClicked = { menuExpanded = !menuExpanded },
                    modifier = Modifier.padding(6.dp)
                )

            }
            Spacer(modifier = Modifier.size(2.dp))
        }
    }
}

@Composable
private fun TaskEditButton(
    onClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(22.dp)
            .background(
                color = MaterialTheme.colorScheme.surfaceContainerLow,
                shape = CircleShape
            )
            .clickable { onClicked() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Edit,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier
                .size(16.dp)
        )
    }
}

@Composable
fun DynamicPaddingBasicTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var lineCount by remember { mutableIntStateOf(0) }

    val animatedHeight by animateDpAsState(
        targetValue = when (lineCount) {
            1 -> 8.dp
            else -> 4.dp
        },
        label = "DynamicPaddingBasicTextField"
    )

    Box(modifier = modifier.padding(top = animatedHeight)) {
        BasicTextField(
            value = value,
            onValueChange = { onValueChange(it) },
            modifier = Modifier.fillMaxWidth(),
            onTextLayout = { textLayoutResult: TextLayoutResult ->
                lineCount = textLayoutResult.lineCount
            },
            textStyle = MaterialTheme.typography.bodyLarge
        )
        if (value.isEmpty()) {
            Text(
                text = stringResource(R.string.new_task),
                style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
            )
        }
    }
}