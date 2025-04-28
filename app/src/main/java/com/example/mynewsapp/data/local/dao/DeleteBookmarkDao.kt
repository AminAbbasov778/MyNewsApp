package com.example.mynewsapp.data.local.dao

import androidx.room.Dao
import androidx.room.Query

@Dao
interface DeleteBookmarkDao {
    @Query("DELETE FROM Bookmark WHERE url = :url")
    suspend fun deleteBookmark(url: String)
}