package com.povush.modusvivendi.data.state

import com.povush.modusvivendi.data.dataclass.Quest

data class QuestlinesUiState(
    val quests: List<Quest> = listOf() // TODO: Take from local data
)
