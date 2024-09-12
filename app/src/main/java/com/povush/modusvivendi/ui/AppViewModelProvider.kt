package com.povush.modusvivendi.ui

import android.app.Application
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.povush.modusvivendi.ModusVivendiApplication
import com.povush.modusvivendi.ui.questlines.QuestlinesViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            QuestlinesViewModel()
        }
    }
}

fun CreationExtras.modusVivendiApplication(): ModusVivendiApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as ModusVivendiApplication)
