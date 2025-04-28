package com.example.mynewsapp.di.Module

import android.content.Context
import android.content.SharedPreferences
import com.example.mynewsapp.data.local.dao.DeleteBookmarkDao
import com.example.mynewsapp.data.local.dao.IsNewsBookmarkedDao
import com.example.mynewsapp.data.local.dao.ReadBookmarkDao
import com.example.mynewsapp.data.local.dao.WriteBookmarkDao
import com.example.mynewsapp.data.remote.RequestService
import com.example.mynewsapp.data.repositories.BookmarkDatabaseRepositoryImpl
import com.example.mynewsapp.data.repositories.CameraRepositoryImpl
import com.example.mynewsapp.data.repositories.CategoryRepositoryImpl
import com.example.mynewsapp.data.repositories.CommentRepositoryImpl
import com.example.mynewsapp.data.repositories.FollowRepositoryImpl
import com.example.mynewsapp.data.repositories.LanguageRepositoryImpl
import com.example.mynewsapp.data.repositories.RegisterRepositoryImpl
import com.example.mynewsapp.data.repositories.RetrofitRepositoryImpl
import com.example.mynewsapp.data.repositories.SharedPreferenceRepositoryImpl
import com.example.mynewsapp.data.repositories.ThemeRepositoryImpl
import com.example.mynewsapp.data.repositories.TopicRepositoryImpl
import com.example.mynewsapp.data.repositories.UserRepositoryImpl
import com.example.mynewsapp.data.repository.FavoriteRepositoryImpl
import com.example.mynewsapp.domain.interfaces.BookmarkDatabaseRepository
import com.example.mynewsapp.domain.interfaces.CameraRepository
import com.example.mynewsapp.domain.interfaces.CategoryRepository
import com.example.mynewsapp.domain.interfaces.CommentRepository
import com.example.mynewsapp.domain.interfaces.FavoriteRepository
import com.example.mynewsapp.domain.interfaces.FollowRepository
import com.example.mynewsapp.domain.interfaces.LanguageRepository
import com.example.mynewsapp.domain.interfaces.RegisterRepository
import com.example.mynewsapp.domain.interfaces.RetrofitRepository
import com.example.mynewsapp.domain.interfaces.SharedPreferenceRepository
import com.example.mynewsapp.domain.interfaces.ThemeRepository
import com.example.mynewsapp.domain.interfaces.TopicRepository
import com.example.mynewsapp.domain.interfaces.UserRepository
import com.facebook.CallbackManager
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    fun provideAuthRepository(
        firebaseAuth: FirebaseAuth,
        googleSignInClient: GoogleSignInClient
    ): RegisterRepository {
        return RegisterRepositoryImpl(firebaseAuth, googleSignInClient)
    }
    @Provides
    @Singleton
    fun provideRetrofitRepository(requestService: RequestService): RetrofitRepository =
        RetrofitRepositoryImpl(requestService)


    @Provides
    @Singleton
    fun provideBookmarkDatabaseRepository(
        readBookmarkDao: ReadBookmarkDao,
        writeBookmarkDao: WriteBookmarkDao,
        deleteBookmarkDao: DeleteBookmarkDao,
        isNewsBookmarkedDao: IsNewsBookmarkedDao,
    ): BookmarkDatabaseRepository =
        BookmarkDatabaseRepositoryImpl(
            readBookmarkDao,
            writeBookmarkDao,
            deleteBookmarkDao,
            isNewsBookmarkedDao
        )

    @Provides
    @Singleton
    fun provideEncryptedSharedPreferenceRepository(
        @Named("encrypted") encryptedSharedPreferences: SharedPreferences,
        @Named("default") sharedPreference: SharedPreferences,
    ): SharedPreferenceRepository =
        SharedPreferenceRepositoryImpl(encryptedSharedPreferences, sharedPreference)


    @Provides
    @Singleton
    fun getCategoryRepository(): CategoryRepository = CategoryRepositoryImpl()

    @Provides
    @Singleton
    fun provideCameraRepository(@ApplicationContext context: Context): CameraRepository =
        CameraRepositoryImpl(context)

    @Provides
    @Singleton
    fun provideUserRepository(
        firebaseStore: FirebaseFirestore,
        firebaseAuth: FirebaseAuth,
    ): UserRepository = UserRepositoryImpl(firebaseStore, firebaseAuth)

    @Provides
    @Singleton
    fun provideTopicRepository(firebaseAuth: FirebaseAuth,firestore: FirebaseFirestore): TopicRepository = TopicRepositoryImpl(firebaseAuth,firestore)


    @Provides
    @Singleton
    fun provideFollowRepository(firebaseAuth: FirebaseAuth,firestore: FirebaseFirestore):FollowRepository = FollowRepositoryImpl(firebaseAuth,firestore)

    @Provides
    @Singleton
    fun provideFavoriteRepository(firebaseAuth: FirebaseAuth,firestore: FirebaseFirestore):FavoriteRepository =
        FavoriteRepositoryImpl(firebaseAuth,firestore)


    @Provides
    @Singleton
    fun provideLanguageRepositoryImpl(@Named("default") sharedPreferences: SharedPreferences):LanguageRepository = LanguageRepositoryImpl(sharedPreferences)


    @Provides
    @Singleton
    fun provideCommentRepositoryImpl(firebaseAuth: FirebaseAuth,firestore: FirebaseFirestore): CommentRepository = CommentRepositoryImpl(firebaseAuth,firestore)

    @Provides
    @Singleton
    fun provideThemeRepository(@Named("default") sharedPreferences: SharedPreferences): ThemeRepository = ThemeRepositoryImpl(sharedPreferences)


}