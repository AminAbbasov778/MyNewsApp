package com.example.mynewsapp.domain.interfaces

interface CategoryRepository {
    fun getCategories(): ArrayList<Int>
    fun getSearchCategories(): ArrayList<Int>
}