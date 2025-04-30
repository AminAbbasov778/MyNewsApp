package com.example.mynewsapp.data.local.entity


import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.mynewsapp.data.model.latestnews.Source


@Entity(tableName = "Bookmark")
data class BookmarkEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val author: String,
    val content: String,
    val description: String,
    val publishedAt: String,
    @Embedded val source: SourceEntity,
    val title: String,
    val url: String ,
    val urlToImage: String,
    val timeDifference: String,

) {

}