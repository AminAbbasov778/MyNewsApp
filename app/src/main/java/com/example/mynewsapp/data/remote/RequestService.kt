package com.example.mynewsapp.data.remote

import com.example.mynewsapp.data.model.latestnews.LatestNewsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RequestService {

    @GET("v2/everything")
    suspend fun getLatestNews(
        @Query("q") q: String,
        @Query("sortBy") sortBy: String,
        @Query("pageSize") pageSize: Int?,
        @Query("page") page: Int?,
        @Query("apiKey") apiKey: String,
    ): Response<LatestNewsResponse>


}