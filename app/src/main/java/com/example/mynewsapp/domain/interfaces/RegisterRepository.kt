package com.example.mynewsapp.domain.interfaces

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser

interface RegisterRepository {

    suspend fun loginUser(email: String, password: String): Result<Boolean>
    suspend fun signupUser(email: String, password: String): Result<AuthResult>
    fun logout(): Result<Boolean>
     suspend fun signInWithGoogle(idToken: String): Result<FirebaseUser?>
     suspend fun signInWithFacebook(token: String): Result<FirebaseUser?>


}