package com.povush.modusvivendi.ui.questlines

data class QuestUiState(
    val canBeCompleted: Boolean,
    val expanded: Boolean = false,
    val pinned: Boolean = false
)
