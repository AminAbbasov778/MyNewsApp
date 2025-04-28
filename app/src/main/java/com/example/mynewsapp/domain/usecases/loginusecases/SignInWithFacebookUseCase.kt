package com.example.mynewsapp.domain.usecases.loginusecases

import com.example.mynewsapp.domain.interfaces.RegisterRepository
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject

class SignInWithFacebookUseCase @Inject constructor(
    private val registerRepository: RegisterRepository
) {
    suspend operator fun invoke(token: String): Result<FirebaseUser?> {
        return registerRepository.signInWithFacebook(token)
    }
}
