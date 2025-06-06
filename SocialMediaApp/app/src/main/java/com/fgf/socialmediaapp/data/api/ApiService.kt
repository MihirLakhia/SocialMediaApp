package com.fgf.socialmediaapp.data.api

import com.fgf.socialmediaapp.data.model.PexelsResponse
import dagger.Provides
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Inject


interface ApiService {
    @GET("curated")
    suspend fun getPhotos(@Query("page") page: Int, @Query("per_page") perPage: Int): PexelsResponse
}