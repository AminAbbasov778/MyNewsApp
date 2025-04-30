package com.example.mynewsapp.domain.usecases.loginusecases

import com.example.mynewsapp.domain.domainstates.DomainValidationState
import javax.inject.Inject

class IsEmptyFieldUseCase  @Inject constructor() {
    operator fun invoke(email: String, password: String): DomainValidationState {
        if (email.isEmpty() || password.isEmpty()) {
            return DomainValidationState.Error.EmptyField
        } else {
            return DomainValidationState.Success
        }

    }
}