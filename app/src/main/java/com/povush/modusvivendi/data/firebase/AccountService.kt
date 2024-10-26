package com.povush.modusvivendi.data.firebase

import com.povush.modusvivendi.data.model.CountryProfile
import kotlinx.coroutines.flow.Flow

interface AccountService {
    val currentProfile: Flow<CountryProfile?>
    fun hasProfile(): Boolean
    suspend fun signIn(email: String, password: String)
    suspend fun signUp(email: String, password: String)
    suspend fun signOut()
    suspend fun deleteProfile()
}