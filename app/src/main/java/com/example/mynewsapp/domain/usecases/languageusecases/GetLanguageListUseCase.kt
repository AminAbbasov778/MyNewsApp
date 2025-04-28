package com.example.mynewsapp.domain.usecases.languageusecases

import com.example.mynewsapp.data.model.language.LanguageModel
import com.example.mynewsapp.domain.interfaces.LanguageRepository
import javax.inject.Inject

class GetLanguageListUseCase @Inject constructor(val languageRepository: LanguageRepository) {
   operator fun invoke(): List<LanguageModel> = languageRepository.getLanguageList()
}