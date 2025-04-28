package com.example.mynewsapp.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mynewsapp.data.local.dao.DeleteBookmarkDao
import com.example.mynewsapp.data.local.dao.IsNewsBookmarkedDao
import com.example.mynewsapp.data.local.dao.ReadBookmarkDao
import com.example.mynewsapp.data.local.dao.WriteBookmarkDao
import com.example.mynewsapp.data.local.entity.BookmarkEntity

@Database(entities = [BookmarkEntity::class], version = 2, exportSchema = false)
abstract class BookmarkDatabase : RoomDatabase() {

    abstract fun getBookmarkWriteDao(): WriteBookmarkDao
    abstract fun getBookmarkReadDao(): ReadBookmarkDao
    abstract fun getBookmarkDeleteDao(): DeleteBookmarkDao
    abstract fun getBookmarkedNewsDao(): IsNewsBookmarkedDao
}
