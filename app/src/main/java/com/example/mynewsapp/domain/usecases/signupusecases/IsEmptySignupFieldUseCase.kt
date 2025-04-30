package com.example.mynewsapp.domain.usecases.signupusecases

import com.example.mynewsapp.domain.domainstates.DomainValidationState
import javax.inject.Inject


class IsEmptySignupFieldUseCase  @Inject constructor(){
    operator fun invoke(
        email: String,
        password: String,
        confirmPassword: String,
    ): DomainValidationState {
        if (confirmPassword.isEmpty() || email.isEmpty() || password.isEmpty()) {
            return DomainValidationState.Error.EmptyField
        } else {
            return DomainValidationState.Success
        }

    }
}