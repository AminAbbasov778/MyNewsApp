package com.example.mynewsapp.domain.usecases.signupusecases

import com.example.mynewsapp.data.repositories.RegisterRepositoryImpl
import com.example.mynewsapp.domain.usecases.commonusecases.EmailValidationUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SignupUseCasesModule {

    @Provides
    @Singleton
    fun provideIsEmptySignupFieldUseCase(): IsEmptySignupFieldUseCase = IsEmptySignupFieldUseCase()


    @Provides
    @Singleton
    fun providePasswordLowerCaseValidationUseCase(): PasswordLowerCaseValidationUseCase {
        return PasswordLowerCaseValidationUseCase()
    }

    @Provides
    @Singleton
    fun providePasswordUpperCaseValidationUseCase(): PasswordUpperCaseValidationUseCase {
        return PasswordUpperCaseValidationUseCase()
    }

    @Provides
    @Singleton
    fun providePasswordDigitValidationUseCase(): PasswordDigitValidationUseCase {
        return PasswordDigitValidationUseCase()
    }

    @Provides
    @Singleton
    fun providePasswordSpecialCharacterValidationUseCase(): PasswordSpecialCharacterValidationUseCase {
        return PasswordSpecialCharacterValidationUseCase()
    }

    @Provides
    @Singleton
    fun provideConfirmPasswordValidationUseCase(): ConfirmPasswordValidationUseCase {
        return ConfirmPasswordValidationUseCase()
    }

    @Provides
    @Singleton
    fun provideSignupUserUseCase(
        registerRepositoryImpl: RegisterRepositoryImpl,
    ): SignupUserUseCase {
        return SignupUserUseCase(registerRepositoryImpl)
    }

    @Provides
    @Singleton
    fun provideSignupValidationUseCase(
        isEmptySignupFieldUseCase: IsEmptySignupFieldUseCase,
        emailValidationUseCase: EmailValidationUseCase,
        passwordValidationUseCase: PasswordValidationUseCase,
        confirmPasswordValidationUseCase: ConfirmPasswordValidationUseCase,
    ): SignupValidationUseCase {
        return SignupValidationUseCase(
            isEmptySignupFieldUseCase, emailValidationUseCase,
            passwordValidationUseCase, confirmPasswordValidationUseCase
        )
    }
}