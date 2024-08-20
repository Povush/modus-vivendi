package com.povush.modusvivendi.data.state

import com.povush.modusvivendi.data.dataclass.Quest
import com.povush.modusvivendi.data.dataclass.QuestType
import com.povush.modusvivendi.data.datasource.myAdditionalQuests
import com.povush.modusvivendi.data.datasource.myMainQuests

data class QuestlinesUiState(
    val quests: Map<QuestType, List<Quest>> = emptyMap(),
    val selectedQuestSection: QuestType = QuestType.Main
)
