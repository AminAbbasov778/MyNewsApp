package com.example.mynewsapp.data.local.entity


import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "Bookmark")
data class BookmarkEntity(
    val sourceName: String,
    val imageUrl: String,
    val newsTitle: String,
    val newDescription: String,
    val timeDifference: String,
    val url: String,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
) {

}