package com.example.mynewsapp.domain.usecases.commonusecases

import android.content.Context
import com.example.mynewsapp.domain.interfaces.CameraRepository
import com.example.mynewsapp.domain.interfaces.CategoryRepository
import com.example.mynewsapp.domain.interfaces.RetrofitRepository
import com.example.mynewsapp.domain.interfaces.SharedPreferenceRepository
import com.example.mynewsapp.domain.interfaces.UserRepository
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
    fun provideNewsResultCheckingUseCase(
        retrofitRepository: RetrofitRepository,
    ): NewsResultCheckingUseCase = NewsResultCheckingUseCase(retrofitRepository)


    @Provides
    @Singleton
    fun provideEmailValidationUseCase(): EmailValidationUseCase = EmailValidationUseCase()

    @Provides
    @Singleton
    fun providePasswordLengthValidationUseCase(): PasswordLengthValidationUseCase =
        PasswordLengthValidationUseCase()

    @Provides
    @Singleton
    fun provideGettingUserEmailUseCase(
        sharedPreferenceRepository: SharedPreferenceRepository,
    ): GettingUserEmailUseCase = GettingUserEmailUseCase(sharedPreferenceRepository)


    @Provides
    @Singleton
    fun provideTimeDifferenceUseCase(@ApplicationContext context: Context): TimeDifferenceUseCase = TimeDifferenceUseCase(context)


    @Provides
    @Singleton
    fun provideAddingTimeDifferenceToNewsUseCase(
        timeDifferenceUseCase: TimeDifferenceUseCase,
        changeIsoToMillisFromApiUseCase: ChangeIsoToMillisFromApiUseCase,
    ): AddTimeDifferenceToNewsUseCase =
        AddTimeDifferenceToNewsUseCase(timeDifferenceUseCase, changeIsoToMillisFromApiUseCase)


    @Provides
    @Singleton
    fun provideGetCategoryUseCase(
        categoryRepository: CategoryRepository,
    ): GetCategoryUseCase = GetCategoryUseCase(categoryRepository)


    @Provides
    @Singleton
    fun provideGettingNewsAfterProcessUseCase(
        newsResultCheckingUseCase: NewsResultCheckingUseCase,
        addTimeDifferenceToNewsUseCase: AddTimeDifferenceToNewsUseCase,
    ): GetProcessedNewsUseCase {
        return GetProcessedNewsUseCase(newsResultCheckingUseCase, addTimeDifferenceToNewsUseCase)
    }

    @Provides
    @Singleton
    fun provideChangingIsoToMillisUseCase(): ChangeIsoToMillisFromApiUseCase {
        return ChangeIsoToMillisFromApiUseCase()
    }

    @Provides
    @Singleton
    fun provideCapturePhotoUseCase(cameraRepository: CameraRepository): CapturePhotoUseCase =
        CapturePhotoUseCase(cameraRepository)

    @Provides
    @Singleton
    fun provideGetUserIdUseCase(userRepository: UserRepository):GetUserIdUseCase = GetUserIdUseCase(userRepository)

}