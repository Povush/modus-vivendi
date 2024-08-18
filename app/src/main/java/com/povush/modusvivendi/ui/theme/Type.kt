package com.povush.modusvivendi.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import com.povush.modusvivendi.R
import androidx.compose.ui.text.font.Font

val Typography = Typography(
//    displayLarge
//    displayMedium
//    displaySmall
//    headlineLarge
//    headlineMedium
//    headlineSmall


/**
 * Use for titles of TopAppBars.
 */
    titleLarge = TextStyle(
        fontFamily = FontFamily(
            Font(R.font.carima)
        ),
        fontWeight = FontWeight.Normal,
        fontSize = 28.sp,
        lineHeight = 36.sp,
        letterSpacing = 0.sp,
        shadow = Shadow(
            color = Color.Black,
            offset = Offset(1f, 1f),
            blurRadius = 2f
        )
    )

//    titleMedium
//    titleSmall
//    bodyLarge
//    bodyMedium
//    bodySmall
//    labelLarge
//    labelMedium
//    labelSmall
)

