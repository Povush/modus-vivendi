package com.povush.modusvivendi.ui.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.povush.modusvivendi.R
import com.povush.modusvivendi.ui.screen.component.AppBarWithSectionsAndSearch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun QuestlinesScreen() {
    Scaffold(
        topBar = {
            AppBarWithSectionsAndSearch(
                title = stringResource(R.string.questlines),
                onNavigationClicked = { /*TODO*/ }
            )
        }
    ) {
        Column(
            modifier = Modifier
        ) {

        }
    }
}



