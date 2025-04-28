package com.example.mynewsapp.di.Module

import android.content.Context
import androidx.room.Room
import com.example.mynewsapp.data.local.dao.DeleteBookmarkDao
import com.example.mynewsapp.data.local.dao.IsNewsBookmarkedDao
import com.example.mynewsapp.data.local.dao.ReadBookmarkDao
import com.example.mynewsapp.data.local.dao.WriteBookmarkDao
import com.example.mynewsapp.data.local.database.BookmarkDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): BookmarkDatabase {
        return Room.databaseBuilder(context, BookmarkDatabase::class.java, "Bookmark")
            .fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun provideWriteBookmark(bookmarkDatabase: BookmarkDatabase): WriteBookmarkDao {
        return bookmarkDatabase.getBookmarkWriteDao()
    }


    @Provides
    @Singleton
    fun provideReadBookmark(bookmarkDatabase: BookmarkDatabase): ReadBookmarkDao {
        return bookmarkDatabase.getBookmarkReadDao()
    }

    @Singleton
    @Provides
    fun provideDeleteBookmark(bookmarkDatabase: BookmarkDatabase): DeleteBookmarkDao {
        return bookmarkDatabase.getBookmarkDeleteDao()
    }

    @Provides
    @Singleton
    fun provideBookmarkedNewsDao(bookmarkDatabase: BookmarkDatabase): IsNewsBookmarkedDao {
        return bookmarkDatabase.getBookmarkedNewsDao()
    }
}
