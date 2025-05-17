package com.example.mynewsapp.domain.interfaces

import com.example.mynewsapp.domain.domainmodels.AuthorModel

interface AuthorsRepository {
    fun getAuthors(): List<AuthorModel>
}