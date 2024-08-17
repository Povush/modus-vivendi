package com.povush.modusvivendi.data.state

import com.povush.modusvivendi.data.dataclass.Quest
import com.povush.modusvivendi.data.dataclass.QuestSection
import com.povush.modusvivendi.data.datasource.povishQuests

data class QuestlinesUiState(
    val mainQuests: List<Quest> = povishQuests, // TODO: Take from local data
    val additionalQuests: List<Quest> = listOf(Quest(), Quest()),
    val completedQuests: List<Quest> = listOf(Quest()),
    val failedQuests: List<Quest> = listOf(),
    val questSections: List<QuestSection> = listOf(),
    val selectedQuestSectionIndex: Int = 0
)
