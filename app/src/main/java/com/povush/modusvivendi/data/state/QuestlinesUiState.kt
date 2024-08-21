package com.povush.modusvivendi.data.state

import com.povush.modusvivendi.data.dataclass.Quest
import com.povush.modusvivendi.data.dataclass.QuestType

data class QuestlinesUiState(
    val quests: Map<QuestType, List<Quest>> = emptyMap(),
    val selectedQuestSection: Int = QuestType.Main
)
