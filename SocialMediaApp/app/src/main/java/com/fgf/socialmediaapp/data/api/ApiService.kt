package com.fgf.socialmediaapp.data.api

import com.fgf.socialmediaapp.data.model.PexelsResponse
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiService {
    @GET("curated")
    suspend fun getPhotos(@Query("page") page: Int, @Query("per_page") perPage: Int): PexelsResponse
}