package com.povush.modusvivendi.data

import android.content.Context
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.viewmodel.compose.viewModel
import com.povush.modusvivendi.data.db.ModusVivendiDatabase
import com.povush.modusvivendi.data.repository.OfflineQuestsRepository
import com.povush.modusvivendi.ui.questlines.QuestViewModel

class AppDataContainer(private val context: Context) {
    val offlineQuestsRepository: OfflineQuestsRepository by lazy {
        OfflineQuestsRepository(
            ModusVivendiDatabase.getDatabase(context).questDao(),
            ModusVivendiDatabase.getDatabase(context).taskDao()
        )
    }
}