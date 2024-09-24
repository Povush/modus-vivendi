package com.povush.modusvivendi.ui.questlines

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.povush.modusvivendi.R
import com.povush.modusvivendi.data.model.QuestType
import com.povush.modusvivendi.ui.AppViewModelProvider
import com.povush.modusvivendi.ui.appbar.ModusVivendiAppBar
import com.povush.modusvivendi.ui.navigation.NavigationDestination
import com.povush.modusvivendi.ui.theme.NationalTheme

object QuestCreateDestination : NavigationDestination {
    override val route = "create_quest"
    override val titleRes = R.string.create_quest
}

@Composable
fun QuestCreateScreen(
    navigateBack: () -> Boolean,
    viewModel: QuestCreateViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            ModusVivendiAppBar(
                titleRes = QuestCreateDestination.titleRes,
                navigation = {
                    IconButton(onClick = { navigateBack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimary,
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /*TODO: Save quest*/ }) {
                        Icon(
                            imageVector = Icons.Filled.Done,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimary,
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(8.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            QuestTitle(
                value = uiState.name,
                onValueChange = { input -> viewModel.updateQuestName(input) }
            )
            QuestType(
                expanded = uiState.typeExpanded,
                onExpandedChange = { typeExpanded -> viewModel.updateTypeExpanded(typeExpanded) },
                questType = uiState.type,
                onQuestTypeChange = { questType -> viewModel.updateType(questType) }
            )
        }
    }
}

@Composable
fun QuestTitle(
    value: String,
    onValueChange: (String) -> Unit
) {
    TextField(
        value = value,
        onValueChange = { onValueChange(it) },
        label = { Text(stringResource(R.string.quest_title)) },
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
        ),
        modifier = Modifier.fillMaxWidth(),
        textStyle = MaterialTheme.typography.headlineSmall,
        singleLine = true,
    )
}

@OptIn(ExperimentalMaterial3Api::class,ExperimentalFoundationApi::class)
@Composable
fun QuestType(
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    questType: QuestType,
    onQuestTypeChange: (QuestType) -> Unit
) {
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { onExpandedChange(it) },
        modifier = Modifier
    ) {
        TextField(
            value = stringResource(questType.textResId),
            onValueChange = { },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            readOnly = true,
            label = { Text(stringResource(R.string.quest_type)) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Checklist,
                    contentDescription = null,
                    modifier = Modifier.size(30.dp)
                )
            },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = ExposedDropdownMenuDefaults.textFieldColors()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { onExpandedChange(false) },
            modifier = Modifier
        ) {
            QuestType.entries.forEach { questType ->
                DropdownMenuItem(
                    text = { Text(stringResource(questType.textResId)) },
                    onClick = {
                        onQuestTypeChange(questType)
                        onExpandedChange(false)
                    },
                    modifier = Modifier
                )
            }
        }
    }
}

@Preview
@Composable
fun QuestCreateScreenPreview() {
    NationalTheme {
        QuestType(
            expanded = false,
            onExpandedChange = {  },
            questType = QuestType.ADDITIONAL,
            onQuestTypeChange = {  }
        )
    }
}