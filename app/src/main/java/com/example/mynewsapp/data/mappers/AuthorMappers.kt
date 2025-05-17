package com.example.mynewsapp.data.mappers

import com.example.mynewsapp.data.model.author.Author
import com.example.mynewsapp.domain.domainmodels.AuthorModel


fun Author.toDomain(): AuthorModel{
    return AuthorModel(sourceName,sourceImg,sourceFollowerCount)
}
