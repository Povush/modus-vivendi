package com.povush.modusvivendi.ui.screen.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.povush.modusvivendi.R
import com.povush.modusvivendi.ui.theme.NationalTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExampleAppBar() {
    TopAppBar(
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color(0xFFE7E4D5),
            titleContentColor = Color.White,
        ),
        title = {
            Text(
                "Квестлайны",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontFamily = FontFamily(
                    Font(R.font.moyenage)
                ),
                fontWeight = FontWeight.Normal,
                color = Color.Black
            )
        },
        navigationIcon = {
            IconButton(onClick = { /* do something */ }) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = "Localized description",
                    tint = Color.Black
                )
            }
        },
        actions = {
            IconButton(onClick = { /* do something */ }) {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "Localized description",
                    tint = Color.Black
                )
            }
        },
        windowInsets = WindowInsets(top = 0.dp)
    )
}

@Composable
fun MainParametersBar(modifier: Modifier = Modifier) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color(0xFF642424))
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
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
    val stroke = Stroke(width = 3f,
        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
    )

    Box(
        modifier = Modifier
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 2.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
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
                        color = Color(0xFF403152) ,
                        shape = RoundedCornerShape(4.dp)
                    )
                    .border(
                        width = 2.dp ,
                        color = Color(0xFF332640) ,
                        shape = RoundedCornerShape(4.dp)
                    )
                    .drawBehind {
                        drawRoundRect(
                            color = Color.Gray ,
                            style = stroke ,
                            cornerRadius = CornerRadius(12f)
                        )
                    },
                contentAlignment = Alignment.TopStart
            ) {
                val text = buildAnnotatedString {
                    withStyle(style = SpanStyle(baselineShift = BaselineShift(0.5f))) {
                        append(value)
                    }
                }

                Text(
                    text = text,
                    modifier = Modifier
                        .padding(horizontal = 4.dp) ,
                    color = Color.White ,
                    fontWeight = FontWeight.Medium ,
                    fontSize = 12.sp ,
                )
            }
        }
    }
}

@Preview
@Composable
fun Preview() {
    NationalTheme {
        MainParametersBar()
    }
}