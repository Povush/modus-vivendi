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

data class SignInUiState(
    val email: String = "",
    val password: String = ""
)

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val accountService: AccountService
) : ViewModel() {

    private val _uiState = MutableStateFlow(SignInUiState())
    val uiState = _uiState.asStateFlow()

    fun onEmailChange(input: String) {
        _uiState.update { it.copy(email = input) }
    }

    fun onPasswordChange(input: String) {
        _uiState.update { it.copy(password = input) }
    }

    fun enter() {
        viewModelScope.launch {
            accountService.signIn(uiState.value.email, uiState.value.password)
        }
    }
}