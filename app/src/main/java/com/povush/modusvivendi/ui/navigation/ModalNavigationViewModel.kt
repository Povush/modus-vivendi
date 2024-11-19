package com.povush.modusvivendi.ui.navigation

import androidx.annotation.DrawableRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.povush.modusvivendi.R
import com.povush.modusvivendi.data.firebase.AccountService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ModalNavigationUiState(
    @DrawableRes val coatOfArmsRes: Int,
    val countryName: String,
    val handle: String,
    val isGodMode: Boolean = false,
    val accountsExpanded: Boolean = false
)

@HiltViewModel
class ModalNavigationViewModel @Inject constructor(
    private val accountService: AccountService
) : ViewModel() {
    private val _uiState = MutableStateFlow(ModalNavigationUiState(
        R.drawable.img_imperial_direction_coat_of_arms,
        "Imperial Direction",
        "@cosmologicalRenaissance"
    ))
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            accountService.currentCountryProfile.collect { countryProfile ->
                countryProfile?.let { currentProfile ->
                    _uiState.update { it.copy(
                        countryName = currentProfile.countryName,
                        handle = currentProfile.handle
                    ) }
                }
            }
        }
    }

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

    fun exitGame() {
        viewModelScope.launch {
            accountService.signOut()
        }
    }
}