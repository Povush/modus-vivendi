package com.povush.modusvivendi.data

import android.content.Context
import com.povush.modusvivendi.data.db.ModusVivendiDatabase
import com.povush.modusvivendi.data.repository.OfflineQuestsRepository

class AppDataContainer(private val context: Context) {
    val offlineQuestsRepository: OfflineQuestsRepository by lazy {
        OfflineQuestsRepository(ModusVivendiDatabase.getDatabase(context).questDao())
    }
}