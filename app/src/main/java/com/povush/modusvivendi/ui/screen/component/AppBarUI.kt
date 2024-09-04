package com.povush.modusvivendi.ui.screen.component

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModusVivendiAppBar(
    title: String,
    onNavigationClicked: () -> Unit,
    mainParametersBarOn: Boolean,
    actions: @Composable () -> Unit = {},
    @StringRes sections: List<Int> = listOf(),
    selectedSection: Int = 0,
    onTabClicked: (Int) -> Unit = {},
    tabCounter: ((Int) -> Int)? = null,
) {
    Column(modifier = Modifier) {
        if (!mainParametersBarOn) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .background(color = MaterialTheme.colorScheme.primary)
            )
        }
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
                    .height(6.dp)
                    .background(color = MaterialTheme.colorScheme.primary)
            )
        } else {
            ScrollableSectionsRow(
                sections = sections,
                selectedSection = selectedSection,
                onTabClicked = onTabClicked,
                tabCounter = tabCounter
            )
        }
    }
}

@Composable
fun ScrollableSectionsRow(
    @StringRes sections: List<Int>,
    selectedSection: Int,
    onTabClicked: (Int) -> Unit,
    tabCounter: ((Int) -> Int)?
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
                    TabText(
                        section = section,
                        selectedSection = selectedSection,
                        index = index,
                        tabCounter = tabCounter
                    )
                },
            )
        }
    }
}

@Composable
fun TabText(
    @StringRes section: Int,
    selectedSection: Int,
    index: Int,
    tabCounter: ((Int) -> Int)?
) {
    Row(
        modifier = Modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(id = section),
            color = MaterialTheme.colorScheme.onPrimary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.titleSmall.copy(
                fontWeight = if (selectedSection == index) FontWeight.Bold else FontWeight.Normal
            )
        )
        if (tabCounter != null) {
            Spacer(modifier = Modifier.size(8.dp))
            Box(
                modifier = Modifier
                    .background(
                        color = if (selectedSection == index) MaterialTheme.colorScheme.onPrimary
                        else Color(0xFFFFFFB2),
                        shape = RoundedCornerShape(12.dp)
                    )
            ) {
                Text(
                    text = tabCounter(index).toString(),
                    modifier = Modifier.padding(horizontal = 8.dp),
                    color = MaterialTheme.colorScheme.primary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        shadow = null
                    )

                )
            }
        }
    }
}