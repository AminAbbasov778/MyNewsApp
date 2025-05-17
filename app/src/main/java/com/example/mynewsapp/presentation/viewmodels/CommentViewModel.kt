package com.example.mynewsapp.presentation.viewmodels

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mynewsapp.R
import com.example.mynewsapp.domain.usecases.commentusecases.AddCommentUseCase
import com.example.mynewsapp.domain.usecases.commentusecases.GetCommentsUseCase
import com.example.mynewsapp.domain.usecases.commentusecases.IsCommentLikedUseCase
import com.example.mynewsapp.domain.usecases.commentusecases.LikeCommentUseCase
import com.example.mynewsapp.domain.usecases.commentusecases.UnlikeCommentUseCase
import com.example.mynewsapp.domain.usecases.commonusecases.GetProfileDataUseCase
import com.example.mynewsapp.domain.usecases.commonusecases.GetUserIdUseCase
import com.example.mynewsapp.domain.usecases.createnewsusecases.GetTimeStampUseCase
import com.example.mynewsapp.presentation.mappers.toDomain
import com.example.mynewsapp.presentation.mappers.toUi
import com.example.mynewsapp.presentation.uimodels.comment.CommentLikeStatus
import com.example.mynewsapp.presentation.uimodels.comment.CommentsUiModel
import com.example.mynewsapp.presentation.uimodels.comment.NewCommentUIModel
import com.example.mynewsapp.presentation.uimodels.profile.NewProfileUiModel
import com.example.mynewsapp.presentation.uistates.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CommentViewModel @Inject constructor(
    private val addCommentUseCase: AddCommentUseCase,
    private val getProfileDataUseCase: GetProfileDataUseCase,
    private val getUserIdUseCase: GetUserIdUseCase,
    private val getTimeStampUseCase: GetTimeStampUseCase,
    private val getCommentsUseCase: GetCommentsUseCase,
    private val isCommentLikedUseCase: IsCommentLikedUseCase,
    private val likeCommentUseCase: LikeCommentUseCase,
    private val unLikeCommentUseCase: UnlikeCommentUseCase,
) : ViewModel() {

    private val _commentState = MutableLiveData<UiState<Int>>()
    val commentState: LiveData<UiState<Int>> get() = _commentState

    private val _comments = MutableLiveData<UiState<List<CommentsUiModel>>>()
    val comments: LiveData<UiState<List<CommentsUiModel>>> get() = _comments

    private val _selectedReplyComment = MutableLiveData<CommentsUiModel?>(null)
    val selectedReplyComment: LiveData<CommentsUiModel?> get() = _selectedReplyComment

    private val _likeState = MutableLiveData<UiState<Unit>>()
    val likeState: LiveData<UiState<Unit>> get() = _likeState

    private val _commentLikeStatus = MutableLiveData<Map<String, CommentLikeStatus>>(emptyMap())

    @RequiresApi(Build.VERSION_CODES.O)
    fun addComment(commentText: String, newsUrl: String) {
        if (commentText.isNotEmpty()) {
            _commentState.value = UiState.Loading
            viewModelScope.launch(Dispatchers.Main) {
                val profileResult = withContext(Dispatchers.IO) {
                    getUserProfile()
                }

                if (profileResult.isFailure) {
                    _commentState.value = UiState.Error(R.string.failed_add_comment)
                    return@launch
                }
                val isReply = selectedReplyComment.value != null

                val comment = NewCommentUIModel(
                    commentText,
                    profileResult.getOrNull()!!.imageBase64,
                    profileResult.getOrNull()!!.username,
                    getUserIdUseCase() ?: "unknown",
                    getTimeStampUseCase(),
                    newsUrl,
                    isReply,
                    selectedReplyComment.value?.parentCommentId,
                    selectedReplyComment.value?.username,
                )

                val result = withContext(Dispatchers.IO) {
                    addCommentUseCase(comment.toDomain())
                }

                if (result.isSuccess) {
                    _commentState.value = UiState.Success(R.string.success_add_comment)
                    clearData()
                } else _commentState.value = UiState.Error(R.string.failed_add_comment)
            }
        }
    }

    private suspend fun getUserProfile(): Result<NewProfileUiModel> {
        val result = getProfileDataUseCase().firstOrNull()
        return result?.let { profile ->
            if (profile.isSuccess && profile.getOrNull() != null) Result.success(
                profile.getOrNull()!!.toUi()
            )
            else Result.failure(profile.exceptionOrNull() ?: Exception("Profile is not found"))

        } ?: run {
            Result.failure(Exception("Profile is not found"))
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getComments(url: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val resultFlow = getCommentsUseCase(url)
            resultFlow.collect { result ->
                withContext(Dispatchers.Main) {
                    if (result.isSuccess) {
                        val commentDomainList = result.getOrNull() ?: emptyList()
                        val currentLikeStatus = _commentLikeStatus.value ?: emptyMap()
                        var commentUiList = commentDomainList.map { it.toUi() }
                        commentUiList = commentUiList.map { comment ->
                            val isLikedFromStatus = currentLikeStatus[comment.commentedAt]?.isLiked
                            val isLiked = isLikedFromStatus
                                ?: isCommentLikedUseCase(comment.commentedAt).getOrNull() ?: false
                            val updatedReplies = comment.replies.map { reply ->
                                val replyIsLiked = currentLikeStatus[reply.commentedAt]?.isLiked
                                    ?: isCommentLikedUseCase(reply.commentedAt).getOrNull() ?: false
                                reply.copy(isLiked = replyIsLiked)
                            }
                            comment.copy(isLiked = isLiked, replies = updatedReplies)
                        }
                        _comments.value = UiState.Success(commentUiList)
                        initializeLikeStatus(commentUiList)
                    } else {
                        _comments.value = UiState.Error(R.string.failed_to_get_comments)
                    }
                }
            }
        }
    }
    fun setReplyComment(comment: CommentsUiModel) {
        if (comment.parentUsername.isNullOrEmpty()) {
            _selectedReplyComment.value =
                comment.copy(isReply = true, parentCommentId = comment.commentedAt)

        } else {
            _selectedReplyComment.value = comment.copy(
                isReply = true,
                parentCommentId = comment.parentCommentId,
                parentUsername = comment.username
            )
        }
    }

    fun clearData() {
        _selectedReplyComment.value = null
    }

    fun toggleLikeStatus(commentId: String, newsUrl: String, isCurrentlyLiked: Boolean) {
        if (isCurrentlyLiked) {
            unlikeComment(commentId, newsUrl)
        } else {
            likeComment(commentId, newsUrl)
        }
    }

    private fun likeComment(commentId: String, newsUrl: String) {
        _likeState.value = UiState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            val result = likeCommentUseCase(commentId, newsUrl)
            withContext(Dispatchers.Main) {
                if (result.isSuccess) {
                    updateCommentLikeStatus(commentId, isLiked = true)
                    _likeState.value = UiState.Success(Unit)
                } else {
                    _likeState.value = UiState.Error(R.string.failed_favorite)
                }
            }
        }
    }

    private fun unlikeComment(commentId: String, newsUrl: String) {
        _likeState.value = UiState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            val result = unLikeCommentUseCase(commentId, newsUrl)
            withContext(Dispatchers.Main) {
                if (result.isSuccess) {
                    updateCommentLikeStatus(commentId, isLiked = false)
                    _likeState.value = UiState.Success(Unit)
                } else {
                    _likeState.value = UiState.Error(R.string.failed_unfavorite)
                }
            }
        }
    }

    private fun initializeLikeStatus(comments: List<CommentsUiModel>) {
        viewModelScope.launch(Dispatchers.IO) {
            val likeStatusMap = _commentLikeStatus.value?.toMutableMap() ?: mutableMapOf()
            comments.forEach { comment ->
                if (likeStatusMap[comment.commentedAt] == null) {
                    val isLiked = isCommentLikedUseCase(comment.commentedAt).getOrNull() ?: false
                    likeStatusMap[comment.commentedAt] = CommentLikeStatus(
                        commentId = comment.commentedAt,
                        isLiked = isLiked,
                        likesCount = comment.likesCount
                    )
                }
                comment.replies.forEach { reply ->
                    if (likeStatusMap[reply.commentedAt] == null) {
                        val isLiked = isCommentLikedUseCase(reply.commentedAt).getOrNull() ?: false
                        likeStatusMap[reply.commentedAt] = CommentLikeStatus(
                            commentId = reply.commentedAt,
                            isLiked = isLiked,
                            likesCount = reply.likesCount
                        )
                    }
                }
            }
            withContext(Dispatchers.Main) {
                _commentLikeStatus.value = likeStatusMap
                updateCommentsWithLikeStatus()
            }
        }
    }

    private fun updateCommentLikeStatus(commentId: String, isLiked: Boolean) {
        val currentStatus = _commentLikeStatus.value?.toMutableMap() ?: mutableMapOf()
        val currentLikeStatus = currentStatus[commentId] ?: CommentLikeStatus(
            commentId = commentId,
            isLiked = false,
            likesCount = 0
        )
        currentStatus[commentId] = CommentLikeStatus(
            commentId = commentId,
            isLiked = isLiked,
            likesCount = if (isLiked) currentLikeStatus.likesCount + 1 else maxOf(
                0,
                currentLikeStatus.likesCount - 1
            )
        )
        _commentLikeStatus.value = currentStatus

        updateCommentsWithLikeStatus()
    }

    private fun updateCommentsWithLikeStatus() {
        val currentComments = (_comments.value as? UiState.Success)?.data ?: return
        val updatedComments = currentComments.map { comment ->
            val status = _commentLikeStatus.value?.get(comment.commentedAt)
            val updatedComment = if (status != null) {
                comment.copy(
                    isLiked = status.isLiked,
                    likesCount = status.likesCount
                )
            } else {
                comment
            }

            val updatedReplies = comment.replies.map { reply ->
                val replyStatus = _commentLikeStatus.value?.get(reply.commentedAt)
                if (replyStatus != null) {
                    reply.copy(
                        isLiked = replyStatus.isLiked,
                        likesCount = replyStatus.likesCount
                    )
                } else {
                    reply
                }
            }
            updatedComment.copy(replies = updatedReplies)
        }
        _comments.value = UiState.Success(updatedComments)

    }
}