package com.povush.modusvivendi.ui.questlines

import com.povush.modusvivendi.data.repository.QuestSortingMethod

data class QuestlinesUiState(
    val selectedQuestSection: Int = 0,
    val sortingMethod: QuestSortingMethod = QuestSortingMethod.BY_NAME_UP
)
