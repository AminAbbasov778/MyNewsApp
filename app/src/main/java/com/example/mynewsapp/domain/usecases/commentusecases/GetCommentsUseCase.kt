package com.example.mynewsapp.domain.usecases.commentusecases

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.mynewsapp.domain.domainmodels.CommentModel
import com.example.mynewsapp.domain.interfaces.CommentRepository
import com.example.mynewsapp.domain.usecases.commonusecases.TimeDifferenceUseCase
import com.example.mynewsapp.domain.usecases.profileUseCase.ChangeIsoToMillisFromFirebaseUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetCommentsUseCase @Inject constructor(
    val commentRepository: CommentRepository,
    val buildNestedCommentsUseCase: BuildNestedCommentsUseCase,
    val changeIsoToMillisFromFirebaseUseCase: ChangeIsoToMillisFromFirebaseUseCase,
    val timeDifferenceUseCase: TimeDifferenceUseCase
) {
    @RequiresApi(Build.VERSION_CODES.O)
    suspend operator fun invoke(url: String): Flow<Result<List<CommentModel>>> {
        return commentRepository.getComments(url).map { result ->
            result.map { comments ->
                val commentList = comments.map {
                    it.copy(timeDifference = timeDifferenceUseCase(changeIsoToMillisFromFirebaseUseCase(it.commentedAt)))
                }
                buildNestedCommentsUseCase(commentList)
            }
        }
    }
}