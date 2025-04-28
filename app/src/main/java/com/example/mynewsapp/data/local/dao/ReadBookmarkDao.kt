package com.example.mynewsapp.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.mynewsapp.data.local.entity.BookmarkEntity


@Dao
interface ReadBookmarkDao {

    @Query("SELECT * FROM Bookmark")
    fun getBookmark(): kotlinx.coroutines.flow.Flow<List<BookmarkEntity>>

}