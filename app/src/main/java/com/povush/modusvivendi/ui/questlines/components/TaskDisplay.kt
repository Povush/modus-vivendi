package com.povush.modusvivendi.ui.questlines.components

import android.os.Build
import android.view.HapticFeedbackConstants
import android.widget.Toast
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.povush.modusvivendi.R
import com.povush.modusvivendi.data.model.Task
import com.povush.modusvivendi.data.model.TaskWithSubtasks
import com.povush.modusvivendi.ui.theme.VerticalSubtaskLine
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun TaskDisplay(
    taskWithSubtasks: TaskWithSubtasks,
    onCheckedChange: (Task, Boolean, List<Task>?) -> Boolean,
    isEnabled: Boolean
) {
    val task = taskWithSubtasks.task
    val subtasks = taskWithSubtasks.subtasks.sortedBy { it.orderIndex }

    TaskDisplayItem(task, onCheckedChange, isEnabled, subtasks)
    subtasks.forEach { subtask ->
        key(subtask.id) {
            TaskDisplayItem(subtask, onCheckedChange, isEnabled)
        }
    }
}

@Composable
private fun TaskDisplayItem(
    task: Task,
    onCheckedChange: (Task, Boolean, List<Task>?) -> Boolean,
    isEnabled: Boolean,
    subtasks: List<Task>? = null
) {
    val isSubtask = task.parentTaskId != null
    val context = LocalContext.current
    val view = LocalView.current
    val coroutineScope = rememberCoroutineScope()

    var isCheckboxScaled by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isCheckboxScaled) 0.8f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "Task checkbox animation"
    )

    var taskHeightPx by remember { mutableIntStateOf(0) }
    val taskHeightDp = with(LocalDensity.current) { taskHeightPx.toDp() }

    Row(modifier = Modifier.padding(horizontal = 8.dp)) {
        if (isSubtask) {
            Box(
                modifier = Modifier.size(15.dp,taskHeightDp),
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
                    coroutineScope.launch {
                        isCheckboxScaled = true
                        delay(150)
                        isCheckboxScaled = false
                    }
                    val isChecked = onCheckedChange(task, it, subtasks)
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
                enabled = isEnabled
            )
            Box(modifier = Modifier.padding(top = 5.dp)) {
                Text(
                    text = buildAnnotatedString {
                        append(task.name)
                        if (task.isAdditional) {
                            append(" (")
                            withStyle(style = SpanStyle(color =
                                if (task.isFailed) MaterialTheme.colorScheme.error
                                else Color(0xFF806000)
                            )) {
                                append(stringResource(R.string.add_l))
                            }
                            append(")")
                        }
                    },
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color =
                            if (task.isFailed) MaterialTheme.colorScheme.error
                            else Color.Black,
                        textDecoration =
                            if (task.isFailed) TextDecoration.LineThrough
                            else TextDecoration.None
                    )
                )
            }
        }
    }
}