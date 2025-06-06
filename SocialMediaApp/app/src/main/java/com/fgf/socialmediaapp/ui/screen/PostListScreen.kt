package com.fgf.socialmediaapp.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.fgf.socialmediaapp.data.model.Comment
import com.fgf.socialmediaapp.data.model.Post
import com.fgf.socialmediaapp.ui.theme.OffWhite
import com.fgf.socialmediaapp.viewmodel.PostViewModel
import kotlinx.coroutines.flow.distinctUntilChanged

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PostListScreen(viewModel: PostViewModel = hiltViewModel(), modifier: Modifier) {
    val posts = viewModel.posts
    val isLoading by viewModel.isLoading

    val listState = rememberLazyListState()
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isLoading,
        onRefresh = {
            viewModel.loadPosts(refresh = true)
        }
    )

    Box(modifier = modifier.pullRefresh(pullRefreshState)) {
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 1.dp)
                .background(OffWhite)
        ) {
            items(posts.size) { position ->
                val post = posts[position]
                var commentText by remember { mutableStateOf("") }
                PostItem(post = post,
                    onLikeClicked = { viewModel.toggleLike(post.id) },
                    comments = post.comments,
                    onCommentAdded = {
                        viewModel.addComment(post.id, it)
                    },
                    commentText = commentText,
                    onCommentTextChange = { commentText = it })
            }
        }

        PullRefreshIndicator(
            refreshing = isLoading,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }

    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .distinctUntilChanged()
            .collect { lastVisibleItemIndex ->
                if (lastVisibleItemIndex == posts.lastIndex && !isLoading) {
                    viewModel.loadPosts()
                }
            }
    }
}

@Composable
fun PostItem(
    post: Post,
    onLikeClicked: () -> Unit,
    comments: List<Comment>,
    onCommentAdded: (String) -> Unit,
    commentText: String,
    onCommentTextChange: (String) -> Unit
) {
    Card(modifier = Modifier.padding(8.dp)) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(
                text = post.title,
                Modifier.padding(top = 4.dp),
                style = MaterialTheme.typography.h6
            )
            Spacer(modifier = Modifier.height(4.dp))
            Image(
                painter = rememberAsyncImagePainter(post.imageUrl),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop
            )
            IconButton(onClick = onLikeClicked) {
                Icon(
                    imageVector = if (post.isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = null,
                    tint = if (post.isLiked) Color.Red else Color.Gray
                )
            }
            Text(text = "Description:" + post.body, style = MaterialTheme.typography.caption)

            //comment List
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Comments:", fontWeight = FontWeight.Bold)
            comments.forEach { comment ->
                Text(text = "- ${comment.text}", style = MaterialTheme.typography.body2)
            }

            //new Comment
            Spacer(modifier = Modifier.height(4.dp))
            CommentSection(
                commentText = commentText,
                onCommentTextChange = onCommentTextChange,
                onCommentAdded = onCommentAdded
            )
        }
    }
}


@Composable
fun CommentSection(
    commentText: String,
    onCommentTextChange: (String) -> Unit,
    onCommentAdded: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            value = commentText,
            onValueChange = onCommentTextChange,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .height(48.dp)
                .padding(0.dp),
            placeholder = { Text("Add a comment...") },
            textStyle = TextStyle(fontSize = 14.sp),
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color(0xFFF0F0F0),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            )
        )

        IconButton(
            onClick = {
                if (commentText.isNotBlank()) {
                    onCommentAdded(commentText)
                    onCommentTextChange("")
                }
            }
        ) {
            Icon(
                imageVector = Icons.Default.Send,
                contentDescription = "Send Comment",
                tint = MaterialTheme.colors.primary
            )
        }
    }
}