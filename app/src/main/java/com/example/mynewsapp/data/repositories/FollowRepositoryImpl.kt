package com.example.mynewsapp.data.repositories

import com.example.mynewsapp.data.model.follow.FollowModel
import com.example.mynewsapp.domain.interfaces.FollowRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FollowRepositoryImpl @Inject constructor(val firebaseAuth: FirebaseAuth,val firebaseFireStore: FirebaseFirestore) : FollowRepository {

        override suspend fun followNewsSource(followModel: FollowModel): Result<Unit> {
            val user = firebaseAuth.currentUser ?: return Result.failure(Exception("User not logged in"))

            return try {
                val data = hashMapOf(
                    "sourceName" to followModel.sourceName,
                    "sourceLogo" to followModel.sourceImg,
                    "sourceFollowerCount" to followModel.sourceFollowerCount,
                    "followedAt" to FieldValue.serverTimestamp()
                )

                firebaseFireStore
                    .collection("users")
                    .document(user.uid)
                    .collection("followings")
                    .document(followModel.sourceName)
                    .set(data)
                    .await()

                Result.success(Unit)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    override suspend fun unfollowNewsSource(sourceName: String): Result<Unit> {
        val user = firebaseAuth.currentUser ?: return Result.failure(Exception("User not logged in"))

        return try {
            firebaseFireStore
                .collection("users")
                .document(user.uid)
                .collection("followings")
                .document(sourceName)
                .delete()
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun isNewsSourceFollowed(sourceName: String): Result<Boolean> {
        val user = firebaseAuth.currentUser ?: return Result.failure(Exception("User not logged in"))
        return try {
            val documentSnapshot = firebaseFireStore
                .collection("users")
                .document(user.uid)
                .collection("followings")
                .document(sourceName)
                .get()
                .await()
            Result.success(documentSnapshot.exists())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }



    override suspend fun getFollowedSources(): Flow<Result<List<FollowModel>>> = callbackFlow {
        try {
            val user = firebaseAuth.currentUser ?: run {
                trySend(Result.failure(Exception("User not logged in")))
                close()
                return@callbackFlow
            }

            val listener = firebaseFireStore
                .collection("users")
                .document(user.uid)
                .collection("followings")
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        trySend(Result.failure(error))
                        return@addSnapshotListener
                    }

                    try {
                        val sources = snapshot?.documents?.mapNotNull { doc ->
                            val name = doc.getString("sourceName")
                            val logo = doc.getString("sourceLogo")
                            val sourceFollowerCount = doc.getLong("sourceFollowerCount")?.toInt() ?: 0
                            if (name != null && logo != null) {
                                FollowModel(name, logo,sourceFollowerCount)
                            } else null
                        } ?: emptyList()

                        trySend(Result.success(sources))
                    } catch (e: Exception) {
                        trySend(Result.failure(e))
                    }
                }

            awaitClose { listener.remove() }

        } catch (e: Exception) {
            trySend(Result.failure(e))
            close()
        }
    }





}