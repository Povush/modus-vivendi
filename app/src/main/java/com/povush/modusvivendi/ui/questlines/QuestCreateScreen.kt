package com.povush.modusvivendi.ui.questlines

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.povush.modusvivendi.R
import com.povush.modusvivendi.data.model.Difficulty
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
            TextField(
                value = uiState.name,
                onValueChange = { viewModel.updateQuestName(it) },
                label = { Text(stringResource(R.string.title)) },
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
    }
}

//@Composable
//fun OldDifficulties(
//    uiState: QuestCreateUiState,
//    updateQuestDifficulty: (Float) -> Unit
//) {
//    var difficultiesWidth by remember { mutableFloatStateOf(0f) }
//
//    val numberOfDifficulties = Difficulty.entries.size
//    val screenWidth = LocalConfiguration.current.screenWidthDp.toFloat()
//    val scale = screenWidth / difficultiesWidth
//
//    Row(
//        modifier = Modifier,
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        Column {
//            Text(
//                text = stringResource(R.string.difficulty),
//                modifier = Modifier.padding(horizontal = 8.dp)
//            )
//            Slider(
//                value = uiState.difficulty.ordinal.toFloat(),
//                onValueChange = { input -> updateQuestDifficulty(input) },
//                modifier = Modifier.width(200.dp),
//                steps = numberOfDifficulties - 2,
//                valueRange = 0f..(numberOfDifficulties - 1).toFloat(),
//            )
//        }
//        Spacer(modifier = Modifier.size(24.dp))
//        Column(
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            Icon(
//                painter = painterResource(uiState.difficulty.imgResId),
//                contentDescription = null,
//                modifier = Modifier.width(80.dp),
//                tint = uiState.difficulty.color
//            )
//            Text(
//                text = stringResource(uiState.difficulty.textResId),
//                modifier = Modifier,
//                color = uiState.difficulty.color
//            )
//        }
//    }
//}

//@Preview
@Composable
fun QuestCreateScreenPreview() {
    NationalTheme {
        QuestCreateScreen(navigateBack = { true })
    }
}