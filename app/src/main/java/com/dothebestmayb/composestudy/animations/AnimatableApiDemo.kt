package com.dothebestmayb.composestudy.animations

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.dothebestmayb.composestudy.ui.theme.ComposeStudyTheme
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

/**
 * Animatable을 활용해서 여러 Animation을 순차적으로 적용해보자.
 *
 * snapTo : 애니메이션 없이 특정 Value로 바로 변경할 때 사용함
 */
@Composable
private fun AnimatableApiDemo(modifier: Modifier = Modifier) {
    var size by remember {
        mutableStateOf(IntSize.Zero)
    }
    val step1Offset = remember {
        Animatable(
            initialValue = 0f,

            )
    }
    val step2Offset = remember {
        Animatable(
            initialValue = 0f,

            )
    }
    val scope = rememberCoroutineScope()

    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .onSizeChanged {
                    size = it
                }
        ) {
            // 오른쪽 아래로 1 step만에 이동
            Box(
                modifier = Modifier
                    .size(100.dp)
                    // offset은 animation Value가 변경돼도 recomposition이 발생하지 않음
                    .offset {
                        // 사각형의 오른쪽 끝이 화면의 끝과 맞도록 Box의 width인 100dp를 뺌
                        IntOffset(
                            x = ((size.width - 100.dp.roundToPx()) * step1Offset.value).roundToInt(),
                            y = ((size.height - 100.dp.roundToPx()) * step1Offset.value).roundToInt(),
                        )
                    }
                    .background(Color.Red)
            )
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .offset {
                        // 사각형의 오른쪽 끝이 화면의 끝과 맞도록 Box의 width인 100dp를 뺌
                        IntOffset(
                            x = ((size.height - 100.dp.roundToPx()) * step2Offset.value).roundToInt(),
                            y = ((size.height - 100.dp.roundToPx()) * step1Offset.value).roundToInt(),
                        )
                    }
                    .graphicsLayer {
                        val scale = 1 - (step2Offset.value * 0.25f)
                        scaleX = scale
                        scaleY = scale
                    }
                    .background(Color.Green)
            )
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .offset {
                        // 사각형의 오른쪽 끝이 화면의 끝과 맞도록 Box의 width인 100dp를 뺌
                        IntOffset(
                            x = ((size.width - 100.dp.roundToPx()) * step1Offset.value).roundToInt(),
                            y = ((size.width - 100.dp.roundToPx()) * step2Offset.value).roundToInt(),
                        )
                    }
                    .graphicsLayer {
                        val scale = 1 - (step2Offset.value * 0.5f)
                        scaleX = scale
                        scaleY = scale
                    }
                    .background(Color.Blue)
            )
        }

        Button(
            onClick = {
                scope.launch {
                    // animateTo는 suspend 함수이기 때문에, 원하는 애니메이션을 순차적으로 실행할 수 있다.
                    step1Offset.animateTo(
                        targetValue = 1f,
                        animationSpec = tween(
                            durationMillis = 5000,
                            easing = EaseInOutCubic
                        )
                    )
                    // 애니메이션 없이 특정 값으로 변경할 때 사용
//                    step1Offset.snapTo(1f)
                    step2Offset.animateTo(
                        targetValue = 1f,
                        animationSpec = tween(
                            durationMillis = 5000,
                            easing = EaseInOutCubic
                        )
                    )

                    // 만약 stpe1 과 step2 애니메이션을 동시에 적용하고 싶으면
                    // launch 블록을 이용해 개별적으로 실행하면 된다.
//                    launch {
//                        step1Offset.animateTo(
//                            targetValue = 1f,
//                            animationSpec = tween(
//                                durationMillis = 5000,
//                                easing = EaseInOutCubic
//                            )
//                        )
//                    }
//                    launch {
//                        step2Offset.animateTo(
//                            targetValue = 1f,
//                            animationSpec = tween(
//                                durationMillis = 5000,
//                                easing = EaseInOutCubic
//                            )
//                        )
//                    }
                }
            }
        ) {
            Text("Start")
        }
    }
}

@Preview(
    showBackground = true,
)
@Composable
private fun AnimatableApiDemoPreview() {
    ComposeStudyTheme {
        AnimatableApiDemo()
    }
}
