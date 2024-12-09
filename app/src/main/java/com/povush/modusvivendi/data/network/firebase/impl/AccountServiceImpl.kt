package com.povush.modusvivendi.data.network.firebase.impl

import android.content.Context
import androidx.credentials.CredentialManager
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.povush.modusvivendi.data.network.firebase.AccountService
import com.povush.modusvivendi.data.model.CountryProfile
import com.povush.modusvivendi.data.model.User
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AccountServiceImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : AccountService {

    private val credentialManager = CredentialManager.create(context)

    override val currentUser: Flow<User?>
        get() = callbackFlow {
            val listener =
                FirebaseAuth.AuthStateListener { auth ->
                    this.trySend(auth.currentUser?.let { User(it.uid) })
                }
            Firebase.auth.addAuthStateListener(listener)
            awaitClose { Firebase.auth.removeAuthStateListener(listener) }
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val currentCountryProfile: Flow<CountryProfile?>
        get() = currentUser.flatMapLatest { user ->
            callbackFlow {
                val query = Firebase.firestore
                    .collection("profiles")
                    .whereEqualTo("userId", user?.id)

                val listener = query.addSnapshotListener { snapshot, exception ->
                    if (exception != null) {
                        close(exception)
                        return@addSnapshotListener
                    }

                    val profile = snapshot?.documents?.firstOrNull()?.toObject(CountryProfile::class.java)
                    trySend(profile)
                }

                awaitClose { listener.remove() }
            }
        }

    override fun hasProfile(): Boolean {
        return Firebase.auth.currentUser != null
    }

    override suspend fun signIn(email: String, password: String): Result<Unit> {
        return try {
            Firebase.auth.signInWithEmailAndPassword(email, password).await()
            Result.success(Unit)
        } catch (e: FirebaseAuthInvalidUserException) {
            Result.failure(Exception("User does not exist. Please register first."))
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            Result.failure(Exception("Invalid password. Please try again."))
        } catch (e: FirebaseAuthException) {
            Result.failure(Exception("Authentication error: ${e.message}"))
        } catch (e: Exception) {
            Result.failure(Exception("Unknown error occurred: ${e.message}"))
        }
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