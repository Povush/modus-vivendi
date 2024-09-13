package com.povush.modusvivendi.ui

import android.content.Context
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.MutableCreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.povush.modusvivendi.ModusVivendiApplication
import com.povush.modusvivendi.data.model.Quest
import com.povush.modusvivendi.ui.questlines.QuestViewModel
import com.povush.modusvivendi.ui.questlines.QuestlinesViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            QuestlinesViewModel(
                modusVivendiApplication().container.offlineQuestsRepository
            )
        }
        initializer {
            QuestViewModel(
                modusVivendiApplication().container.offlineQuestsRepository,
                this[QuestKey] ?: throw IllegalStateException("Quest data missing")
            )
        }
    }
}

fun CreationExtras.modusVivendiApplication(): ModusVivendiApplication =
    this[AndroidViewModelFactory.APPLICATION_KEY] as ModusVivendiApplication

val QuestKey = object : CreationExtras.Key<Quest> {}

fun createQuestViewModelExtras(quest: Quest,context: Context): CreationExtras {
    val extras = MutableCreationExtras()
    extras[AndroidViewModelFactory.APPLICATION_KEY] = context as ModusVivendiApplication
    extras[QuestKey] = quest
    return extras
}