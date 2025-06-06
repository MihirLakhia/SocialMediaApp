package com.fgf.socialmediaapp.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fgf.socialmediaapp.data.model.Comment
import com.fgf.socialmediaapp.data.model.Post
import com.fgf.socialmediaapp.data.repository.PostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val repository: PostRepository
) : ViewModel() {

    private val _posts = mutableStateListOf<Post>()
    val posts: List<Post> get() = _posts

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private var currentPage = 1
    private val perPage = 20

    init {
        loadPosts()
    }

    fun loadPosts(refresh: Boolean = false) {
        if (refresh) {
            currentPage = 1
            _posts.clear()
        }
        viewModelScope.launch {
            _isLoading.value = true
            val newPosts = repository.fetchPosts(currentPage, perPage)
            _posts.addAll(newPosts)
            _isLoading.value = false
            currentPage++
        }
    }

    fun toggleLike(postId: Int) {
        val index = _posts.indexOfFirst { it.id == postId }
        if (index != -1) {
            val post = _posts[index]
            _posts[index] = post.copy(isLiked = !post.isLiked)
        }
    }

    fun addComment(postId: Int, text: String) {
        val index = _posts.indexOfFirst { it.id == postId }
        if (index != -1) {
            val post = _posts[index]
            val updated = post.copy(
                comments = post.comments + Comment(
                    text = text,
                    timestamp = System.currentTimeMillis(),
                    postId = postId
                )
            )
            _posts[index] = updated
        }
    }
}