package com.example.mynewsapp.data.repositories

import com.example.mynewsapp.domain.interfaces.CategoryRepository
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor() : CategoryRepository {


    override fun getCategories(): ArrayList<String> {
        return arrayListOf(
            "All",
            "Business",
            "Entertainment",
            "General",
            "Health",
            "Science",
            "Sports",
            "Technology"

        )
    }
}