package com.povush.modusvivendi.data.state

import com.povush.modusvivendi.data.dataclass.Quest
import com.povush.modusvivendi.data.dataclass.QuestType

data class QuestlinesUiState(
    val questsByTypes: Map<QuestType, List<Quest>> = emptyMap(),
    val selectedQuestSection: QuestType = QuestType.Main
)
