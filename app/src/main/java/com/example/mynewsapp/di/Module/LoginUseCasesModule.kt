package com.example.mynewsapp.domain.usecases.loginusecases

import com.example.mynewsapp.domain.interfaces.RegisterRepository
import com.example.mynewsapp.domain.interfaces.SharedPreferenceRepository
import com.example.mynewsapp.domain.usecases.commonusecases.EmailValidationUseCase
import com.example.mynewsapp.domain.usecases.commonusecases.PasswordLengthValidationUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LoginUseCasesModule {


    @Provides
    @Singleton
    fun provideSavingUserLoginInfoUseCase(
        sharedPreferenceRepository: SharedPreferenceRepository,
    ): SavingUserLoginInfoUseCase = SavingUserLoginInfoUseCase(sharedPreferenceRepository)


    @Provides
    @Singleton
    fun provideLoginResultUseCase(
        registerRepository: RegisterRepository,
    ): LoginResultUseCase = LoginResultUseCase(registerRepository)


    @Provides
    @Singleton
    fun provideIsEmptyFieldUseCase(): IsEmptyFieldUseCase = IsEmptyFieldUseCase()


    @Provides
    @Singleton
    fun provideLoginValidationUseCase(
        isEmptyFieldUseCase: IsEmptyFieldUseCase,
        emailValidationUseCase: EmailValidationUseCase,
        passwordLengthValidationUseCase: PasswordLengthValidationUseCase,
    ): LoginValidationUseCase {
        return LoginValidationUseCase(
            isEmptyFieldUseCase,
            emailValidationUseCase,
            passwordLengthValidationUseCase
        )
    }
}