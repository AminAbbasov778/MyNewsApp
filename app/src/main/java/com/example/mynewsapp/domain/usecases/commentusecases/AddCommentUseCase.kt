package com.example.mynewsapp.domain.usecases.commentusecases

import com.example.mynewsapp.domain.domainmodels.CommentModel
import com.example.mynewsapp.domain.interfaces.CommentRepository
import com.example.mynewsapp.domain.mappers.toDomain
import com.example.mynewsapp.presentation.uimodels.comment.NewCommentUIModel
import javax.inject.Inject

class AddCommentUseCase @Inject constructor(
    val commentRepository: CommentRepository,
) {

    suspend operator fun invoke(comment: CommentModel): Result<Unit> {
        return commentRepository.addComment(comment)
    }
}