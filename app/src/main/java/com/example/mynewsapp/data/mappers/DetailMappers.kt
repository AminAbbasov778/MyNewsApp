package com.example.mynewsapp.data.mappers

import com.example.mynewsapp.data.model.follow.Follow
import com.example.mynewsapp.domain.domainmodels.FollowModel

fun FollowModel.toData(): Follow{
    return Follow(sourceName,sourceImg,sourceFollowerCount)
}