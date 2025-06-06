package com.fgf.socialmediaapp.data.model

data class Comment(
    val id: Int,
    val postId: Int,
    val name: String,
    val body: String
)