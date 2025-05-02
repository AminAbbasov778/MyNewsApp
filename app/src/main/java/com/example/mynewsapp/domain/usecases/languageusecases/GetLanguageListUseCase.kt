package com.example.mynewsapp.domain.usecases.languageusecases

import com.example.mynewsapp.data.model.language.Language
import com.example.mynewsapp.domain.domainmodels.LanguageModel
import com.example.mynewsapp.domain.interfaces.LanguageRepository
import com.example.mynewsapp.domain.mappers.toDomain
import javax.inject.Inject

class GetLanguageListUseCase @Inject constructor(val languageRepository: LanguageRepository) {
   operator fun invoke(): List<LanguageModel> = languageRepository.getLanguageList()
}