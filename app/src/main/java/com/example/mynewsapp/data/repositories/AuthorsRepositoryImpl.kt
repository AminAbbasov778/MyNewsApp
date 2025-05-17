package com.example.mynewsapp.data.repositories

import com.example.mynewsapp.Utils.NewsSourceConstants
import com.example.mynewsapp.data.mappers.toDomain
import com.example.mynewsapp.data.model.author.Author
import com.example.mynewsapp.domain.domainmodels.AuthorModel
import com.example.mynewsapp.domain.interfaces.AuthorsRepository
import javax.inject.Inject

class AuthorsRepositoryImpl @Inject constructor() : AuthorsRepository {
    override fun getAuthors(): List<AuthorModel> {
        val list =  listOf(
            Author("BBC News", NewsSourceConstants.sourceLogo, 12000),
            Author("CNN", NewsSourceConstants.sourceLogo, 123000),
            Author("Vox", NewsSourceConstants.sourceLogo, 130000),
            Author("USA Today", NewsSourceConstants.sourceLogo, 112000),
            Author("CNBC", NewsSourceConstants.sourceLogo, 235000),
            Author("CNET", NewsSourceConstants.sourceLogo, 325000),
            Author("MSN", NewsSourceConstants.sourceLogo, 525000),
            Author("World News", NewsSourceConstants.sourceLogo, 225000),
        )
        return list.map { it.toDomain() }
    }

}