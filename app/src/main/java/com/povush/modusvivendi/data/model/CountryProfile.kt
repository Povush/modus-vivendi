package com.povush.modusvivendi.data.model

import androidx.compose.ui.text.toUpperCase
import java.util.Locale

data class CountryProfile(
    val userId: String? = null,
    val countryName: String = "Unnamed kingdom",
    val handle: String = "No handle",
    val coatOfArms: Int? = null
) {
    /**
    Turns "cosmologicalRenaissance" to "@cosmologicalRenaissance [CR]".
     */
    fun getFormattedHandle(): String {
        if (!checkHandleForCamelCaseAndTwoWords(handle)) return "@$handle"
        val handleAbbreviation = handle.filterIndexed { index, c ->
            index == 0 || c.isUpperCase()
        }.uppercase()
        return "@$handle [$handleAbbreviation]"
    }

    private fun checkHandleForCamelCaseAndTwoWords(handle: String): Boolean {
        val firstAndLastLettersIsLowerCase = handle.first().isLowerCase() && handle.last().isLowerCase()
        val middleUpperCaseCount = handle.drop(1).dropLast(1).count { it.isUpperCase() }
        return firstAndLastLettersIsLowerCase && middleUpperCaseCount == 1
    }
}

