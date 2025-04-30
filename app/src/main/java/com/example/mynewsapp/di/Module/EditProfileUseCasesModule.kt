package com.example.mynewsapp.di.Module

import android.content.Context
import com.example.mynewsapp.domain.interfaces.UserRepository
import com.example.mynewsapp.domain.usecases.editprofileusecases.ConvertUriToBase64UseCase
import com.example.mynewsapp.domain.usecases.editprofileusecases.UpdateUserProfileUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object EditProfileUseCasesModule {

    @Provides
    @Singleton
    fun provideConvertUriToBase64UseCase(@ApplicationContext context: Context): ConvertUriToBase64UseCase =
        ConvertUriToBase64UseCase(context)
}