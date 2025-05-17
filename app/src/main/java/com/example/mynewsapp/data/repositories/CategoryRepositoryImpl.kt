package com.example.mynewsapp.data.repositories

import com.example.mynewsapp.R
import com.example.mynewsapp.domain.interfaces.CategoryRepository
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor() : CategoryRepository {


    override fun getCategories(): ArrayList<Int> {
        return arrayListOf(
            R.string.all,
            R.string.business,
            R.string.entertainment,
            R.string.general,
            R.string.health,
            R.string.science,
            R.string.sports,
            R.string.technology

        )
    }

    override fun getSearchCategories(): ArrayList<Int> {
        return arrayListOf(
            R.string.news,
            R.string.topic,
            R.string.author
        )
    }
}