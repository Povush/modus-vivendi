package com.povush.modusvivendi.data.state

import com.povush.modusvivendi.data.dataclass.Quest
import com.povush.modusvivendi.data.datasource.myAdditionalQuests
import com.povush.modusvivendi.data.datasource.myMainQuests

data class QuestlinesUiState(
    // TODO: Take from local data
    val mainQuests: MutableList<Quest> = myMainQuests,
    val additionalQuests: MutableList<Quest> = myAdditionalQuests,
    val completedQuests: MutableList<Quest> = mutableListOf(),
    val failedQuests: MutableList<Quest> = mutableListOf(),

    val selectedQuestSection: Int = 0
)
