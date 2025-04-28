package com.example.mynewsapp.domain.usecases.commonusecases

import com.example.mynewsapp.domain.interfaces.CategoryRepository
import javax.inject.Inject

class GetCategoryUseCase @Inject constructor(
    private val categoryRepository: CategoryRepository,
) {
    operator fun invoke(): ArrayList<String> {
        return categoryRepository.getCategories()
    }
}