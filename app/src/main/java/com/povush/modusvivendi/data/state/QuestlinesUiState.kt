package com.povush.modusvivendi.data.state

import com.povush.modusvivendi.data.dataclass.Quest
import com.povush.modusvivendi.data.datasource.povishQuests

data class QuestlinesUiState(
    val quests: List<Quest> = povishQuests // TODO: Take from local data
)
