package com.povush.modusvivendi.ui.navigation

import androidx.annotation.DrawableRes
import androidx.lifecycle.ViewModel
import com.povush.modusvivendi.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class ModalNavigationUiState(
    @DrawableRes val coatOfArmsRes: Int,
    val countryName: String,
    val handle: String,
    val isGodMode: Boolean = false,
    val accountsExpanded: Boolean = false
)

class ModalNavigationViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ModalNavigationUiState(
        R.drawable.img_imperial_direction_coat_of_arms,
        "Imperial Direction",
        "@cosmologicalRenaissance"
    ))
    val uiState = _uiState.asStateFlow()

    fun onHandleClicked() {
        _uiState.update {
            it.copy(accountsExpanded = !uiState.value.accountsExpanded)
        }
    }

    fun switchGodMode() {
        _uiState.update {
            it.copy(isGodMode = !uiState.value.isGodMode)
        }
    }
}