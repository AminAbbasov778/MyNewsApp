package com.example.mynewsapp.domain.usecases.commonusecases

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CommonUseCasesModule {

    @Provides
    @Singleton
    fun provideTimeDifferenceUseCase(@ApplicationContext context: Context): TimeDifferenceUseCase = TimeDifferenceUseCase(context)

}