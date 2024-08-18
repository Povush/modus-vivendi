package com.povush.modusvivendi.ui.screen.component

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.povush.modusvivendi.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModusVivendiAppBar(
    title: String,
    onNavigationClicked: () -> Unit,
    actions: @Composable () -> Unit,
    @StringRes sections: List<Int> = listOf(),
    selectedSection: Int = 0
) {
    Column {
        TopAppBar(
            title = {
                Text(
                    text = title,
                    color = MaterialTheme.colorScheme.onPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleLarge
                )
            },
            //        modifier = Modifier.heightIn(max = 20.dp),
            navigationIcon = {
                IconButton(onClick = onNavigationClicked) {
                    Icon(
                        imageVector = Icons.Filled.Menu,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary,
                    )
                }
            },
            actions = { actions() },
            //        windowInsets = WindowInsets(0.dp),
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
            )
        )
        if (sections.isNotEmpty()) {
            ScrollableSectionsRow(
                sections = sections,
                selectedSection = selectedSection
            )
        }
    }
}

@Composable
fun ScrollableSectionsRow(
    @StringRes sections: List<Int>,
    selectedSection: Int
) {
    ScrollableTabRow(
        selectedTabIndex = selectedSection,
        modifier = Modifier
            .fillMaxWidth(),
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        edgePadding = 6.dp
    ) {
        sections.forEachIndexed { index, section ->
            Tab(
                selected = selectedSection == index,
                onClick = {  },
                text = {
                    Text(
                        text = stringResource(id = section),
                        modifier = Modifier,
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal,
                        fontFamily = FontFamily(
                            Font(R.font.carima)
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = TextStyle(
                            shadow = Shadow(
                                color = Color.Black,
                                offset = Offset(1f, 1f) ,
                                blurRadius = 2f
                            )
                        )
                    )
                }
            )
        }
    }
}