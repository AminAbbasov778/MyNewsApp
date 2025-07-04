package com.example.mynewsapp.data.repositories

import com.example.mynewsapp.data.mappers.toData
import com.example.mynewsapp.data.mappers.toDomain
import com.example.mynewsapp.data.model.usernews.UserNews
import com.example.mynewsapp.data.model.userprofile.Profile
import com.example.mynewsapp.domain.domainmodels.ProfileModel
import com.example.mynewsapp.domain.domainmodels.UserNewsModel
import com.example.mynewsapp.domain.interfaces.UserRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    val firebaseStore: com.google.firebase.firestore.FirebaseFirestore,
    val firebaseAuth: com.google.firebase.auth.FirebaseAuth,
) : UserRepository {

    override suspend fun uploadProfileData(profileModel: ProfileModel): Result<Unit> {
        val profile = profileModel.toData()
        val user = firebaseAuth.currentUser
        val userProfile = firebaseStore.collection("users").document(user?.uid ?: "")
        return try {
            userProfile.set(
                mapOf(
                    "fullName" to profile.fullName,
                    "bio" to profile.bio,
                    "email" to profile.email,
                    "imageBase64" to profile.imageBase64,
                    "username" to profile.username,
                    "phoneNumber" to profile.phoneNumber,
                    "website" to profile.website
                )

            ).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getProfileData(): Flow<Result<ProfileModel>> = callbackFlow {
        try {
            val user = firebaseAuth.currentUser

            if (user == null) {
                trySend(Result.failure(Exception("No user")))
                close()
                return@callbackFlow
            }

            val listener = firebaseStore
                .collection("users")
                .document(user.uid)
                .addSnapshotListener { snapshot, error ->

                    val result = try {
                        when {
                            error != null -> Result.failure(error)
                            snapshot != null -> {
                                val profile = snapshot.toObject(Profile::class.java)
                                if (profile != null) {
                                    Result.success(profile.toDomain())
                                } else {
                                    Result.success(ProfileModel())
                                }
                            }
                            else -> Result.success(ProfileModel())
                        }
                    } catch (e: Exception) {
                        Result.failure(e)
                    }

                    trySend(result)
                }

            awaitClose { listener.remove() }

        } catch (e: Exception) {
            trySend(Result.failure(e))
            close(e)
        }
    }





        override suspend fun createNews(userNewsModel: UserNewsModel): Result<Unit> {
            val userNews = userNewsModel.toData()
             firebaseAuth.currentUser?.uid?.let {user ->
                val document =
                    firebaseStore.collection("users").document(user).collection("news").document(userNewsModel.publishedAt)
                return try {
                    document.set(
                        mapOf(
                            "newsTitle" to userNews.newsTitle,
                            "newsArticle" to userNews.newsArticle,
                            "imageBase64" to userNews.imageBase64,
                            "profileImageBase64" to userNews.profileImageBase64,
                            "fullName" to userNews.fullName,
                            "publishedAt" to userNews.publishedAt
                        )

                    ).await()
                    Result.success(Unit)
                } catch (e: Exception) {
                    Result.failure(e)
                }
            }
           return Result.failure(Exception("No user"))

        }

    override suspend fun getUserNews(): Flow<Result<List<UserNewsModel>>> {
        return callbackFlow {
            try {
                val user = firebaseAuth.currentUser

                if (user == null) {
                    trySend(Result.failure(Exception("No user")))
                    close()
                    return@callbackFlow
                }

                val listenerRegistration = firebaseStore
                    .collection("users")
                    .document(user.uid)
                    .collection("news")
                    .addSnapshotListener { snapshot, error ->
                        val result = try {
                            when {
                                error != null -> Result.failure(error)
                                snapshot != null -> {
                                   val news =  snapshot.toObjects(UserNews::class.java)
                                    Result.success(news.map { it.toDomain() })
                                }
                                else -> Result.success(emptyList<UserNewsModel>())
                            }
                        } catch (e: Exception) {
                            Result.failure(e)
                        }
                        trySend(result)
                    }

                awaitClose { listenerRegistration.remove() }

            } catch (e: Exception) {
                trySend(Result.failure(e))
                close(e)
            }
        }
    }


    override suspend fun deleteNewsByPublishedAt(publishedAt : String): Result<Unit> {
        val user = firebaseAuth.currentUser
       return try {
            firebaseStore.collection("users").document(user?.uid ?: "").collection("news").document(publishedAt).delete()
            Result.success(Unit)
        }catch (e: Exception){
            Result.failure(e)
        }



    }
    override fun getUserId() : String? = firebaseAuth.currentUser?.uid





}





