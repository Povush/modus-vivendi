package com.povush.modusvivendi.ui.questlines

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.povush.modusvivendi.R
import com.povush.modusvivendi.data.model.QuestType
import com.povush.modusvivendi.ui.AppViewModelProvider
import com.povush.modusvivendi.ui.appbar.ModusVivendiAppBar
import com.povush.modusvivendi.ui.createQuestCreateViewModelExtras
import com.povush.modusvivendi.ui.createQuestViewModelExtras
import com.povush.modusvivendi.ui.navigation.NavigationDestination
import com.povush.modusvivendi.ui.theme.NationalTheme

object QuestCreateDestination : NavigationDestination {
    override val route = "create_quest"
    override val titleRes = R.string.create_quest
}

@Composable
fun QuestCreateScreen(
    navigateBack: () -> Boolean,
    currentQuestSectionNumber: Int,
    viewModel: QuestCreateViewModel = viewModel(
        factory = AppViewModelProvider.Factory,
        extras = createQuestCreateViewModelExtras(currentQuestSectionNumber, LocalContext.current.applicationContext)
    )
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
    val isFocused = remember { mutableStateOf(false) }

    TextField(
        value = value,
        onValueChange = { onValueChange(it) },
        label = {
            Text(
                text = stringResource(R.string.quest_title),
                style = if (value.isEmpty() && !isFocused.value) {
                    MaterialTheme.typography.headlineSmall
                } else {
                    LocalTextStyle.current
                }
            )
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
            unfocusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
            disabledContainerColor = MaterialTheme.colorScheme.primaryContainer,
        ),
        modifier = Modifier
            .fillMaxWidth()
            .onFocusChanged { focusState ->
                isFocused.value = focusState.isFocused
            },
        textStyle = MaterialTheme.typography.headlineSmall,
        singleLine = true,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
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
        modifier = Modifier.padding(horizontal = 4.dp)
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
                    modifier = Modifier
                )
            },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = ExposedDropdownMenuDefaults.textFieldColors()
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { onExpandedChange(false) },
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primaryContainer)
                .exposedDropdownSize()
        ) {
            QuestType.entries.forEach { currentQuestType ->
                DropdownMenuItem(
                    text = { Text(stringResource(currentQuestType.textResId)) },
                    onClick = {
                        onQuestTypeChange(currentQuestType)
                        onExpandedChange(false)
                    },
                    modifier = Modifier,
                    trailingIcon = {
                        if (currentQuestType == questType) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                modifier = Modifier
                            )
                        }
                    }
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