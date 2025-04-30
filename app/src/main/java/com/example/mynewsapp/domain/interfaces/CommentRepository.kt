package com.example.mynewsapp.domain.interfaces

import com.example.mynewsapp.data.model.comment.Comment
import com.example.mynewsapp.domain.domainmodels.CommentModel
import kotlinx.coroutines.flow.Flow

interface CommentRepository {
    suspend fun addComment(commentModel: CommentModel): Result<Unit>
    suspend fun getComments(newsUrl: String): Flow<Result<List<Comment>>>
    suspend fun likeComment(commentId: String, newsUrl: String): Result<Unit>
    suspend fun unlikeComment(commentId: String, newsUrl: String): Result<Unit>
    suspend fun isCommentLiked(commentId: String): Result<Boolean>
}