package com.povush.modusvivendi.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.povush.modusvivendi.R

val Typography = Typography(
//    displayLarge
//    displayMedium
//    displaySmall
//    headlineLarge
//    headlineMedium

    // Use for titles of Quests.
    headlineSmall = TextStyle(
        fontFamily = FontFamily(
            Font(R.font.moyenage)
        ),
        fontWeight = FontWeight.Medium,
        fontSize = 22.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp,
        shadow = Shadow(
            color = Color.Black,
            offset = Offset(0.10f,0.10f),
            blurRadius = 0.30f
        )
    ),

    // Use for titles of TopAppBars.
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
            offset = Offset(1.5f, 1.5f),
            blurRadius = 3f
        )
    ),

    // Use for routes in DrawerSheet.
    titleMedium = TextStyle(
        fontFamily = FontFamily(
            Font(R.font.carima)
        ),
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp,
        color = lerp(Color.Black, Color.White, 0.3f),
    ),

    // Use for Tabs in ScrollableSectionsRow.
    titleSmall = TextStyle(
        fontFamily = FontFamily(
            Font(R.font.carima)
        ),
        fontWeight = FontWeight.Medium,
        fontSize = 18.sp,
        lineHeight = 26.sp,
        letterSpacing = 0.15.sp,
        shadow = Shadow(
            color = Color.Black,
            offset = Offset(1.5f, 1.5f),
            blurRadius = 3f
        )
    ),

    // Use for tasks in Questlines.
    bodyLarge = TextStyle(
        fontFamily = FontFamily(
            Font(R.font.blender_pro_heavy)
        ),
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 18.sp,
    ),

    // Use for general text on the screen.
    bodyMedium = TextStyle(
        fontFamily = FontFamily(
            Font(R.font.roboto_regular)
        ),
        fontWeight = FontWeight.Thin,
        fontSize = 14.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.25.sp,
        shadow = Shadow(
            color = Color.Black,
            offset = Offset(0.10f, 0.10f),
            blurRadius = 0.30f
        )
    ),

//    bodySmall
//    labelLarge

    // Use for parameters in MainParametersBar.
    labelMedium = TextStyle(
        fontFamily = FontFamily(
            Font(R.font.ptserif_regular)
        ),
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp,
        shadow = Shadow(
            color = Color.Black,
            offset = Offset(1.5f,1.5f),
            blurRadius = 3f
        )
    ),

//    labelSmall
)

