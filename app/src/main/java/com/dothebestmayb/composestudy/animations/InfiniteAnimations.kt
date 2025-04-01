package com.dothebestmayb.composestudy.animations

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dothebestmayb.composestudy.ui.theme.ComposeStudyTheme

/**
 * InfiniteAnimation의 대표적 예시 : CircularProgressIndicator
 *
 * InfiniteAnimation을 직접 만들어보자.
 */
@Composable
private fun InfiniteAnimations(modifier: Modifier = Modifier) {
//    CircularProgressIndicator()
    val transition = rememberInfiniteTransition(label = "infinite transition")

    // 값이 initialValue -> targetValue로 변경되는데, 애니메이션의 값으로 사용됨
    val ratio by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3000),
            // initialValue -> targetValue -> initialValue -> targetValue ... 를 반복한다.
            // RepeatMode.Restart는 initialValue -> targetValue를 반복한다.
            // 그래서 스무스한 애니메이션을 적용하고 싶다면 Reverse를 고려해보자.
            repeatMode = RepeatMode.Reverse,
        ),
        label = "ratio animation"
    )
    val color by transition.animateColor(
        initialValue = Color.Red,
        targetValue = Color.Green,
        animationSpec = infiniteRepeatable(
            animation = tween(3000),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "color animation"
    )
    Box(
        modifier = modifier
            .graphicsLayer {
                rotationZ = ratio * 360f
                scaleX = ratio
                scaleY = ratio
            }
            .size(100.dp)
            // 성능상의 이유로 background(color) 대신 drawBehind를 사용함
            // background(color)를 사용하면 동일한 효과를 주지만, 색상이 변경되는 프레임마다 recomposition됨
            // drawBehind는 draw phase에 적용됨
            .drawBehind {
                drawRect(
                    color = color,
                )
            }
    )
}

@Preview
@Composable
private fun InfiniteAnimationsPreview() {
    ComposeStudyTheme {
        InfiniteAnimations()
    }
}
