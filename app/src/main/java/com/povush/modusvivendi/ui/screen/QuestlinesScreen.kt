package com.povush.modusvivendi.ui.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.povush.modusvivendi.R
import com.povush.modusvivendi.data.dataclass.Quest
import com.povush.modusvivendi.data.dataclass.Task
import com.povush.modusvivendi.data.dataclass.highDifficulty
import com.povush.modusvivendi.ui.screen.component.AppBarWithSectionsAndSearch
import com.povush.modusvivendi.ui.screen.component.QuestCard

@Composable
fun QuestlinesScreen() {
    Scaffold(
        topBar = {
            AppBarWithSectionsAndSearch(
                title = stringResource(R.string.questlines),
                onNavigationClicked = { /*TODO*/ }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            

            val sampleQuest = Quest(
                title = "Code of reality II",
                difficulty = highDifficulty,
                description = "The outcome of lengthy parliamentary debates of the Direction to take in the IT field was the decision to focus on mobile application development. The main advantages of this choice include higher demand compared to frontend development, greater impact on the immediately visible result compared to backend development, the ability to port game mechanics easily, and local compatibility with the current demands of programmers. But most importantly, we believe that the future lies in mobile development.",
                tasks = listOf(
                    Task(
                        text = "Take a short primary course on Android development on Kotlin",
                        isCompleted = true
                    ),
                    Task(text = "Go through Android Basics with Compose"),
                    Task(text = "Create an application for linguistic simulation"),
                    Task(text = "Create an application for Ilya's diploma"),
                    Task(
                        text = "To dissect the entire Play Market",
                        isAdditional = true
                    )
                ),
                isCompleted = false,
                dateOfCompletion = null
            )

            QuestCard(
                quest = sampleQuest
            )
        }
    }
}



