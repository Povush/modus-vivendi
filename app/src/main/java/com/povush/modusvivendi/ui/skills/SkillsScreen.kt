package com.povush.modusvivendi.ui.skills

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.povush.modusvivendi.R
import com.povush.modusvivendi.ui.common.appbar.ModusVivendiAppBar
import com.povush.modusvivendi.ui.navigation.NavigationDestination

object SkillsDestination : NavigationDestination {
    override val route = "skills"
    override val titleRes = R.string.skills
}

@Composable
fun SkillsScreen(onNavigationClick: () -> Unit) {
    Scaffold(
        modifier = Modifier,
        topBar = {
            ModusVivendiAppBar(
                titleRes = SkillsDestination.titleRes,
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
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {  }
    }
}