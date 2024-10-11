package com.povush.modusvivendi.ui.theme

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun VerticalSubtaskLine() {
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