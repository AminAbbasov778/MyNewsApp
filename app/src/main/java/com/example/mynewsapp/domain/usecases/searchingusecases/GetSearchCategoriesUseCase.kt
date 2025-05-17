package com.example.mynewsapp.domain.usecases.searchingusecases

import com.example.mynewsapp.domain.interfaces.CategoryRepository
import javax.inject.Inject

class GetSearchCategoriesUseCase @Inject constructor(val categoryRepository: CategoryRepository) {
    operator fun invoke(): List<Int> = categoryRepository.getSearchCategories()
}