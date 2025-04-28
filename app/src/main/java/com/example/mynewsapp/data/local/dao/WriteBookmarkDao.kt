package com.example.mynewsapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import com.example.mynewsapp.data.local.entity.BookmarkEntity

@Dao
interface WriteBookmarkDao {

    @Insert
    suspend fun insertBookmark(news: BookmarkEntity)
}
