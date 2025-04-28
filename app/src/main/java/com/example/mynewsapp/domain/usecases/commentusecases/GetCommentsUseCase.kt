package com.example.mynewsapp.domain.usecases.commentusecases

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.mynewsapp.domain.domainmodels.CommentDomainModel
import com.example.mynewsapp.domain.interfaces.CommentRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetCommentsUseCase @Inject constructor(
    val commentRepository: CommentRepository,
    val commentDomainModelUseCase: CommentDomainModelUseCase,
    val buildNestedCommentsUseCase: BuildNestedCommentsUseCase,
) {
    @RequiresApi(Build.VERSION_CODES.O)
    suspend operator fun invoke(url: String): Flow<Result<List<CommentDomainModel>>> {
        return commentRepository.getComments(url).map { result ->
            result.map {
                val commentList = commentDomainModelUseCase(it)
                buildNestedCommentsUseCase(commentList)
            }
        }
    }
}