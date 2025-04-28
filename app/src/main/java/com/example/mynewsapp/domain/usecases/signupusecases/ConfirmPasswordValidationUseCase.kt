package com.example.mynewsapp.domain.usecases.signupusecases

import com.example.mynewsapp.domain.domainstates.DomainValidationState

class ConfirmPasswordValidationUseCase {
    operator fun invoke(password: String, confirmPassword: String): DomainValidationState {
        return if (password == confirmPassword) DomainValidationState.Success else DomainValidationState.Error.ConfirmPasswordDismatch
    }
}