package com.povush.modusvivendi.ui.map

import androidx.annotation.RawRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.povush.modusvivendi.R
import com.povush.modusvivendi.ui.common.appbars.ModusVivendiAppBar
import com.povush.modusvivendi.ui.navigation.NavigationDestination
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.mapview.MapView

object MapDestination : NavigationDestination {
    override val route = "map"
    override val titleRes = R.string.map
}

@Composable
fun MapScreen(
    onNavigationClick: () -> Unit,
    viewModel: MapViewModel = hiltViewModel()
) {
    Scaffold(
        modifier = Modifier,
        topBar = {
            ModusVivendiAppBar(
                titleRes = MapDestination.titleRes,
                navigation = {
                    IconButton(onClick = { onNavigationClick() }) {
                        Icon(
                            imageVector = Icons.Filled.Menu,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimary,
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {  }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.MoreVert,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {  },
                modifier = Modifier.padding(vertical = 16.dp, horizontal = 8.dp),
                shape = CircleShape,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(
                    imageVector = Icons.Filled.Navigation,
                    contentDescription = null
                )
            }
        }
    ) { innerPadding ->
        YandexMapView(modifier = Modifier.padding(innerPadding))
    }
}

@Composable
fun YandexMapView(modifier: Modifier = Modifier) {
    AndroidView(
        factory = { context ->
            MapView(context).apply {
                moveToDefaultCologneLocation()
            }
        },
        modifier = Modifier.fillMaxSize()
    )
}

fun MapView.setupStyle(@RawRes style: Int) {
    val styleJson = context.resources.openRawResource(style)
        .bufferedReader()
        .use { it.readText() }
    mapWindow.map.setMapStyle(styleJson)
}

fun MapView.moveToDefaultCologneLocation() {
    mapWindow.map.move(
        CameraPosition(
            Point(50.930779, 6.938399),
            3.75f,
            0.0f,
            0.0f
        )
    )
}