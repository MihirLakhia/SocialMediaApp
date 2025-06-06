package com.fgf.socialmediaapp.data.model

data class Post(
    val id: Int,
    val title: String,
    val body: String,
    var isLiked: Boolean = false,
    val imageUrl: String = "https://picsum.photos/300/200?random=$id"
)
