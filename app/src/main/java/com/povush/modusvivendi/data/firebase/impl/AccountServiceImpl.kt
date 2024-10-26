package com.povush.modusvivendi.data.firebase.impl

import android.content.Context
import androidx.credentials.CredentialManager
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.povush.modusvivendi.data.firebase.AccountService
import com.povush.modusvivendi.data.model.CountryProfile
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AccountServiceImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : AccountService {

//    private val activity = context as Activity
    private val credentialManager = CredentialManager.create(context)
    private val auth = Firebase.auth

    override val currentProfile: Flow<CountryProfile?>
        get() = callbackFlow {
            val listener =
                FirebaseAuth.AuthStateListener { auth ->
                    this.trySend(auth.currentUser?.let { CountryProfile(it.uid) })
                }
            Firebase.auth.addAuthStateListener(listener)
            awaitClose { Firebase.auth.removeAuthStateListener(listener) }
        }

    override fun hasProfile(): Boolean {
        return Firebase.auth.currentUser != null
    }

    override suspend fun signIn(email: String, password: String) {
        Firebase.auth.signInWithEmailAndPassword(email, password).await()

    }

//    suspend fun signIn(): IntentSender? {
//        val result = try {
//            oneTapClient.beginSignIn(
//                buildSignInRequest()
//            ).await()
//        } catch(e: Exception) {
//            e.printStackTrace()
//            if(e is CancellationException) throw e
//            null
//        }
//        return result?.pendingIntent?.intentSender

    override suspend fun signUp(email: String, password: String) {
        Firebase.auth.createUserWithEmailAndPassword(email, password).await()

    }

//    suspend fun signUp(username: String, password: String): SignUpResult {
//        return try {
//            credentialManager.createCredential(
//                context = activity,
//                request = CreatePasswordRequest(
//                    id = username,
//                    password = password
//                )
//            )
//            SignUpResult.Success(username)
//        } catch(e: CreateCredentialCancellationException) {
//            e.printStackTrace()
//            SignUpResult.Cancelled
//        } catch(e: CreateCredentialException) {
//            e.printStackTrace()
//            SignUpResult.Failure
//        }
//    }

    override suspend fun signOut() {
        Firebase.auth.signOut()
    }

    override suspend fun deleteProfile() {
        Firebase.auth.currentUser!!.delete().await()
    }
}