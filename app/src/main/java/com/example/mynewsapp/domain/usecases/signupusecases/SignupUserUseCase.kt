package com.example.mynewsapp.domain.usecases.signupusecases

import com.example.mynewsapp.data.repositories.RegisterRepositoryImpl
import com.google.firebase.auth.AuthResult
import javax.inject.Inject

class SignupUserUseCase @Inject constructor(
    private val registerRepositoryImpl: RegisterRepositoryImpl,
) {
    suspend operator fun invoke(email: String, password: String): Result<AuthResult> =
        registerRepositoryImpl.signupUser(email, password)


}