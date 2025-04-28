package com.example.mynewsapp.domain.usecases.signupusecases

import com.example.mynewsapp.domain.domainstates.DomainValidationState
import com.example.mynewsapp.domain.usecases.commonusecases.EmailValidationUseCase

import javax.inject.Inject

class SignupValidationUseCase @Inject constructor(
    val isEmptySignupFieldUseCase: IsEmptySignupFieldUseCase,
    val emailValidationUseCase: EmailValidationUseCase,
    val passwordValidationUseCase: PasswordValidationUseCase,
    val confirmPasswordValidationUseCase: ConfirmPasswordValidationUseCase,
) {
    operator fun invoke(
        email: String,
        password: String, confirmPassword: String,
    ): DomainValidationState {


        val isEmpty = isEmptySignupFieldUseCase(email, password, confirmPassword)
        if (isEmpty is DomainValidationState.Error) return isEmpty

        val isValidEmail = emailValidationUseCase(email)
        if (isValidEmail is DomainValidationState.Error) return isValidEmail

        val confirmPasswordDisMatch = confirmPasswordValidationUseCase(password, confirmPassword)
        if (confirmPasswordDisMatch is DomainValidationState.Error) return confirmPasswordDisMatch

        val isValidPassword = passwordValidationUseCase(password)
        return isValidPassword


    }
}