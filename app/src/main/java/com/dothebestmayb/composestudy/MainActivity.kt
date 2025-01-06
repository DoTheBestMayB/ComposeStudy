package com.dothebestmayb.composestudy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.dothebestmayb.composestudy.basic_modifier.SpacingModifierDemo
import com.dothebestmayb.composestudy.basic_modifier.SpacingModifierLazyDemo
import com.dothebestmayb.composestudy.basic_modifier.SpacingModifierScaffoldAndWindowInsetsDemo
import com.dothebestmayb.composestudy.basic_modifier.self_study.WipeToReplyRoot
import com.dothebestmayb.composestudy.state_management.home_work.TodoScreenRoot
import com.dothebestmayb.composestudy.ui.theme.ComposeStudyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposeStudyTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    WipeToReplyRoot(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}
