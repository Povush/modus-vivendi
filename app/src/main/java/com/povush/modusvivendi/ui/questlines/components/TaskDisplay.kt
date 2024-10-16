package com.povush.modusvivendi.ui.questlines.components

import android.os.Build
import android.view.HapticFeedbackConstants
import android.widget.Toast
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import com.povush.modusvivendi.R
import com.povush.modusvivendi.data.model.Task
import com.povush.modusvivendi.data.model.TaskWithSubtasks
import com.povush.modusvivendi.ui.theme.VerticalSubtaskLine
import kotlinx.coroutines.delay

@Composable
fun TaskDisplay(
    taskWithSubtasks: TaskWithSubtasks,
    onCheckedChange: (Task, Boolean) -> Boolean
) {
    key(taskWithSubtasks.task.id) {
        TaskDisplayItem(taskWithSubtasks.task, onCheckedChange)
        taskWithSubtasks.subtasks.sortedBy { it.orderIndex }.forEach { subtask ->
            key(subtask.id) {
                TaskDisplayItem(subtask, onCheckedChange)
            }
        }
    }
}

@Composable
private fun TaskDisplayItem(task: Task, onCheckedChange: (Task, Boolean) -> Boolean) {
    val isSubtask = task.parentTaskId != null
    val context = LocalContext.current
    val view = LocalView.current

    var isScaledUp by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isScaledUp) 1.2f else 1f,
        animationSpec = tween(durationMillis = 300),
        label = "Task animation"
    )

    var taskHeightPx by remember { mutableIntStateOf(0) }
    val taskHeightDp = with(LocalDensity.current) { taskHeightPx.toDp() }

    LaunchedEffect(task.isCompleted) {
        isScaledUp = true
        delay(150)
        isScaledUp = false
    }

    Row(modifier = Modifier.padding(horizontal = 8.dp)) {
        if (isSubtask) {
            Box(
                modifier = Modifier.size(17.dp,taskHeightDp),
                contentAlignment = Alignment.CenterEnd
            ) {
                VerticalSubtaskLine()
            }
            Spacer(modifier = Modifier.size(4.dp))
        }
        Row(
            modifier = Modifier
                .onGloballyPositioned { coordinates ->
                    taskHeightPx = coordinates.size.height
                }
        ) {
            Checkbox(
                checked = task.isCompleted,
                onCheckedChange = {
                    val isChecked = onCheckedChange(task,it)
                    if (!isChecked) {
                        Toast.makeText(
                            context,
                            context.getString(R.string.has_active_subtasks),
                            Toast.LENGTH_SHORT
                        ).show()
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                            view.performHapticFeedback(HapticFeedbackConstants.REJECT)
                        }
                    } else {
                        if (it && Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                            view.performHapticFeedback(HapticFeedbackConstants.CONFIRM)
                        }
                    }
                },
                modifier = Modifier
                    .size(28.dp)
                    .scale(scale),
            )
            Box(modifier = Modifier.padding(top = 5.dp)) {
                Text(
                    text = task.name,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}