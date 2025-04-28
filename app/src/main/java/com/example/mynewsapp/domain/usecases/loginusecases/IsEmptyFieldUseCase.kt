package com.example.mynewsapp.domain.usecases.loginusecases

import com.example.mynewsapp.domain.domainstates.DomainValidationState

class IsEmptyFieldUseCase {
    operator fun invoke(email: String, password: String): DomainValidationState {
        if (email.isEmpty() || password.isEmpty()) {
            return DomainValidationState.Error.EmptyField
        } else {
            return DomainValidationState.Success
        }

    }
}