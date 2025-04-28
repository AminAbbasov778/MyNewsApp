package com.example.mynewsapp.di.Module

import com.example.mynewsapp.domain.interfaces.BookmarkDatabaseRepository
import com.example.mynewsapp.domain.interfaces.FavoriteRepository
import com.example.mynewsapp.domain.interfaces.FollowRepository
import com.example.mynewsapp.domain.usecases.detail.ConvertingArticleToBookmarkEntityUseCase
import com.example.mynewsapp.domain.usecases.detail.DeleteBookmarkUseCase
import com.example.mynewsapp.domain.usecases.detail.IsNewsBookmarkedUseCase
import com.example.mynewsapp.domain.usecases.detail.SaveBookmarkUseCase
import com.example.mynewsapp.domain.usecases.detailusecases.FavoriteNewsUseCase
import com.example.mynewsapp.domain.usecases.detailusecases.FollowNewsSourceUseCase
import com.example.mynewsapp.domain.usecases.detailusecases.IsNewsSourceFollowedUseCase
import com.example.mynewsapp.domain.usecases.detailusecases.UnFavoriteNewUseCase
import com.example.mynewsapp.domain.usecases.detailusecases.UnfollowNewSourceUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DetailUseCasesModule {


    @Provides
    @Singleton
    fun provideConvertingArticleToBookmarkEntityUseCase(): ConvertingArticleToBookmarkEntityUseCase  = ConvertingArticleToBookmarkEntityUseCase()



    @Provides
    @Singleton
    fun provideBookmarkDeleteUseCase(
        repository: BookmarkDatabaseRepository,
    ): DeleteBookmarkUseCase  = DeleteBookmarkUseCase(repository)


    @Provides
    @Singleton
    fun provideBookmarkSaveUseCase(
        repository: BookmarkDatabaseRepository,
        nullCheckingUseCase: ConvertingArticleToBookmarkEntityUseCase,
    ): SaveBookmarkUseCase  = SaveBookmarkUseCase(repository, nullCheckingUseCase)


    @Provides
    @Singleton
    fun provideIsNewsBookmarkedUseCase(
        repository: BookmarkDatabaseRepository,
    ): IsNewsBookmarkedUseCase  = IsNewsBookmarkedUseCase(repository)


    @Provides
    @Singleton
    fun provideUnfollowNewSourceUseCase(followRepository: FollowRepository):UnfollowNewSourceUseCase = UnfollowNewSourceUseCase(followRepository)

    @Provides
    @Singleton
    fun provideFollowNewsSourceUseCase(followRepository: FollowRepository):FollowNewsSourceUseCase = FollowNewsSourceUseCase(followRepository)

    @Provides
    @Singleton
    fun provideFavoriteNewsUseCase(favoriteRepository: FavoriteRepository):FavoriteNewsUseCase = FavoriteNewsUseCase(favoriteRepository)

    @Provides
    @Singleton
    fun provideUnFavoriteNewUseCase(favoriteRepository: FavoriteRepository):UnFavoriteNewUseCase = UnFavoriteNewUseCase(favoriteRepository)

    @Provides
    @Singleton
    fun provideIsNewsSourceFollowedUseCase(followRepository: FollowRepository):IsNewsSourceFollowedUseCase = IsNewsSourceFollowedUseCase(followRepository)
}