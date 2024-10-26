package com.povush.modusvivendi.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.povush.modusvivendi.data.firebase.AccountService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SignUpUiState(
    val email: String = "",
    val password: String = "",
//    val countryName: String = ""
)

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val accountService: AccountService
) : ViewModel() {

    private val _uiState = MutableStateFlow(SignUpUiState())
    val uiState = _uiState.asStateFlow()

    fun onEmailChange(input: String) {
        _uiState.update { it.copy(email = input) }
    }

    fun onPasswordChange(input: String) {
        _uiState.update { it.copy(password = input) }
    }

    fun createCountry() {
        viewModelScope.launch {
            accountService.signUp(uiState.value.email, uiState.value.password)
        }
    }
}