package com.dothebestmayb.composestudy.animations

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.EaseInExpo
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dothebestmayb.composestudy.ui.theme.ComposeStudyTheme

/**
 * Preview에서 Start Animation Preview를 누르면 시간대 별로 애니메이션 효과 확인할 수 있다.
 */
@Composable
fun AnimatedVisibilityDemo(modifier: Modifier = Modifier) {
    var toggle by remember {
        mutableStateOf(false)
    }
    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        Button(
            onClick = {
                toggle = !toggle
            }
        ) {
            Text("Toggle")
        }
        // easing은 애니메이션의 속도를 조절함
        // EaseInExpo는 지수적으로 점점 빨라짐
        val easing = FastOutSlowInEasing

        AnimatedVisibility(
            visible = toggle,
            // keyframes를 이용하면 프레임 별로 애니메이션을 자세하게 설정할 수 있다.
            enter = scaleIn(
                animationSpec = keyframes {
                    durationMillis = 5000
                    // scale(효과에 대한 값?) at 시작 시간(밀리초) using 효과
                    0.75f at 2500 using EaseInExpo
                    0.25f at 3760 using LinearEasing
                    1f at 5000 using FastOutSlowInEasing
                }
            )
            // spring : 최대 크기, 최소 크기에 도달하면 팅기는 효과를 준다.
//            enter = scaleIn(
//                animationSpec = spring(
//                    dampingRatio = Spring.DampingRatioHighBouncy
//                )
//            ),
//            exit = scaleOut(
//                animationSpec = spring(
//                    dampingRatio = Spring.DampingRatioHighBouncy
//                )
//            )
            // tween : 점진적으로 커지거나 작아짐
//            enter = scaleIn(
//                animationSpec = tween(
//                    durationMillis = 5000,
//                    delayMillis = 300,
//                    easing = easing,
//                )
//            ) + fadeIn(
//                animationSpec = tween(
//                    durationMillis = 5000,
//                    delayMillis = 300,
//                    easing = easing,
//                )
//            ),
//            exit = scaleOut(
//                animationSpec = tween(
//                    durationMillis = 5000,
//                    delayMillis = 300,
//                    easing = easing,
//                )
//            ) + fadeOut(
//                animationSpec = tween(
//                    durationMillis = 5000,
//                    delayMillis = 300,
//                    easing = easing,
//                )
//            ),
        ) {
            Text(
                text = "Hello World!",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(16.dp)
                    .border(
                        width = 5.dp,
                        color = Color.Red
                    )
                    .wrapContentSize() // fillMaxWidth를 이용해 텍스트를 중앙 정렬시키고, 클릭 영역은 텍스트만큼만 차지하도록 설정
            )
        }
        Text("Hello World!")
    }
}

@Preview(
    showBackground = true,
)
@Composable
private fun AnimatedVisibilityDemoPreview() {
    ComposeStudyTheme {
        AnimatedVisibilityDemo()
    }
}
