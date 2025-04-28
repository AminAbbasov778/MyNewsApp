package com.example.mynewsapp.di.Module

import com.example.mynewsapp.domain.interfaces.UserRepository
import com.example.mynewsapp.domain.usecases.createnewsusecases.CreateNewsUseCase
import com.example.mynewsapp.domain.usecases.createnewsusecases.GetTimeStampUseCase
import com.example.mynewsapp.domain.usecases.createnewsusecases.UserNewsModelUseCase
import com.example.mynewsapp.domain.usecases.editprofileusecases.ConvertUriToBase64UseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent :: class)
object CreateNewsUseCasesModule {
    @Provides
    @Singleton
    fun provideCreateNewsUseCase(userRepository: UserRepository):CreateNewsUseCase = CreateNewsUseCase(userRepository)

    @Provides
    @Singleton
    fun provideUserNewsModelUseCase(convertUriToBase64UseCase: ConvertUriToBase64UseCase,timeStampUseCase: GetTimeStampUseCase,userRepository: UserRepository):UserNewsModelUseCase = UserNewsModelUseCase(convertUriToBase64UseCase,timeStampUseCase,userRepository)

   @Provides
   @Singleton
   fun provideGetTimeStampUseCase():GetTimeStampUseCase = GetTimeStampUseCase()

}