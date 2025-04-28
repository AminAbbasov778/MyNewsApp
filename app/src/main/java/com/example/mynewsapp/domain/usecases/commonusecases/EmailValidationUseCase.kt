package com.example.mynewsapp.domain.usecases.commonusecases

import android.util.Patterns
import com.example.mynewsapp.domain.domainstates.DomainValidationState
import javax.inject.Inject

class EmailValidationUseCase @Inject constructor(
) {
    operator fun invoke(email: String): DomainValidationState {
        val isValidEmail = Patterns.EMAIL_ADDRESS.matcher(email).matches()
        if (isValidEmail) {
            return DomainValidationState.Success
        } else {
            return DomainValidationState.Error.InvalidEmail
        }
    }
}