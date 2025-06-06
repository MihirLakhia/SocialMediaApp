package com.fgf.socialmediaapp.data.model

data class Comment(
    val id: Int = System.currentTimeMillis().toInt(),
    val timestamp: Long = System.currentTimeMillis(),
    val postId: Int,
    val text: String
)