package com.example.mynewsapp.domain.usecases.followingusecases

import com.example.mynewsapp.domain.domainmodels.FollowModel
import javax.inject.Inject

class GetSearchedFollowSourcesUseCase @Inject constructor() {
    operator fun invoke(sources: List<FollowModel>, query: String): List<FollowModel> {
        return sources.filter { sources ->
            sources.sourceName.contains(query, ignoreCase = true) == true
        }
    }
}