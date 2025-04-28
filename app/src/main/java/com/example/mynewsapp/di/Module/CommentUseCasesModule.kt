package com.example.mynewsapp.di.Module

import com.example.mynewsapp.domain.interfaces.CommentRepository
import com.example.mynewsapp.domain.usecases.commentusecases.AddCommentUseCase
import com.example.mynewsapp.domain.usecases.commentusecases.BuildNestedCommentsUseCase
import com.example.mynewsapp.domain.usecases.commentusecases.CommentDomainModelUseCase
import com.example.mynewsapp.domain.usecases.commentusecases.GetCommentsUseCase
import com.example.mynewsapp.domain.usecases.commonusecases.ChangeIsoToMillisFromApiUseCase
import com.example.mynewsapp.domain.usecases.commonusecases.TimeDifferenceUseCase
import com.example.mynewsapp.domain.usecases.profileUseCase.ChangeIsoToMillisFromFirebaseUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent :: class)
object CommentUseCasesModule {
    @Provides
    @Singleton
    fun provideAddCommentUseCase(commentRepository: CommentRepository):AddCommentUseCase = AddCommentUseCase(commentRepository)

    @Provides
    @Singleton
    fun provideGetCommentsUseCase(commentRepository: CommentRepository,commentDomainModelUseCase: CommentDomainModelUseCase,buildNestedCommentsUseCase: BuildNestedCommentsUseCase):GetCommentsUseCase = GetCommentsUseCase(commentRepository,commentDomainModelUseCase,buildNestedCommentsUseCase)

    @Provides
    @Singleton
    fun provideCommentDomainModelUseCase(changeIsoToMillisFromFirebaseUseCase: ChangeIsoToMillisFromFirebaseUseCase, timeDifferenceUseCase: TimeDifferenceUseCase): CommentDomainModelUseCase = CommentDomainModelUseCase(changeIsoToMillisFromFirebaseUseCase,timeDifferenceUseCase)

    @Provides
    @Singleton
    fun provideBuildNestedCommentsUseCase():BuildNestedCommentsUseCase = BuildNestedCommentsUseCase()

}