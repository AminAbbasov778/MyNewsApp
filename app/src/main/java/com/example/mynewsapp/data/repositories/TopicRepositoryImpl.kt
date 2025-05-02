package com.example.mynewsapp.data.repositories

import android.util.Log
import com.example.mynewsapp.R
import com.example.mynewsapp.data.mappers.toDomain
import com.example.mynewsapp.data.model.topic.TopicEntity
import com.example.mynewsapp.domain.domainmodels.TopicModel
import com.example.mynewsapp.domain.interfaces.TopicRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class TopicRepositoryImpl @Inject constructor(
    val firebaseAuth: FirebaseAuth,
    val firestore: FirebaseFirestore,
) : TopicRepository {
    override fun getTopics(): List<TopicModel> {

        val topicList = arrayListOf(
            TopicEntity(
                R.drawable.healthimg,
                "Health",
                "Get energizing workout routines, nutritious meal plans, and wellness tips to boost your overall health and vitality"
            ),
            TopicEntity(
                R.drawable.technology,
                "Technology",
                "The application of scientific knowledge to develop innovative tools, devices, and systems that improve everyday life"
            ),
            TopicEntity(
                R.drawable.art,
                "Art",
                "Art is a diverse range of human expression that reflects creativity, emotion, and culture through various forms like painting, music, and design"
            ),
            TopicEntity(
                R.drawable.politics,
                "Politics",
                "Get in-depth opinion and analysis of American and global politics, from policy decisions to international relations."
            ),
            TopicEntity(
                R.drawable.sports,
                "Sport",
                "Sports news and live coverage including scores, highlights, player updates, and expert analysis across all major leagues."
            )
        )
    return topicList.map { it.toDomain() }
}

override suspend fun saveTopic(topicName: String): Result<Unit> {
    val user = firebaseAuth.currentUser ?: return Result.failure(Exception("User not logged in"))

    return try {
        val data = hashMapOf(
            "topicName" to topicName,
            "savedAt" to FieldValue.serverTimestamp()
        )

        firestore
            .collection("users")
            .document(user.uid)
            .collection("topics")
            .document(topicName)
            .set(data)
            .await()
        Log.e("yoxla43", topicName)

        Result.success(Unit)
    } catch (e: Exception) {
        Log.e("yoxla45", "isTopicSaved: ")


        Result.failure(e)
    }
}

override suspend fun unSaveTopic(topicName: String): Result<Unit> {
    val user = firebaseAuth.currentUser ?: return Result.failure(Exception("User not logged in"))

    return try {
        firestore
            .collection("users")
            .document(user.uid)
            .collection("topics")
            .document(topicName)
            .delete()
            .await()

        Result.success(Unit)
    } catch (e: Exception) {

        Result.failure(e)
    }
}

override suspend fun getSavedTopics(): Flow<Result<List<String>>> = callbackFlow {
    try {
        val user = firebaseAuth.currentUser

        if (user == null) {
            trySend(Result.failure(Exception("User not logged in")))
            close()
            return@callbackFlow
        }

        val listenerRegistration = firestore
            .collection("users")
            .document(user.uid)
            .collection("topics")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(Result.failure(error))
                    return@addSnapshotListener
                }

                try {
                    if (snapshot != null) {
                        val topicNames = snapshot.documents.mapNotNull { it.getString("topicName") }
                        trySend(Result.success(topicNames))
                    }
                } catch (e: Exception) {
                    trySend(Result.failure(e))
                }
            }

        awaitClose { listenerRegistration.remove() }

    } catch (e: Exception) {
        trySend(Result.failure(e))
        close(e)
    }
}


}