package com.example.mynewsapp.di.Module

import com.example.mynewsapp.domain.interfaces.RegisterRepository
import com.example.mynewsapp.domain.interfaces.SharedPreferenceRepository
import com.example.mynewsapp.domain.interfaces.UserRepository
import com.example.mynewsapp.domain.usecases.commonusecases.TimeDifferenceUseCase
import com.example.mynewsapp.domain.usecases.profileUseCase.ChangeIsoToMillisFromFirebaseUseCase
import com.example.mynewsapp.domain.usecases.profileUseCase.DeleteNewsByPublishedAtUseCase
import com.example.mynewsapp.domain.usecases.commonusecases.GetProfileDataUseCase
import com.example.mynewsapp.domain.usecases.profileUseCase.GetTimeDifferenceUseCase
import com.example.mynewsapp.domain.usecases.profileUseCase.GetUserNewsUseCase
import com.example.mynewsapp.domain.usecases.profileUseCase.LogoutUseCase
import com.example.mynewsapp.domain.usecases.profileUseCase.RemoveUserLoginInfoUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ProfileUseCasesModule {


    @Provides
    @Singleton
    fun getLogOutUseCase(registerRepository: RegisterRepository): LogoutUseCase {
        return LogoutUseCase(registerRepository)
    }

    @Provides
    @Singleton
    fun getRemoveUserLoginInfoUseCase(sharedPreferenceRepository: SharedPreferenceRepository): RemoveUserLoginInfoUseCase {
        return RemoveUserLoginInfoUseCase(sharedPreferenceRepository)
    }

    @Provides
    @Singleton
    fun provideFetchProfileDataUseCase(userRepository: UserRepository): GetProfileDataUseCase =
        GetProfileDataUseCase(userRepository)

    @Provides
    @Singleton
    fun provideGetUserNewsUseCase(userRepository: UserRepository):GetUserNewsUseCase = GetUserNewsUseCase(userRepository)

    @Provides
    @Singleton
    fun provideGetTimeDifferenceForUserNewsUseCase(timeDifferenceUseCase: TimeDifferenceUseCase,changeIsoToMillisForUserNewsUseCase: ChangeIsoToMillisFromFirebaseUseCase):GetTimeDifferenceUseCase = GetTimeDifferenceUseCase(changeIsoToMillisForUserNewsUseCase,timeDifferenceUseCase)

   @Provides
   @Singleton
   fun provideChangeIsoToMillisForUserNewsUseCase():ChangeIsoToMillisFromFirebaseUseCase = ChangeIsoToMillisFromFirebaseUseCase()

    @Provides
    @Singleton
    fun provideDeleteNewsByPublishedAt(userRepository: UserRepository):DeleteNewsByPublishedAtUseCase = DeleteNewsByPublishedAtUseCase(userRepository)
}