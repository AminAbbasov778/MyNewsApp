package com.example.mynewsapp.data.repositories

import com.example.mynewsapp.domain.interfaces.RegisterRepository
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class RegisterRepositoryImpl @Inject constructor(
    var firebaseAuth: FirebaseAuth, private val googleSignInClient: GoogleSignInClient,
) :
    RegisterRepository {

    override suspend fun loginUser(email: String, password: String): Result<Boolean> {
        return try {
            firebaseAuth.signInWithEmailAndPassword(email, password).await()

            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }

    }


    override suspend fun signupUser(email: String, password: String): Result<AuthResult> {
        return try {
            val authResponse = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            Result.success(authResponse)

        } catch (e: Exception) {
            Result.failure(e)
        }

    }


    override fun logout(): Result<Boolean> {
        return try {
            firebaseAuth.signOut()
            googleSignInClient.signOut()
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }

    }

    override suspend fun signInWithGoogle(idToken: String): Result<FirebaseUser?> {
        return try {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            val result = firebaseAuth.signInWithCredential(credential).await()
            if (result.user != null) {
                Result.success(result.user)
            } else {
                Result.failure(Exception("Login failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun signInWithFacebook(token: String): Result<FirebaseUser?> {
        return try {
            val credential = FacebookAuthProvider.getCredential(token)
            val result = firebaseAuth.signInWithCredential(credential).await()
            if (result.user != null) {
                Result.success(result.user)
            } else {
                Result.failure(Exception("Login failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }



}