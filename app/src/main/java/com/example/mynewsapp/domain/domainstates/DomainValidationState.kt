package com.example.mynewsapp.domain.domainstates

sealed class DomainValidationState {
    object Success : DomainValidationState()
    sealed class Error : DomainValidationState() {
        object EmptyField : Error()
        object InvalidEmail : Error()
        object ConfirmPasswordDismatch : Error()
        object ShortPassword : Error()
        object NoDigit : Error()
        object NoUpperCase : Error()
        object NoLowerCase : Error()
        object NoSpecialChar : Error()

    }
}