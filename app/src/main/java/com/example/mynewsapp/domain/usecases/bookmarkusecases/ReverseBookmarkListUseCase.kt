package com.example.mynewsapp.domain.usecases.bookmark

import com.example.mynewsapp.data.local.entity.BookmarkEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ReverseBookmarkListUseCase {
    operator fun invoke(bookmarks: Flow<List<BookmarkEntity>>): Flow<List<BookmarkEntity>> {
        return bookmarks.map { it.reversed() }
    }
}