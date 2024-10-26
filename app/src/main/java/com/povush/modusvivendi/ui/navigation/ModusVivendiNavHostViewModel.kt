package com.povush.modusvivendi.ui.navigation

import androidx.lifecycle.ViewModel
import com.povush.modusvivendi.data.firebase.AccountService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ModusVivendiNavHostViewModel @Inject constructor(
    private val accountService: AccountService
) : ViewModel() {

    val hasProfile: Boolean
        get() = accountService.hasProfile()
}