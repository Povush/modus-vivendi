package com.povush.modusvivendi.data.state

import com.povush.modusvivendi.data.dataclass.Quest
import com.povush.modusvivendi.data.datasource.myAdditionalQuests
import com.povush.modusvivendi.data.datasource.myMainQuests

data class QuestlinesUiState(
    // TODO: Take from local data
    val mainQuests: List<Quest> = myMainQuests,
    val additionalQuests: List<Quest> = myAdditionalQuests,
    val completedQuests: List<Quest> = listOf(),
    val failedQuests: List<Quest> = listOf(),

    val selectedQuestSection: Int = 0
)
