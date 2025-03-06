package com.dothebestmayb.composestudy.animations

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dothebestmayb.composestudy.ui.theme.ComposeStudyTheme

/**
 *  modifier에서 Dynamic Content Size 설정 이전에
 *  animateContentSize를 선언해주면 크기가 변경될 때 애니메이션이 적용된다.
 */
@Composable
fun AnimatedContentSizeDemo(modifier: Modifier = Modifier) {
    var toggle by remember {
        mutableStateOf(false)
    }
    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Button(
            onClick = {
                toggle = !toggle
            }
        ) {
            Text("Toggle")
        }
        Box(
            modifier = Modifier
                .background(Color.Blue)
                .animateContentSize()
                .height(if (toggle) 400.dp else 200.dp)
                .fillMaxWidth()
        )
        Text(
            text = "I'm below"
        )
    }
}

@Preview
@Composable
private fun AnimatedContentSizeDemoPreview() {
    ComposeStudyTheme {
        AnimatedContentSizeDemo()
    }
}
