package com.example.mynewsapp.domain.usecases.loginusecases

import com.example.mynewsapp.domain.interfaces.RegisterRepository
import javax.inject.Inject

class LoginResultUseCase @Inject constructor(
    private val registerRepository: RegisterRepository,
) {
    suspend operator fun invoke(email: String, password: String): Result<Boolean> =
        registerRepository.loginUser(email, password)

}
