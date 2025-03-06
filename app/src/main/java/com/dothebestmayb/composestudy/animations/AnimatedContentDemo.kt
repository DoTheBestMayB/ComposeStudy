package com.dothebestmayb.composestudy.animations

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
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
 * AnimatedContent
 *
 * targetState가 변경될 때 animation이 동작한다.
 *
 * transitionSpec : {enter transition} togetherWith {exit transition} 형식으로 선언
 *
 * 아래 예시에서는 toggle을 이용해 true, false content만 표시했는데
 * sealed interface를 이용해 state를 정의하면 when절을 활용해 더 많은 상태에 따른 Composable을 적용할 수 있다.
 *
 */
@Composable
fun AnimatedContentDemo(modifier: Modifier = Modifier) {
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

        AnimatedContent(
            targetState = toggle,
            transitionSpec = {
                slideIntoContainer(
                    towards = if (toggle) {
                        AnimatedContentTransitionScope.SlideDirection.Right
                    } else {
                        AnimatedContentTransitionScope.SlideDirection.Left
                    },
                    animationSpec = tween(1500)
                ) togetherWith slideOutOfContainer(
                    towards = if (toggle) {
                        AnimatedContentTransitionScope.SlideDirection.Right
                    } else {
                        AnimatedContentTransitionScope.SlideDirection.Left
                    },
                    animationSpec = tween(1500)
                )
            }
        ) { toggle ->
            if (toggle) {
                Text(
                    text = "hello world!",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(16.dp)
                        .border(
                            width = 2.dp,
                            color = Color.Red,
                        )
                        .wrapContentSize()
                )
            } else {
                Text(
                    text = "Bye bye world!",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(16.dp)
                        .border(
                            width = 2.dp,
                            color = Color.Green,
                        )
                        .wrapContentSize()
                )
            }
        }
        Text(
            text = "I'm below"
        )
    }
}

@Preview(
    showBackground = true,
)
@Composable
private fun AnimatedContentDemoPreview() {
    ComposeStudyTheme {
        AnimatedContentDemo()
    }
}
