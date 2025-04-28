package com.example.mynewsapp.domain.usecases.commonusecases

import com.example.mynewsapp.domain.domainstates.DomainValidationState
import javax.inject.Inject

class PasswordLengthValidationUseCase @Inject constructor() {
    operator fun invoke(password: String): DomainValidationState {
        if (password.length >= 8) {
            return DomainValidationState.Success
        } else {
            return DomainValidationState.Error.ShortPassword
        }
    }
}