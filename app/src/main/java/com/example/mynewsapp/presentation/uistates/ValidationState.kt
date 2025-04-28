package com.example.mynewsapp.presentation.uistates

sealed class ValidationState {
    data class Success(val messages: Int) : ValidationState()
    sealed class Error : ValidationState() {
        data class EmptyField(val messages: Int) : Error()
        data class InvalidEmail(val messages: Int) : Error()
        data class UnsafetyPassword(val messages: Int) : Error()
        data class ShortPassword(val messages: Int) : Error()
        data class ConfirmPasswordDismatch(val messages: Int) : Error()

    }
}