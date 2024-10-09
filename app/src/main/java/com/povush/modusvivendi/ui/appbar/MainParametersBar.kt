package com.povush.modusvivendi.ui.appbar

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.povush.modusvivendi.R

@Composable
fun MainParametersBar() {
    var screenWidth by remember { mutableFloatStateOf(0f) }
    var mainParametersWidth by remember { mutableFloatStateOf(0f) }

    val scale = if (screenWidth != 0f && mainParametersWidth != 0f) {
        screenWidth/mainParametersWidth
    } else 1f

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .onGloballyPositioned { coordinates ->
                screenWidth = coordinates.size.width.toFloat()
            }
            .background(color = MaterialTheme.colorScheme.primary),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier
                .wrapContentSize()
                .onGloballyPositioned { coordinates ->
                    mainParametersWidth = coordinates.size.width.toFloat()
                }
                .padding(vertical = 8.dp, horizontal = 12.dp)
                .scale(scale),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            /*TODO: Make parameters interactive*/
            MainParameter(value = "4820",iconIdRes = R.drawable.ic_money)
            MainParameter(value = "240",iconIdRes = R.drawable.ic_development_5)
            MainParameter(value = "105",iconIdRes = R.drawable.ic_willpower)
            MainParameter(value = "+2",iconIdRes = R.drawable.ic_stability)
            MainParameter(value = "86",iconIdRes = R.drawable.ic_crown_2)
            MainParameter(value = "37",iconIdRes = R.drawable.ic_innovativeness)
        }
    }
}

@Composable
fun MainParameter(
    value: String,
    @DrawableRes iconIdRes: Int,
) {
    /**
     * This is dotted line for MainParameter stroke.
      */
    val stroke = Stroke(
        width = 4.5f,
        pathEffect = PathEffect.dashPathEffect(floatArrayOf(8.5f, 8.5f), 0f)
    )

    Surface(
        modifier = Modifier
            .height(24.dp)
            .drawBehind {
                drawRoundRect(
                    color = Color.Gray,
                    style = stroke,
                    cornerRadius = CornerRadius(36f)
                )
            },
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceContainerLow,
        border = BorderStroke(width = 2.dp, color = MaterialTheme.colorScheme.surfaceContainerLowest)
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 4.dp, vertical = 2.dp)
                .clickable { /*TODO*/ },
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            /**
             * Variable for a small lift of the text.
             */
            val text = buildAnnotatedString {
                withStyle(style = SpanStyle(baselineShift = BaselineShift(-0.05f))) {
                    append(value)
                }
            }

            Icon(
                painter = painterResource(iconIdRes),
                contentDescription = null,
                modifier = Modifier
                    .size(22.dp)
                    .padding(end = 4.dp),
                tint = Color.Unspecified
            )
            Text(
                text = text,
                modifier = Modifier,
                color = Color.White,
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}