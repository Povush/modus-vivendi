package com.povush.modusvivendi.ui.questlines

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.povush.modusvivendi.R
import com.povush.modusvivendi.ui.appbar.ModusVivendiAppBar
import com.povush.modusvivendi.ui.navigation.NavigationDestination
import com.povush.modusvivendi.ui.theme.NationalTheme

object QuestCreateDestination : NavigationDestination {
    override val route = "create_quest"
    override val titleRes = R.string.create_quest
}

@Composable
fun QuestCreateScreen(
    navigateBack: Boolean
) {
    Scaffold(
        topBar = {
            ModusVivendiAppBar(
                titleRes = QuestCreateDestination.titleRes,
                navigation = {
                    IconButton(onClick = {  }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimary,
                        )
                    }
                },
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            OutlinedTextField(
                value = "",
                onValueChange = { /*TODO: Text input*/ },
                label = { Text(stringResource(R.string.title)) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                    unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                    disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                ),
                modifier = Modifier.fillMaxWidth().padding(8.dp)
            )
        }
    }
}

@Preview
@Composable
fun QuestCreateScreenPreview() {
    NationalTheme {
        QuestCreateScreen(navigateBack = true)
    }
}