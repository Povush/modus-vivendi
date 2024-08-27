package com.povush.modusvivendi.ui.screen.component

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
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
import com.povush.modusvivendi.data.dataclass.QuestType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModusVivendiAppBar(
    title: String,
    onNavigationClicked: () -> Unit,
    actions: @Composable () -> Unit = {},
    @StringRes sections: List<Int> = listOf(),
    selectedSection: Int = 0,
    onTabClicked: (Int) -> Unit = {},
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
            modifier = Modifier.heightIn(max = 45.dp),
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
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
            )
        )
        if (sections.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .background(color = MaterialTheme.colorScheme.primary)
            )
        } else {
            ScrollableSectionsRow(
                sections = sections,
                selectedSection = selectedSection,
                onTabClicked = onTabClicked
            )
        }
    }
}

@Composable
fun ScrollableSectionsRow(
    @StringRes sections: List<Int>,
    selectedSection: Int,
    onTabClicked: (Int) -> Unit
) {
    ScrollableTabRow(
        selectedTabIndex = selectedSection,
        modifier = Modifier
            .fillMaxWidth(),
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        edgePadding = 4.dp,
        indicator = { tabPositions ->
            SecondaryIndicator(
                Modifier
                    .tabIndicatorOffset(tabPositions[selectedSection]),
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    ) {
        sections.forEachIndexed { index, section ->
            Tab(
                selected = selectedSection == index,
                onClick = { onTabClicked(index) },
                text = {
                    Text(
                        text = stringResource(id = section),
                        color = MaterialTheme.colorScheme.onPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.titleSmall
                    )
                }
            )
        }
    }
}