package com.example.mynewsapp.domain.usecases.loginusecases

import com.example.mynewsapp.domain.domainstates.DomainValidationState
import com.example.mynewsapp.domain.usecases.commonusecases.EmailValidationUseCase
import com.example.mynewsapp.domain.usecases.commonusecases.PasswordLengthValidationUseCase
import javax.inject.Inject

class LoginValidationUseCase @Inject constructor(
    val isEmptyFieldUseCase: IsEmptyFieldUseCase,
    val emailValidationUseCase: EmailValidationUseCase,
    val shortPasswordValidationUseCase: PasswordLengthValidationUseCase,
) {
    operator fun invoke(email: String, password: String): DomainValidationState {
        val isEmpty = isEmptyFieldUseCase(email, password)
        if (isEmpty is DomainValidationState.Error.EmptyField) return isEmpty

        val isInvalidEmail = emailValidationUseCase(email)
        if (isInvalidEmail is DomainValidationState.Error.InvalidEmail) return isInvalidEmail

        val isShortPassword = shortPasswordValidationUseCase(password)
        return isShortPassword
    }
}