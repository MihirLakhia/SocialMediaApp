package com.fgf.socialmediaapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.fgf.socialmediaapp.ui.AppBar
import com.fgf.socialmediaapp.ui.screen.PostListScreen
import com.fgf.socialmediaapp.ui.theme.SocialMediaAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SocialMediaAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize(),
                    topBar = { AppBar() }
                ) { innerPadding ->
                    PostListScreen(modifier = Modifier.padding(innerPadding))
                }


            }
        }
    }
}
