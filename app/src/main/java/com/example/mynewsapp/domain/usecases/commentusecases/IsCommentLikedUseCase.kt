package com.example.mynewsapp.domain.usecases.commentusecases

import com.example.mynewsapp.domain.interfaces.CommentRepository
import javax.inject.Inject

class IsCommentLikedUseCase @Inject constructor(
    private val commentRepository: CommentRepository
) {
    suspend operator fun invoke(commentId: String): Result<Boolean> {
        return commentRepository.isCommentLiked(commentId)
    }
}