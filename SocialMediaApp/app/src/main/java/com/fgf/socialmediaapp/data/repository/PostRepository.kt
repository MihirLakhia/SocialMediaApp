package com.fgf.socialmediaapp.data.repository

import com.fgf.socialmediaapp.data.api.ApiService
import com.fgf.socialmediaapp.data.model.Comment
import com.fgf.socialmediaapp.data.model.Post
import javax.inject.Inject

class PostRepository @Inject constructor(private val api: ApiService) {
    suspend fun fetchPosts(page: Int, perPage: Int): List<Post> {
        val response = api.getPhotos(page, perPage)
        return response.photos.map {
            Post(
                id = it.id, title = it.title, imageUrl = it.src.medium,
                body = it.body,
                isLiked = it.liked
            )
        }
    }
}
