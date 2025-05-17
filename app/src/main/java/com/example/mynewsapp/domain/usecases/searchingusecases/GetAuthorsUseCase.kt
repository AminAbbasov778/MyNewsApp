package com.example.mynewsapp.domain.usecases.searchingusecases

import com.example.mynewsapp.domain.domainmodels.AuthorModel
import com.example.mynewsapp.domain.interfaces.AuthorsRepository
import javax.inject.Inject

class GetAuthorsUseCase @Inject constructor(val authorsRepository: AuthorsRepository) {
    operator fun invoke(): List<AuthorModel> = authorsRepository.getAuthors()
}