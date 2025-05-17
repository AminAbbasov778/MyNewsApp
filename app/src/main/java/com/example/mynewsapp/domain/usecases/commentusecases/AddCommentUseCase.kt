package com.example.mynewsapp.domain.usecases.commentusecases

import com.example.mynewsapp.domain.domainmodels.CommentModel
import com.example.mynewsapp.domain.interfaces.CommentRepository
import javax.inject.Inject

class AddCommentUseCase @Inject constructor(
    val commentRepository: CommentRepository,
) {

    suspend operator fun invoke(comment: CommentModel): Result<Unit> {
        return commentRepository.addComment(comment)
    }
}