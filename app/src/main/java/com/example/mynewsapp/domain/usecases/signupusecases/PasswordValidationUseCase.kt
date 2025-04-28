package com.example.mynewsapp.domain.usecases.signupusecases

import com.example.mynewsapp.domain.domainstates.DomainValidationState
import com.example.mynewsapp.domain.usecases.commonusecases.PasswordLengthValidationUseCase
import javax.inject.Inject

class PasswordValidationUseCase @Inject constructor(
    val shortPasswordValidationUseCase: PasswordLengthValidationUseCase,
    val digitValidationUseCase: PasswordDigitValidationUseCase,
    val lowerCaseValidationUseCase: PasswordLowerCaseValidationUseCase,
    val upperCaseValidationUseCase: PasswordUpperCaseValidationUseCase,
    val specialCharValidationUseCase: PasswordSpecialCharacterValidationUseCase,

    ) {

    operator fun invoke(
        password: String,
    ): DomainValidationState {


        val shortResult = shortPasswordValidationUseCase(password)
        if (shortResult is DomainValidationState.Error) return shortResult

        val digitResult = digitValidationUseCase(password)
        if (digitResult is DomainValidationState.Error) return digitResult

        val lowerResult = lowerCaseValidationUseCase(password)
        if (lowerResult is DomainValidationState.Error) return lowerResult

        val upperResult = upperCaseValidationUseCase(password)
        if (upperResult is DomainValidationState.Error) return upperResult

        val specialResult = specialCharValidationUseCase(password)
        if (specialResult is DomainValidationState.Error) return specialResult

        return DomainValidationState.Success


    }
}