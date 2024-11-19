package com.povush.modusvivendi.data.firebase

import com.povush.modusvivendi.data.model.CountryProfile
import com.povush.modusvivendi.data.model.User
import kotlinx.coroutines.flow.Flow

interface AccountService {
    val currentUser: Flow<User?>
    val currentCountryProfile: Flow<CountryProfile?>
    fun hasProfile(): Boolean
    suspend fun signIn(email: String, password: String): Result<Unit>
    suspend fun signUp(email: String, password: String)
    suspend fun signOut()
    suspend fun deleteProfile()
}