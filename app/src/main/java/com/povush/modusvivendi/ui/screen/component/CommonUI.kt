package com.povush.modusvivendi.ui.screen.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.povush.modusvivendi.R

@Composable
fun MainParametersBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.primary)
            .padding(top = 6.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        /*TODO: Make parameters interactive*/
        MainParameter(value = "4833", icon = R.drawable.ic_money)
        MainParameter(value = "26,330", icon = R.drawable.ic_vitality_2)
        MainParameter(value = "+3", icon = R.drawable.ic_stability)
        MainParameter(value = "86.50", icon = R.drawable.ic_crown_2)
        MainParameter(value = "31.51", icon = R.drawable.ic_innovativeness)
    }
}

@Composable
fun MainParameter(
    value: String,
    @DrawableRes icon: Int
) {
    // Dotted line
    val stroke = Stroke(
        width = 4.5f,
        pathEffect = PathEffect.dashPathEffect(floatArrayOf(8.5f, 8.5f), 0f)
    )

    Row(
        modifier = Modifier
            .padding(horizontal = 5.dp)
            .clickable { /*TODO*/ },
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            modifier = Modifier
                .size(22.dp)
                .padding(end = 4.dp),
            tint = Color.Unspecified
        )
        Box(
            modifier = Modifier
                .height(20.dp)
                .background(
                    color = Color(0xFF403152),
                    shape = RoundedCornerShape(4.dp)
                )
                .border(
                    width = 2.dp,
                    color = Color(0xFF332640),
                    shape = RoundedCornerShape(4.dp)
                )
                .drawBehind {
                    drawRoundRect(
                        color = Color.Gray,
                        style = stroke,
                        cornerRadius = CornerRadius(12f)
                    )
                },
            contentAlignment = Alignment.TopStart
        ) {
            // Shifting the text slightly downwards
            val text = buildAnnotatedString {
                withStyle(style = SpanStyle(baselineShift = BaselineShift(-0.05f))) {
                    append(value)
                }
            }
            Text(
                text = text,
                modifier = Modifier
                    .padding(horizontal = 4.dp),
                color = Color.White,
                fontWeight = FontWeight.Medium,
                fontFamily = FontFamily(
                    Font(R.font.ptserif_regular)
                ),
                fontSize = 12.sp,
                style = TextStyle(
                    shadow = Shadow(
                        color = Color.Black,
                        offset = Offset(1.5f, 1.5f),
                        blurRadius = 3f
                    )
                )
            )
        }
    }
}
