package com.povush.modusvivendi.ui.splash

import android.os.Build
import android.os.Build.VERSION.SDK_INT
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.ImageLoader
import coil3.compose.AsyncImage
import coil3.gif.AnimatedImageDecoder
import coil3.gif.GifDecoder
import coil3.request.ImageRequest
import com.povush.modusvivendi.R
import com.povush.modusvivendi.ui.navigation.NavigationDestination
import kotlinx.coroutines.delay

object SplashDestination : NavigationDestination {
    override val route = "splash"
    override val titleRes = R.string.splash
}

@Composable
fun SplashScreen(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
//        CircularProgressIndicator(color = MaterialTheme.colorScheme.onBackground)
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(R.raw.mv_splash)
                .build(),
            contentDescription = "Local GIF Example",
            modifier = Modifier.size(250.dp)
        )
        Spacer(modifier = Modifier.size(16.dp))
//        Text(
//            text = "Loading...",
//            modifier = Modifier.fillMaxWidth(),
//            textAlign = TextAlign.Center,
//            style = MaterialTheme.typography.headlineSmall.copy(fontSize = 28.sp)
//        )
        RainbowText("Loading...")
    }
}

@Composable
fun RainbowText(
    text: String,
    fontSize: TextUnit = 28.sp,
    modifier: Modifier = Modifier
) {
    // Создаем анимацию
    val infiniteTransition = rememberInfiniteTransition()
    val animatedOffset = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    // Определяем цвета радуги
    val colors = listOf(
        Color.Red,
        Color.Magenta,
        Color.Blue,
        Color.Cyan,
        Color.Green,
        Color.Yellow,
        Color.Red // повторяем, чтобы замкнуть радугу
    )

    // Создаем градиент с анимацией
    val brush = Brush.linearGradient(
        colors = colors,
        start = Offset(x = animatedOffset.value, y = 0f),
        end = Offset(x = animatedOffset.value + 500f, y = 0f)
    )

    // Рисуем текст с градиентом
    BasicText(
        text = text,
        modifier = modifier.fillMaxWidth(),
        style = MaterialTheme.typography.headlineSmall.copy(
            brush = brush,
            fontSize = 28.sp,
            textAlign = TextAlign.Center
        )

    )
}