package com.example.mynewsapp.domain.usecases.commentusecases

import com.example.mynewsapp.domain.interfaces.CommentRepository
import javax.inject.Inject

class UnlikeCommentUseCase @Inject constructor(
    private val commentRepository: CommentRepository
) {
    suspend operator fun invoke(commentId: String, newsUrl: String): Result<Unit> {
        return commentRepository.unlikeComment(commentId, newsUrl)
    }
}