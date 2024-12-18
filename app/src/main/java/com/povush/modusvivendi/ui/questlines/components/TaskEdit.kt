package com.povush.modusvivendi.ui.questlines.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.outlined.AccountTree
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
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
    onTaskDelete: (Task) -> Unit,
    onTaskMandatoryStatusChange: (Task, Boolean) -> Unit
) {
    var menuExpanded by remember { mutableStateOf(false) }
    val isSubtask = task.parentTaskId != null

    var taskHeightPx by remember { mutableIntStateOf(0) }
    val taskHeightDp = with(LocalDensity.current) { taskHeightPx.toDp() }

    var lineCount by remember { mutableIntStateOf(0) }

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
                    .padding(bottom = if (lineCount > 1) 4.5.dp else 0.dp)
            ) {
                Checkbox(
                    checked = task.isCompleted,
                    onCheckedChange = { onCheckedChange(task,it) },
                    modifier = Modifier.size(32.dp)
                )
                Box(modifier = Modifier.weight(1f).padding(top = 6.5.dp)) {
                    BasicTextField(
                        value = task.name,
                        onValueChange = { onTaskTextChange(task, it) },
                        modifier = Modifier
                            .fillMaxWidth(),
                        decorationBox = { innerTextField ->
                            Box {
                                if (task.isAdditional) {
                                    Text(
                                        text = buildAnnotatedString {
                                            withStyle(style = SpanStyle(color = Color.Transparent)) {
                                                append(task.name)
                                            }
                                            append(" (")
                                            withStyle(style = SpanStyle(color = Color(0xFF806000))) {
                                                append(stringResource(R.string.add_l))
                                            }
                                            append(")")
                                        },
                                        style = MaterialTheme.typography.bodyLarge,
                                        modifier = Modifier.padding(start = 1.dp)
                                    )
                                }
                                innerTextField()
                            }
                        },
                        onTextLayout = { textLayoutResult: TextLayoutResult ->
                            lineCount = textLayoutResult.lineCount
                        },
                        textStyle = MaterialTheme.typography.bodyLarge
                    )
                    if (task.name.isEmpty()) {
                        Text(
                            text = stringResource(R.string.new_task),
                            style = MaterialTheme.typography.bodyLarge.copy(
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                            )
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .wrapContentSize(Alignment.TopEnd)
                ) {
                    DropdownMenu(
                        expanded = menuExpanded,
                        onDismissRequest = { menuExpanded = false },
                        modifier = Modifier
                            .background(Color.White)
                            .wrapContentHeight(),
                        shape = RoundedCornerShape(8.dp),
                        shadowElevation = 8.dp
                    ) {
                        if (task.parentTaskId == null) {
                            ModusVivendiDropdownMenuItem(
                                textRes = R.string.create_subtask,
                                onClick = {
                                    menuExpanded = false
                                    onCreateSubtask(task)
                                },
                                leadingIcon = Icons.Outlined.Add
                            )
                        }
                        ModusVivendiDropdownMenuItem(
                            textRes =
                                if (task.isAdditional) R.string.make_primary
                                else R.string.make_additional,
                            onClick = {
                                menuExpanded = false
                                onTaskMandatoryStatusChange(task, !task.isAdditional)
                            },
                            leadingIcon = Icons.Outlined.AccountTree
                        )
                        ModusVivendiDropdownMenuItem(
                            textRes =
                            if (task.parentTaskId == null) R.string.delete_task
                            else R.string.delete_subtask,
                            onClick = {
                                menuExpanded = false
                                onTaskDelete(task)
                            },
                            leadingIcon = Icons.Outlined.DeleteOutline,
                            isDangerous = true
                        )
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
    val focusManager = LocalFocusManager.current

    Box(
        modifier = modifier
            .size(22.dp)
            .clip(CircleShape)
            .background(
                color = MaterialTheme.colorScheme.primary,
                shape = CircleShape
            )
            .clickable {
                focusManager.clearFocus(false)
                onClicked()
            },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Edit,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier
                .size(16.dp)
        )
    }
}