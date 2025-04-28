package com.example.mynewsapp.data.local.dao

import androidx.room.Dao
import androidx.room.Query

@Dao
interface IsNewsBookmarkedDao {

    @Query("SELECT EXISTS(SELECT 1 FROM Bookmark WHERE url = :url)")
    suspend fun isBookmarked(url: String): Boolean
}